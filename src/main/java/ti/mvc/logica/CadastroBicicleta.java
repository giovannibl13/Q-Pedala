package ti.mvc.logica;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.Bicicleta;
import ti.mvc.modelo.Cliente;
import ti.mvc.modelo.TipoBicicleta;
import ti.mvc.modelo.dao.BicicletaDAO;
import ti.mvc.modelo.dao.ClienteDAO;

public class CadastroBicicleta implements Logica {

	@Override
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String marca = texto(req, "marca");
			String modelo = texto(req, "modelo");
			String tipo = texto(req, "tipo");
			String tamanhoQuadro = texto(req, "tamanhoQuadro");
			String numeroSerie = texto(req, "numeroSerie");
			String anoFabricacaoTexto = texto(req, "anoFabricacao");
			String proprietarioId = texto(req, "proprietarioId");

			if (marca.isEmpty() || modelo.isEmpty() || tamanhoQuadro.isEmpty() || numeroSerie.isEmpty()) {
				throw new IllegalArgumentException("Preencha os dados da bicicleta.");
			}

			TipoBicicleta tipoBicicleta;
			try {
				tipoBicicleta = TipoBicicleta.valueOf(tipo);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Selecione um tipo de bicicleta válido.");
			}

			int anoFabricacao = Integer.parseInt(anoFabricacaoTexto);
			if (anoFabricacao < 1800 || anoFabricacao > LocalDate.now().getYear()) {
				throw new IllegalArgumentException("Informe um ano de fabricação válido.");
			}

			Cliente proprietario = null;
			if (!proprietarioId.isEmpty()) {
				proprietario = new ClienteDAO().buscaPorId(Long.valueOf(proprietarioId));
				if (proprietario == null) {
					throw new IllegalArgumentException("Proprietário não encontrado.");
				}
			}

			Bicicleta bicicleta = new Bicicleta(marca, modelo, tipoBicicleta,
					tamanhoQuadro, numeroSerie, anoFabricacao, proprietario);
			new BicicletaDAO().adiciona(bicicleta);

			resp.sendRedirect(req.getContextPath() + "/mvc?logica=Inicio");
		} catch (NumberFormatException e) {
			req.setAttribute("erro", "Informe valores válidos para o ano e o proprietário.");
			new NovaBicicleta().executa(req, resp);
		} catch (IllegalArgumentException e) {
			req.setAttribute("erro", e.getMessage());
			new NovaBicicleta().executa(req, resp);
		}
	}

	private String texto(HttpServletRequest req, String nome) {
		String valor = req.getParameter(nome);
		return valor == null ? "" : valor.trim();
	}

}
