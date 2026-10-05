package ti.mvc.logica;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.ItemOrdemServico;
import ti.mvc.modelo.OrdemServico;
import ti.mvc.modelo.StatusOrdem;
import ti.mvc.modelo.dao.OrdemServicoDAO;

public class AlterarStatusOrdem implements Logica {

	@Override
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String ordemIdTexto = texto(req.getParameter("ordemId"));
			String acao = texto(req.getParameter("acao"));

			if (ordemIdTexto.isEmpty()) {
				throw new IllegalArgumentException("Informe a ordem de serviço.");
			}
			if (!"cancelar".equals(acao)
					&& !"executar".equals(acao)
					&& !"pagar".equals(acao)) {
				throw new IllegalArgumentException("Selecione uma ação válida.");
			}

			Long id = Long.valueOf(ordemIdTexto);
			OrdemServicoDAO dao = new OrdemServicoDAO();
			OrdemServico ordem = dao.buscaPorId(id);

			if (ordem == null) {
				throw new IllegalArgumentException("Ordem de serviço não encontrada.");
			}

			if ("cancelar".equals(acao)) {
				if (ordem.getStatus() != StatusOrdem.AGENDADA) {
					throw new IllegalArgumentException("Somente uma ordem agendada pode ser cancelada.");
				}

				dao.cancela(id);
				resp.sendRedirect(req.getContextPath() + "/mvc?logica=ListagemServicos");
				return;
			}

			if ("executar".equals(acao)) {
				if (ordem.getStatus() != StatusOrdem.AGENDADA) {
					throw new IllegalArgumentException("Somente uma ordem agendada pode ser executada.");
				}

				req.setAttribute("ordem", ordem);
				req.setAttribute("dataHoje", LocalDate.now());
				req.getRequestDispatcher("/WEB-INF/views/executar-ordem.jsp").forward(req, resp);
				return;
			}

			if (ordem.getStatus() != StatusOrdem.EXECUTADA) {
				throw new IllegalArgumentException("Somente uma ordem executada pode ser paga.");
			}

			int quantidadeRealizados = 0;
			for (ItemOrdemServico item : ordem.getItens()) {
				if (item.isRealizado()) {
					quantidadeRealizados++;
				}
			}

			req.setAttribute("ordem", ordem);
			req.setAttribute("podeAplicarDesconto", quantidadeRealizados >= 3);
			req.getRequestDispatcher("/WEB-INF/views/pagar-ordem.jsp").forward(req, resp);
		} catch (NumberFormatException e) {
			mostrarErro(req, resp, "Informe uma ordem de serviço válida.");
		} catch (IllegalArgumentException e) {
			mostrarErro(req, resp, e.getMessage());
		} catch (RuntimeException e) {
			mostrarErro(req, resp, "Não foi possível realizar a ação na ordem de serviço.");
		}
	}

	private String texto(String valor) {
		return valor == null ? "" : valor.trim();
	}

	private void mostrarErro(HttpServletRequest req, HttpServletResponse resp, String mensagem)
			throws ServletException, IOException {
		req.setAttribute("erro", mensagem);
		new ListagemServicos().executa(req, resp);
	}
}
