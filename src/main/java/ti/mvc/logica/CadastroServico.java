package ti.mvc.logica;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.Servico;
import ti.mvc.modelo.dao.ServicoDAO;

public class CadastroServico implements Logica {

	@Override
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String nome = texto(req.getParameter("nome"));
			String precoTexto = texto(req.getParameter("preco"));

			if (nome.isEmpty()) {
				throw new IllegalArgumentException("Informe o nome do serviço.");
			}

			if (precoTexto.isEmpty()) {
				throw new IllegalArgumentException("Informe o preço do serviço.");
			}

			Double preco = Double.valueOf(precoTexto);
			if (!Double.isFinite(preco) || preco < 0) {
				throw new IllegalArgumentException("Informe um preço válido e não negativo.");
			}

			Servico servico = new Servico(preco, nome);
			new ServicoDAO().adiciona(servico);

			resp.sendRedirect(req.getContextPath() + "/mvc?logica=Inicio");
		} catch (NumberFormatException e) {
			req.setAttribute("erro", "Informe um preço válido.");
			voltarAoFormulario(req, resp);
		} catch (IllegalArgumentException e) {
			req.setAttribute("erro", e.getMessage());
			voltarAoFormulario(req, resp);
		} catch (RuntimeException e) {
			req.setAttribute("erro", "Não foi possível cadastrar o serviço.");
			voltarAoFormulario(req, resp);
		}
	}

	private String texto(String valor) {
		return valor == null ? "" : valor.trim();
	}

	private void voltarAoFormulario(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/views/cadastro-servico.jsp").forward(req, resp);
	}

}
