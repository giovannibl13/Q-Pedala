package ti.mvc.logica;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.dao.BicicletaDAO;
import ti.mvc.modelo.dao.ClienteDAO;
import ti.mvc.modelo.dao.ServicoDAO;

public class NovaOrdemServico implements Logica {
	@Override
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ClienteDAO clienteDAO = new ClienteDAO();
		BicicletaDAO bicicletaDAO = new BicicletaDAO();
		ServicoDAO servicoDAO = new ServicoDAO();
		
		req.setAttribute("clientes", clienteDAO.lista());
		req.setAttribute("servicos", servicoDAO.lista());
		
		String clienteId = req.getParameter("clienteId");
		
		if (clienteId != null && !clienteId.isBlank()) {
			try {
				Long id = Long.valueOf(clienteId);
				req.setAttribute("bicicletas", bicicletaDAO.listaPorCliente(id));
			} catch (NumberFormatException e) {
				req.setAttribute("erro", "Selecione um cliente válido.");
			}
		}
		req.getRequestDispatcher("/WEB-INF/views/nova-ordem-servico.jsp").forward(req, resp);
	}
}
