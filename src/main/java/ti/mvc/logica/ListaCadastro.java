package ti.mvc.logica;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.dao.BicicletaDAO;
import ti.mvc.modelo.dao.ClienteDAO;
import ti.mvc.modelo.dao.ServicoDAO;

public class ListaCadastro implements Logica {

    @Override
    public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ClienteDAO clienteDAO = new ClienteDAO();
        BicicletaDAO bicicletaDAO = new BicicletaDAO();
        ServicoDAO servicoDAO = new ServicoDAO();

        req.setAttribute("clientes", clienteDAO.lista());
        req.setAttribute("bicicletas", bicicletaDAO.lista());
        req.setAttribute("servicos", servicoDAO.lista());

        req.getRequestDispatcher("/WEB-INF/views/lista-cadastro.jsp").forward(req, resp);
    }
}