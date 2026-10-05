package ti.mvc.logica;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.OrdemServico;
import ti.mvc.modelo.dao.OrdemServicoDAO;

public class Relatorio implements Logica {

	@Override
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String dataInicialTexto = req.getParameter("dataInicial");
		String dataFinalTexto = req.getParameter("dataFinal");

		if (dataInicialTexto != null && dataFinalTexto != null) {
			try {
				LocalDate dataInicio = LocalDate.parse(dataInicialTexto);
				LocalDate dataFinal = LocalDate.parse(dataFinalTexto);
				
				OrdemServicoDAO dao = new OrdemServicoDAO();

				List<OrdemServico> ordens = dao.listaRelatorio(dataInicio, dataFinal);
				
				Double totalFaturado = 0.0;
				for (OrdemServico ordem : ordens) {
					totalFaturado = totalFaturado + ordem.getValorFinal();
				}

				req.setAttribute("ordens", ordens);
				req.setAttribute("totalFaturado", totalFaturado);
				req.setAttribute("pesquisou", true);
			} catch (DateTimeParseException e) {
				req.setAttribute("erro", "Informe datas válidas.");
			} catch (IllegalArgumentException e) {
				req.setAttribute("erro", e.getMessage());
			}
		}
		
		req.getRequestDispatcher("/WEB-INF/views/relatorio.jsp").forward(req, resp);
	}

}
