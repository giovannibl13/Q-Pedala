package ti.mvc.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ti.mvc.logica.Logica;

@WebServlet("/mvc")
public class ControleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	@Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException {
        String nomeLogica = req.getParameter("logica");
        
        if (nomeLogica == null || nomeLogica.isBlank()) {
            nomeLogica = "Inicio";
        }

        // Somente o login pode ser executado sem autenticação.
        if (!"Login".equals(nomeLogica)) {
            HttpSession sessao = req.getSession(false);
            if (sessao == null || sessao.getAttribute("administradorId") == null) {
                req.setAttribute("erro", "Faça login para continuar.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }
        }

        String nomeClasse = "ti.mvc.logica." + nomeLogica;
        try {
            Class<? extends Logica> classe = Class.forName(nomeClasse).asSubclass(Logica.class);
            Logica logica = classe.getConstructor().newInstance();
            logica.executa(req, resp);
        } catch (ReflectiveOperationException | ClassCastException e) {
            encaminharErro(req, resp,
                    "A funcionalidade solicitada não foi encontrada.", e);
        } catch (RuntimeException e) {
            encaminharErro(req, resp,
                    "Não foi possível concluir a operação. Tente novamente.", e);
        }
    }

    private void encaminharErro(HttpServletRequest req, HttpServletResponse resp,
            String mensagem, Exception erro) throws ServletException, IOException {

        getServletContext().log("Erro ao processar a requisição em /mvc.", erro);

        if (resp.isCommitted()) {
            throw new ServletException(mensagem, erro);
        }

        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        req.setAttribute("mensagemErro", mensagem);
        req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
    }
}
