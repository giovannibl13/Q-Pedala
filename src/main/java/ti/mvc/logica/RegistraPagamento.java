package ti.mvc.logica;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.ItemOrdemServico;
import ti.mvc.modelo.OrdemServico;
import ti.mvc.modelo.StatusOrdem;
import ti.mvc.modelo.dao.OrdemServicoDAO;

public class RegistraPagamento implements Logica{

	@Override
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long ordemId = Long.valueOf(req.getParameter("ordemId"));	
		String deconto = req.getParameter("aplicarDesconto");
		
		try {
			OrdemServicoDAO dao = new OrdemServicoDAO();
	        OrdemServico ordem = dao.buscaPorId(ordemId);
			
	        if (ordem.getStatus() != StatusOrdem.EXECUTADA) {
	            throw new IllegalArgumentException(
	                "Somente uma ordem executada pode ser paga."
	            );
	        }
	        
	        int quantidadeRealizados = 0;
	        
	        for(ItemOrdemServico item : ordem.getItens()) {
	        	 if(item.isRealizado()) {
	                quantidadeRealizados++;
	            }
	        }
	        
	        boolean aplicarDesconto = deconto != null;
	        
	        if (aplicarDesconto && quantidadeRealizados < 3) {

	            throw new IllegalArgumentException(
	                "O desconto exige pelo menos três serviços realizados."
	            );
	        }
	        
	        double desconto = 0.0;
	        
	        if (aplicarDesconto) {
	            desconto = ordem.getSubtotal() * 10 / 100;
	        }
	        
	        double valorFinal = ordem.getSubtotal() - desconto;

	        ordem.setDesconto(desconto);
	        ordem.setValorFinal(valorFinal);
	        ordem.setStatus(StatusOrdem.PAGA);

	        dao.atualizaPagamento(ordem);
	        
	        resp.sendRedirect(req.getContextPath() + "/mvc?logica=ListagemServicos");
		} catch (RuntimeException e) {
            String mensagem = e.getMessage();

            if (mensagem == null || mensagem.isBlank()) {

                mensagem = "Não foi possível registrar o pagamento.";
            }

            req.setAttribute("erro", mensagem);

            if (ordemId != null) {
                OrdemServicoDAO dao = new OrdemServicoDAO();

                OrdemServico ordem = dao.buscaPorId(ordemId);

                int quantidadeRealizados = 0;

                if (ordem != null) {
                    for (ItemOrdemServico item : ordem.getItens()) {

                        if (item.isRealizado()) {
                        	quantidadeRealizados++;
                        }
                    }
                }

                req.setAttribute("ordem", ordem);

                req.setAttribute("podeAplicarDesconto", quantidadeRealizados >= 3);

                req.getRequestDispatcher("/WEB-INF/views/pagar-ordem.jsp").forward(req, resp);

            } else {
                new ListagemServicos().executa(req, resp);
            }
        }
	}
}
