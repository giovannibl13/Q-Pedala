package ti.mvc.modelo.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ti.mvc.connection.ConnectionFactory;
import ti.mvc.modelo.*;

public class ItemOrdemServicoDAO {
    // Usado pela ordem dentro da mesma transação. Itens não têm exclusão isolada.
    void adiciona(Connection con, Long ordemId, Long servicoId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO item_ordem_servico (ordem_id, servico_id) VALUES (?, ?)")) {
            ps.setLong(1, ordemId);
            ps.setLong(2, servicoId);
            ps.executeUpdate();
        }
    }

    void atualiza(Connection con, ItemOrdemServico item) throws SQLException {
        String sql = "UPDATE item_ordem_servico "
                + "SET realizado=?, valor_executado=? "
                + "WHERE id=? AND ordem_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, item.isRealizado());

            if (item.getValorExecutado() != null) {
                ps.setDouble(2, item.getValorExecutado());
            } else {
                ps.setNull(2, Types.NUMERIC);
            }

            ps.setLong(3, item.getId());
            ps.setLong(4, item.getOrdem().getId());

            if (ps.executeUpdate() != 1) {
                throw new IllegalArgumentException("Item da ordem não encontrado.");
            }
        }
    }

    public List<ItemOrdemServico> listaPorOrdem(Long ordemId) {
        try (Connection con = new ConnectionFactory().getConnection()) {
            return listaPorOrdem(con, ordemId);
        } catch (SQLException e) { throw new RuntimeException("Não foi possível consultar os itens.", e); }
    }

    List<ItemOrdemServico> listaPorOrdem(Connection con, Long ordemId) throws SQLException {
        String sql = "SELECT i.*, s.nome, s.valor FROM item_ordem_servico i "
            + "JOIN servico s ON s.id=i.servico_id WHERE i.ordem_id=? ORDER BY i.id";
        List<ItemOrdemServico> itens = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, ordemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Servico servico = new Servico();
                    servico.setId(rs.getLong("servico_id"));
                    servico.setNome(rs.getString("nome"));
                    servico.setValor(rs.getDouble("valor"));
                    OrdemServico ordem = new OrdemServico();
                    ordem.setId(ordemId);
                    ItemOrdemServico item = new ItemOrdemServico();
                    item.setId(rs.getLong("id"));
                    item.setOrdem(ordem);
                    item.setServico(servico);
                    item.setRealizado(rs.getBoolean("realizado"));
                    double valorExecutado = rs.getDouble("valor_executado");
                    item.setValorExecutado(rs.wasNull() ? null : valorExecutado);
                    itens.add(item);
                }
            }
        }
        return itens;
    }
}
