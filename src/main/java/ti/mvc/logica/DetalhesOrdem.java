package ti.mvc.logica;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.OrdemServico;
import ti.mvc.modelo.dao.OrdemServicoDAO;

public class DetalhesOrdem implements Logica {

    @Override
    public void executa(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            String ordemIdTexto = req.getParameter("ordemId");
            if (ordemIdTexto == null || ordemIdTexto.isBlank()) {
                throw new IllegalArgumentException("Informe a ordem de serviço.");
            }

            Long ordemId = Long.valueOf(ordemIdTexto);
            OrdemServico ordem = new OrdemServicoDAO().buscaPorId(ordemId);

            if (ordem == null) {
                throw new IllegalArgumentException("Ordem de serviço não encontrada.");
            }

            req.setAttribute("ordem", ordem);
            req.setAttribute("origem", origemValida(req.getParameter("origem")));
            req.getRequestDispatcher("/WEB-INF/views/detalhes-ordem.jsp")
                    .forward(req, resp);
        } catch (NumberFormatException e) {
            mostrarErro(req, resp, "Informe uma ordem de serviço válida.");
        } catch (IllegalArgumentException e) {
            mostrarErro(req, resp, e.getMessage());
        }
    }

    private String origemValida(String origem) {
        if ("Historico".equals(origem)
                || "Relatorio".equals(origem)
                || "ListagemServicos".equals(origem)) {
            return origem;
        }
        return "ListagemServicos";
    }

    private void mostrarErro(HttpServletRequest req, HttpServletResponse resp,
            String mensagem) throws ServletException, IOException {
        req.setAttribute("erro", mensagem);
        new ListagemServicos().executa(req, resp);
    }
}
