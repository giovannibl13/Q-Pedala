package ti.mvc.logica;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.dao.BicicletaDAO;
import ti.mvc.modelo.dao.ClienteDAO;
import ti.mvc.modelo.dao.OrdemServicoDAO;

public class Historico implements Logica {

	@Override
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ClienteDAO clienteDAO = new ClienteDAO();
		BicicletaDAO bicicletaDAO = new BicicletaDAO();
		OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
		
		req.setAttribute("clientes", clienteDAO.lista());
		req.setAttribute("bicicletas", bicicletaDAO.lista());
		
		String tipo = req.getParameter("tipo");

        // Ao entrar pelo menu, ainda não há uma pesquisa para executar.
        if (tipo != null) {
            try {
                Long id = Long.valueOf(req.getParameter("id"));

                if ("cliente".equals(tipo)) {
                    req.setAttribute("ordens", ordemServicoDAO.historicoPorCliente(id));

                } else if ("bicicleta".equals(tipo)) {
                    req.setAttribute("ordens", ordemServicoDAO.historicoPorBicicleta(id));

                } else {
                    throw new IllegalArgumentException("Tipo de consulta inválido.");
                }

                req.setAttribute("tipoSelecionado", tipo);
                req.setAttribute("idSelecionado", id);
                req.setAttribute("pesquisou", true);

            } catch (NumberFormatException e) {
                req.setAttribute("erro", "Selecione um cliente ou uma bicicleta válida.");
            } catch (IllegalArgumentException e) {
                req.setAttribute("erro", e.getMessage());
            }
        }
		
		req.getRequestDispatcher("/WEB-INF/views/historico.jsp").forward(req, resp);
	}

}
