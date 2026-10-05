package ti.mvc.logica;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.Servico;
import ti.mvc.modelo.dao.ServicoDAO;

public class AtualizaServico implements Logica {

	@Override
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String servicoIdTexto = texto(req.getParameter("servicoId"));
			String novoValorTexto = texto(req.getParameter("novoValor"));

			if (servicoIdTexto.isEmpty()) {
				throw new IllegalArgumentException("Selecione um serviço.");
			}
			if (novoValorTexto.isEmpty()) {
				throw new IllegalArgumentException("Informe o novo valor.");
			}

			Long servicoId = Long.valueOf(servicoIdTexto);
			Double novoValor = Double.valueOf(novoValorTexto);
			if (!Double.isFinite(novoValor) || novoValor < 0) {
				throw new IllegalArgumentException("Informe um valor válido e não negativo.");
			}

			ServicoDAO dao = new ServicoDAO();
			Servico servico = dao.buscaPorId(servicoId);
			if (servico == null) {
				throw new IllegalArgumentException("Serviço não encontrado.");
			}

			servico.setValor(novoValor);
			dao.atualiza(servico);

			req.setAttribute("sucesso", "Valor do serviço atualizado com sucesso.");
			new AlteraValorServico().executa(req, resp);
		} catch (NumberFormatException e) {
			req.setAttribute("erro", "Informe um serviço e um valor válidos.");
			new AlteraValorServico().executa(req, resp);
		} catch (IllegalArgumentException e) {
			req.setAttribute("erro", e.getMessage());
			new AlteraValorServico().executa(req, resp);
		} catch (RuntimeException e) {
			req.setAttribute("erro", "Não foi possível atualizar o serviço.");
			new AlteraValorServico().executa(req, resp);
		}
	}

	private String texto(String valor) {
		return valor == null ? "" : valor.trim();
	}

}
