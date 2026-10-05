package ti.mvc.logica;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.Cliente;
import ti.mvc.modelo.dao.ClienteDAO;

public class NovaBicicleta implements Logica {

	@Override
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ClienteDAO dao = new ClienteDAO();
        List<Cliente> clientes = dao.lista();

        req.setAttribute("clientes", clientes);
		 req.getRequestDispatcher("/WEB-INF/views/cadastro-bicicleta.jsp").forward(req, resp);
  }
}


