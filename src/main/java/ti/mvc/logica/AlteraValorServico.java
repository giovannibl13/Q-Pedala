package ti.mvc.logica;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.dao.ServicoDAO;

public class AlteraValorServico implements Logica {

	@Override
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServicoDAO dao = new ServicoDAO();
		req.setAttribute("servicos", dao.lista());
		
		req.getRequestDispatcher("/WEB-INF/views/alterar-valor-servico.jsp").forward(req, resp);
	}

}
