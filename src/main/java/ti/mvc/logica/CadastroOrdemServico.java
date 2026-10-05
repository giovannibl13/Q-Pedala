package ti.mvc.logica;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.Bicicleta;
import ti.mvc.modelo.Cliente;
import ti.mvc.modelo.ItemOrdemServico;
import ti.mvc.modelo.OrdemServico;
import ti.mvc.modelo.Servico;
import ti.mvc.modelo.dao.OrdemServicoDAO;

public class CadastroOrdemServico implements Logica {

	@Override
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String clienteIdTexto = req.getParameter("clienteId");
			String bicicletaIdTexto = req.getParameter("bicicletaId");
			String dataAgendamentoTexto = req.getParameter("dataAgendamento");

			if (clienteIdTexto == null || clienteIdTexto.isBlank()
					|| bicicletaIdTexto == null || bicicletaIdTexto.isBlank()) {
				throw new IllegalArgumentException("Selecione o cliente e a bicicleta.");
			}
			if (dataAgendamentoTexto == null || dataAgendamentoTexto.isBlank()) {
				throw new IllegalArgumentException("Informe a data do agendamento.");
			}

			Long clienteId = Long.valueOf(clienteIdTexto);
			Long bicicletaId = Long.valueOf(bicicletaIdTexto);
			LocalDate dataAgendamento = LocalDate.parse(dataAgendamentoTexto);
			String[] servicosId = req.getParameterValues("servicoId");

			if (dataAgendamento.isBefore(LocalDate.now())) {
				throw new IllegalArgumentException("A data do agendamento não pode estar no passado.");
			}
			if (servicosId == null || servicosId.length == 0) {
				throw new IllegalArgumentException("Selecione pelo menos um serviço.");
			}

			Cliente cliente = new Cliente();
			cliente.setId(clienteId);

			Bicicleta bicicleta = new Bicicleta();
			bicicleta.setId(bicicletaId);

			OrdemServico ordem = new OrdemServico(cliente, bicicleta, dataAgendamento);

			for (String id : servicosId) {
				Servico servico = new Servico();
				servico.setId(Long.valueOf(id));

				ItemOrdemServico item = new ItemOrdemServico();
				item.setServico(servico);
				item.setOrdem(ordem);
				ordem.getItens().add(item);
			}

			new OrdemServicoDAO().adiciona(ordem);
			resp.sendRedirect(req.getContextPath() + "/mvc?logica=Inicio");
		} catch (DateTimeParseException e) {
			req.setAttribute("erro", "Informe uma data de agendamento válida.");
			new NovaOrdemServico().executa(req, resp);
		} catch (IllegalArgumentException e) {
			req.setAttribute("erro", e.getMessage() == null ? "Informe dados válidos." : e.getMessage());
			new NovaOrdemServico().executa(req, resp);
		}
	}

}
