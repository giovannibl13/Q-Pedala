package ti.mvc.logica;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ti.mvc.modelo.ItemOrdemServico;
import ti.mvc.modelo.OrdemServico;
import ti.mvc.modelo.StatusOrdem;
import ti.mvc.modelo.dao.OrdemServicoDAO;

public class RegistrarExecucao implements Logica {

    @Override
    public void executa(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        String ordemIdTexto =
            req.getParameter("ordemId");

        String dataExecucaoTexto =
            req.getParameter("dataExecucao");

        String[] itensSelecionados =
            req.getParameterValues("itemId");

        Long ordemId = null;

        try {
            ordemId = Long.valueOf(ordemIdTexto);

            if (dataExecucaoTexto == null || dataExecucaoTexto.isBlank()) {
                throw new IllegalArgumentException("Informe a data de execução.");
            }

            LocalDate dataExecucao;
            try {
                dataExecucao = LocalDate.parse(dataExecucaoTexto);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Informe uma data de execução válida.");
            }

            OrdemServicoDAO dao =
                new OrdemServicoDAO();

            OrdemServico ordem =
                dao.buscaPorId(ordemId);

            if (ordem == null) {
                throw new IllegalArgumentException(
                    "Ordem não encontrada."
                );
            }

            if (ordem.getStatus()
                    != StatusOrdem.AGENDADA) {
                throw new IllegalArgumentException(
                    "Somente uma ordem agendada pode ser executada."
                );
            }

            if (dataExecucao.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException(
                    "A data de execução não pode estar no futuro."
                );
            }

            if (dataExecucao.isBefore(ordem.getDataAgendamento())) {
                throw new IllegalArgumentException(
                    "A data de execução não pode ser anterior à data do agendamento."
                );
            }

            if (itensSelecionados == null
                    || itensSelecionados.length == 0) {
                throw new IllegalArgumentException(
                    "Selecione pelo menos um serviço realizado."
                );
            }

            double subtotal = 0.0;
            int quantidadeEncontrada = 0;

            /*
             * Percorre os itens que realmente pertencem
             * à ordem de serviço.
             */
            for (ItemOrdemServico item
                    : ordem.getItens()) {

                boolean realizado = false;

                for (String itemId
                        : itensSelecionados) {

                    Long idSelecionado =
                        Long.valueOf(itemId);

                    if (item.getId().equals(
                            idSelecionado)) {

                        realizado = true;
                        quantidadeEncontrada++;
                        break;
                    }
                }

                item.setRealizado(realizado);

                if (realizado) {
                    /*
                     * Copia o preço atual do serviço para
                     * o histórico do item.
                     */
                    Double valor =
                        item.getServico().getValor();

                    item.setValorExecutado(valor);

                    subtotal = subtotal + valor;

                } else {
                    item.setValorExecutado(null);
                }
            }

            /*
             * Confere se todos os IDs enviados pelo
             * formulário pertencem à ordem.
             */
            if (quantidadeEncontrada
                    != itensSelecionados.length) {
                throw new IllegalArgumentException(
                    "Foi selecionado um serviço inválido."
                );
            }

            ordem.setDataExecucao(dataExecucao);
            ordem.setStatus(StatusOrdem.EXECUTADA);
            ordem.setSubtotal(subtotal);
            ordem.setDesconto(0.0);
            ordem.setValorFinal(subtotal);

            dao.atualiza(ordem);

            resp.sendRedirect(
                req.getContextPath()
                + "/mvc?logica=ListagemServicos"
            );

        } catch (RuntimeException e) {
            String mensagem = e.getMessage();

            if (mensagem == null
                    || mensagem.isBlank()) {
                mensagem =
                    "Não foi possível registrar a execução.";
            }

            req.setAttribute("erro", mensagem);

            if (ordemId != null) {
                OrdemServicoDAO dao =
                    new OrdemServicoDAO();

                OrdemServico ordem =
                    dao.buscaPorId(ordemId);

                req.setAttribute("ordem", ordem);
                req.setAttribute("dataHoje", LocalDate.now());

                req.getRequestDispatcher(
                    "/WEB-INF/views/executar-ordem.jsp"
                ).forward(req, resp);

            } else {
                new ListagemServicos()
                    .executa(req, resp);
            }
        }
    }
}
