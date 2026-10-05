package ti.mvc.logica;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ti.mvc.modelo.Administrador;
import ti.mvc.modelo.dao.AdministradorDAO;
import ti.mvc.util.SenhaUtil;

public class Login implements Logica {

    @Override
    public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String usuario = req.getParameter("usuario");
        String senha = req.getParameter("senha");

        if (usuario == null || usuario.isBlank() || senha == null || senha.isBlank()) {
            req.setAttribute("erro", "Preencha o usuário e a senha.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }
        
        AdministradorDAO dao = new AdministradorDAO();
        Administrador administrador = dao.buscaPorUsuario(usuario);
        
        if (administrador == null) {
        	req.setAttribute("erro", "Usuário ou senha inválidos.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
		}
        
        boolean senhaCorreta = SenhaUtil.verificar(senha, administrador.getSenha());

        if (!senhaCorreta) {
            req.setAttribute("erro", "Usuário ou senha inválidos.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        HttpSession sessaoAnterior = req.getSession(false);
        if (sessaoAnterior != null) {
            sessaoAnterior.invalidate();
        }

        HttpSession sessao = req.getSession(true);
        sessao.setAttribute("administradorId", administrador.getId());
        sessao.setAttribute("administradorUsuario", administrador.getUsuario());
        sessao.setMaxInactiveInterval(120);

        req.getRequestDispatcher("/WEB-INF/views/inicio.jsp").forward(req, resp);
    }
}
