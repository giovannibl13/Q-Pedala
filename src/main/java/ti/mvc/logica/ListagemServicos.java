package ti.mvc.logica;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.dao.OrdemServicoDAO;
import ti.mvc.modelo.OrdemServico;
import ti.mvc.modelo.StatusOrdem;

public class ListagemServicos implements Logica {

	@Override
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String statusTexto = parametro(req, "status");
        String dataInicialTexto = parametro(req, "dataInicial");
        String dataFinalTexto = parametro(req, "dataFinal");
        req.setAttribute("statusFiltro", statusTexto);
        req.setAttribute("dataInicialFiltro", dataInicialTexto);
        req.setAttribute("dataFinalFiltro", dataFinalTexto);
        req.setAttribute("statusDisponiveis",
                List.of(StatusOrdem.AGENDADA, StatusOrdem.EXECUTADA));
        req.setAttribute("servicos", List.of());

        try {
            StatusOrdem status = null;
            if (!statusTexto.isEmpty()) {
                try {
                    status = StatusOrdem.valueOf(statusTexto);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Selecione um status válido.");
                }

                if (status != StatusOrdem.AGENDADA
                        && status != StatusOrdem.EXECUTADA) {
                    throw new IllegalArgumentException(
                            "A listagem apresenta somente ordens agendadas ou executadas.");
                }
            }
            LocalDate dataInicial = dataInicialTexto.isEmpty() ? null : LocalDate.parse(dataInicialTexto);
            LocalDate dataFinal = dataFinalTexto.isEmpty() ? null : LocalDate.parse(dataFinalTexto);

            OrdemServicoDAO dao = new OrdemServicoDAO();
            if (status != null) {
                req.setAttribute("servicos",
                        dao.listaFiltrada(status, dataInicial, dataFinal));
            } else {
                List<OrdemServico> ordens = new ArrayList<>();
                ordens.addAll(dao.listaFiltrada(
                        StatusOrdem.AGENDADA, dataInicial, dataFinal));
                ordens.addAll(dao.listaFiltrada(
                        StatusOrdem.EXECUTADA, dataInicial, dataFinal));
                req.setAttribute("servicos", ordens);
            }
        } catch (DateTimeParseException e) {
            req.setAttribute("erro", "Informe datas válidas no formato ano-mês-dia.");
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
        }
        
        req.getRequestDispatcher("/WEB-INF/views/lista-servicos.jsp").forward(req, resp);

	}

    private String parametro(HttpServletRequest req, String nome) {
        String valor = req.getParameter(nome);
        return valor == null ? "" : valor.trim();
    }
}
