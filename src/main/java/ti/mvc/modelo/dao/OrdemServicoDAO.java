package ti.mvc.modelo.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import ti.mvc.connection.ConnectionFactory;
import ti.mvc.modelo.*;

public class OrdemServicoDAO {
    public void adiciona(OrdemServico ordem) {
        if (ordem.getCliente() == null || ordem.getCliente().getId() == null
                || ordem.getBicicleta() == null || ordem.getBicicleta().getId() == null
                || ordem.getDataAgendamento() == null || ordem.getDataAgendamento().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Informe cliente, bicicleta e uma data de agendamento válida.");
        if (ordem.getItens() == null || ordem.getItens().isEmpty())
            throw new IllegalArgumentException("Selecione pelo menos um serviço.");
        HashSet<Long> servicos = new HashSet<>();
        for (ItemOrdemServico item : ordem.getItens()) {
            if (item == null || item.getServico() == null || item.getServico().getId() == null
                    || !servicos.add(item.getServico().getId()))
                throw new IllegalArgumentException("Serviço inválido ou repetido.");
        }
        try (Connection con = new ConnectionFactory().getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT proprietario_id FROM bicicleta WHERE id=? FOR UPDATE")) {
                    ps.setLong(1, ordem.getBicicleta().getId());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next() || !ordem.getCliente().getId().equals(rs.getObject(1, Long.class)))
                            throw new IllegalArgumentException("A bicicleta não pertence ao cliente informado.");
                    }
                }
                long id;
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO ordem_servico (cliente_id, bicicleta_id, data_agendamento) VALUES (?, ?, ?) RETURNING id")) {
                    ps.setLong(1, ordem.getCliente().getId());
                    ps.setLong(2, ordem.getBicicleta().getId());
                    ps.setObject(3, ordem.getDataAgendamento());
                    try (ResultSet rs = ps.executeQuery()) { rs.next(); id = rs.getLong(1); }
                }
                ItemOrdemServicoDAO itensDAO = new ItemOrdemServicoDAO();
                for (Long servicoId : servicos) itensDAO.adiciona(con, id, servicoId);
                con.commit();
                ordem.setId(id);
                ordem.setStatus(StatusOrdem.AGENDADA);
                ordem.setDataExecucao(null);
                ordem.setSubtotal(0.0);
                ordem.setDesconto(0.0);
                ordem.setValorFinal(0.0);
            } catch (SQLException | RuntimeException e) { con.rollback(); throw e; }
        } catch (SQLException e) {
            if (e instanceof org.postgresql.util.PSQLException pg
                    && "23505".equals(pg.getSQLState())
                    && pg.getServerErrorMessage() != null
                    && "uq_bicicleta_agendamento".equals(pg.getServerErrorMessage().getConstraint())) {
                throw new IllegalArgumentException(
                        "Esta bicicleta já possui uma ordem agendada para essa data. Escolha outra data.", e);
            }
            throw new RuntimeException("Não foi possível agendar. Confira os serviços e a disponibilidade da data.", e);
        }
    }

    public void cancela(Long id) {
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "UPDATE ordem_servico SET status='CANCELADA' WHERE id=? AND status='AGENDADA'")) {
            ps.setLong(1, id);
            if (ps.executeUpdate() != 1) throw new IllegalArgumentException("A ordem não existe ou não está agendada.");
        } catch (SQLException e) { throw new RuntimeException("Não foi possível cancelar a ordem.", e); }
    }

    public void atualiza(OrdemServico ordem) {
        if (ordem == null || ordem.getId() == null)
            throw new IllegalArgumentException("Informe a ordem de serviço.");
        if (ordem.getItens() == null || ordem.getItens().isEmpty())
            throw new IllegalArgumentException("A ordem não possui serviços.");

        try (Connection con = new ConnectionFactory().getConnection()) {
            con.setAutoCommit(false);

            try {
                ItemOrdemServicoDAO itemDAO = new ItemOrdemServicoDAO();

                for (ItemOrdemServico item : ordem.getItens()) {
                    itemDAO.atualiza(con, item);
                }

                String sql = "UPDATE ordem_servico SET data_execucao=?, status=?, "
                        + "subtotal=?, desconto=?, valor_final=? "
                        + "WHERE id=? AND status='AGENDADA'";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setObject(1, ordem.getDataExecucao());
                    ps.setString(2, ordem.getStatus().name());
                    ps.setDouble(3, ordem.getSubtotal());
                    ps.setDouble(4, ordem.getDesconto());
                    ps.setDouble(5, ordem.getValorFinal());
                    ps.setLong(6, ordem.getId());

                    if (ps.executeUpdate() != 1)
                        throw new IllegalArgumentException(
                                "A ordem não existe ou não está agendada.");
                }

                con.commit();
            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível atualizar a ordem.", e);
        }
    }

    public void atualizaPagamento(OrdemServico ordem) {
	    	String url = "UPDATE ordem_servico " +
	    	        "SET status=?, desconto=?, valor_final=? " +
	    	        "WHERE id=? AND status='EXECUTADA'";
	    	
			try (Connection con = new ConnectionFactory().getConnection();
				 PreparedStatement ps = con.prepareStatement(url)) {

				ps.setString(1, ordem.getStatus().name());
				ps.setDouble(2, ordem.getDesconto());
				ps.setDouble(3, ordem.getValorFinal());
				ps.setLong(4, ordem.getId());

				if (ps.executeUpdate() != 1) {
					throw new IllegalArgumentException(
							"A ordem não existe ou não está executada.");
				}

			} catch (SQLException e) {
		        throw new RuntimeException(
		                "Não foi possível registrar o pagamento.",
	                e
	            );
	        }
	}
    
    public OrdemServico buscaPorId(Long id) {
        List<OrdemServico> ordens = consulta("WHERE id=?", id);
        return ordens.isEmpty() ? null : ordens.get(0);
    }

    public List<OrdemServico> lista() { return consulta(""); }

    public List<OrdemServico> listaRelatorio(LocalDate dataInicial, LocalDate dataFinal) {
        if (dataInicial == null || dataFinal == null) {
            throw new IllegalArgumentException("Informe o período do relatório.");
        }
        if (dataFinal.isBefore(dataInicial)) {
            throw new IllegalArgumentException("A data final não pode ser anterior à data inicial.");
        }

        return consulta(
                "WHERE status IN ('EXECUTADA', 'PAGA') AND data_execucao>=? AND data_execucao<=?",
                dataInicial, dataFinal);
    }

    public List<OrdemServico> listaFiltrada(StatusOrdem status, LocalDate dataInicial, LocalDate dataFinal) {
        if (dataInicial != null && dataFinal != null && dataFinal.isBefore(dataInicial)) {
            throw new IllegalArgumentException("A data final não pode ser anterior à data inicial.");
        }
        StringBuilder filtro = new StringBuilder("WHERE 1=1");
        List<Object> parametros = new ArrayList<>();
        if (status != null) {
            filtro.append(" AND status=?");
            parametros.add(status.name());
        }
        if (dataInicial != null) {
            filtro.append(" AND data_agendamento>=?");
            parametros.add(dataInicial);
        }
        if (dataFinal != null) {
            filtro.append(" AND data_agendamento<=?");
            parametros.add(dataFinal);
        }
        return consulta(filtro.toString(), parametros.toArray());
    }

    public List<OrdemServico> listaAgendadas(LocalDate aPartirDe) {
        if (aPartirDe == null) throw new IllegalArgumentException("Informe a data inicial.");
        return consulta("WHERE status='AGENDADA' AND data_agendamento>=?", aPartirDe);
    }

    public List<OrdemServico> historicoPorCliente(Long clienteId) {
        return consulta("WHERE status IN ('EXECUTADA','PAGA') AND cliente_id=?", clienteId);
    }

    public List<OrdemServico> historicoPorBicicleta(Long bicicletaId) {
        return consulta("WHERE status IN ('EXECUTADA','PAGA') AND bicicleta_id=?", bicicletaId);
    }

    private List<OrdemServico> consulta(String filtro, Object... parametros) {
        List<OrdemServico> ordens = new ArrayList<>();
        // filtro é definido apenas pelos métodos acima, nunca pela entrada do usuário.
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM ordem_servico " + filtro + " ORDER BY data_agendamento, id")) {
            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrdemServico ordem = new OrdemServico();
                    ordem.setId(rs.getLong("id"));
                    ordem.setCliente(new ClienteDAO().buscaPorId(rs.getLong("cliente_id")));
                    ordem.setBicicleta(new BicicletaDAO().buscaPorId(rs.getLong("bicicleta_id")));
                    ordem.setDataAgendamento(rs.getObject("data_agendamento", LocalDate.class));
                    ordem.setDataExecucao(rs.getObject("data_execucao", LocalDate.class));
                    ordem.setStatus(StatusOrdem.valueOf(rs.getString("status")));
                    ordem.setSubtotal(rs.getDouble("subtotal"));
                    ordem.setDesconto(rs.getDouble("desconto"));
                    ordem.setValorFinal(rs.getDouble("valor_final"));
                    ordem.setItens(new ItemOrdemServicoDAO().listaPorOrdem(con, ordem.getId()));
                    for (ItemOrdemServico item : ordem.getItens()) item.setOrdem(ordem);
                    ordens.add(ordem);
                }
            }
            return ordens;
        } catch (SQLException e) { throw new RuntimeException("Não foi possível consultar as ordens.", e); }
    }
}
