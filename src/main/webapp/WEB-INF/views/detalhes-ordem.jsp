<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/includes/bootstrap-head.jspf" %>
    <title>Detalhes da OS - Q-Pedala</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/includes/navbar.jsp" />
    <main class="container py-4">
        <h1>Detalhes da Ordem de Serviço nº <c:out value="${ordem.id}" /></h1>

        <p><strong>Cliente registrado na OS:</strong>
            <c:out value="${ordem.cliente.nome}" />
        </p>

        <p><strong>Bicicleta:</strong>
            <c:out value="${ordem.bicicleta.marca}" />
            <c:out value="${ordem.bicicleta.modelo}" />
            — Série: <c:out value="${ordem.bicicleta.numeroSerie}" />
        </p>

        <p><strong>Data do agendamento:</strong>
            <fmt:formatDate value="${ordem.dataAgendamentoParaExibicao}" pattern="dd/MM/yyyy" />
        </p>

        <c:if test="${not empty ordem.dataExecucao}">
            <p><strong>Data da execução:</strong>
                <fmt:formatDate value="${ordem.dataExecucaoParaExibicao}" pattern="dd/MM/yyyy" />
            </p>
        </c:if>

        <p><strong>Status:</strong>
            <c:out value="${ordem.status.descricao}" />
        </p>

        <h2>Serviços da ordem</h2>

        <div class="table-responsive"><table class="table table-striped table-hover align-middle">
            <thead>
                <tr>
                    <th>Serviço</th>
                    <th>Situação</th>
                    <th>Valor realizado</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${ordem.itens}">
                    <tr>
                        <td><c:out value="${item.servico.nome}" /></td>
                        <td>${item.realizado ? 'Realizado' : 'Não realizado'}</td>
                        <td>
                            <c:choose>
                                <c:when test="${item.realizado}">
                                    <fmt:formatNumber value="${item.valorExecutado}"
                                        type="currency" currencySymbol="R$" />
                                </c:when>
                                <c:otherwise>—</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table></div>

        <p><strong>Subtotal:</strong>
            <fmt:formatNumber value="${ordem.subtotal}"
                type="currency" currencySymbol="R$" />
        </p>

        <p><strong>Desconto:</strong>
            <fmt:formatNumber value="${ordem.desconto}"
                type="currency" currencySymbol="R$" />
        </p>

        <p><strong>Valor final:</strong>
            <fmt:formatNumber value="${ordem.valorFinal}"
                type="currency" currencySymbol="R$" />
        </p>

        <form class="qp-plain-form" action="${pageContext.request.contextPath}/mvc" method="post">
            <input type="hidden" name="logica" value="${origem}">

            <c:if test="${origem == 'Historico'}">
                <input type="hidden" name="tipo" value="${param.tipo}">
                <input type="hidden" name="id" value="${param.id}">
            </c:if>

            <c:if test="${origem == 'Relatorio'}">
                <input type="hidden" name="dataInicial" value="${param.dataInicial}">
                <input type="hidden" name="dataFinal" value="${param.dataFinal}">
            </c:if>

            <c:if test="${origem == 'ListagemServicos'}">
                <input type="hidden" name="status" value="${param.status}">
                <input type="hidden" name="dataInicial" value="${param.dataInicial}">
                <input type="hidden" name="dataFinal" value="${param.dataFinal}">
            </c:if>

            <button class="btn btn-outline-secondary" type="submit">Voltar</button>
        </form>
    </main>
</body>
</html>
