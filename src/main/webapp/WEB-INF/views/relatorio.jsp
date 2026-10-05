<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/includes/bootstrap-head.jspf" %>
<title>Relatório</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />
<main class="container py-4">
    <h1>Relatório de serviços</h1>

    <c:if test="${not empty erro}">
        <p class="alert alert-danger" role="alert"><c:out value="${erro}" /></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="Relatorio">

        <label class="form-label" for="dataInicial">Data inicial:</label>
        <input class="form-control" type="date" id="dataInicial" name="dataInicial" required>

        <label class="form-label" for="dataFinal">Data final:</label>
        <input class="form-control" type="date" id="dataFinal" name="dataFinal" required>

        <input class="btn btn-primary" type="submit" value="Gerar relatório">
    </form>

    <c:if test="${pesquisou}">
        <h2>Resultado</h2>

        <div class="table-responsive"><table class="table table-striped table-hover align-middle">
            <tr>
                <th>OS</th>
                <th>Data de execução</th>
                <th>Cliente</th>
                <th>Bicicleta</th>
                <th>Status</th>
                <th>Valor final</th>
                <th>Detalhes</th>
            </tr>

            <c:if test="${empty ordens}">
                <tr>
                    <td colspan="7">Nenhuma ordem encontrada.</td>
                </tr>
            </c:if>

            <c:forEach var="ordem" items="${ordens}">
                <tr>
                    <td><c:out value="${ordem.id}" /></td>

                    <td><fmt:formatDate value="${ordem.dataExecucaoParaExibicao}" pattern="dd/MM/yyyy" /></td>

                    <td><c:out value="${ordem.cliente.nome}" /></td>

                    <td>
                        <c:out value="${ordem.bicicleta.marca}" />
                        <c:out value="${ordem.bicicleta.modelo}" />
                        <br>
                        Série: <c:out value="${ordem.bicicleta.numeroSerie}" />
                    </td>

                    <td><c:out value="${ordem.status.descricao}" /></td>

                    <td>
                        <fmt:formatNumber value="${ordem.valorFinal}" type="currency" currencySymbol="R$" />
                    </td>

                    <td>
                        <form action="${pageContext.request.contextPath}/mvc" method="post">
                            <input type="hidden" name="logica" value="DetalhesOrdem">
                            <input type="hidden" name="ordemId" value="${ordem.id}">
                            <input type="hidden" name="origem" value="Relatorio">
                            <input type="hidden" name="dataInicial" value="${param.dataInicial}">
                            <input type="hidden" name="dataFinal" value="${param.dataFinal}">
                            <button class="btn btn-outline-primary btn-sm" type="submit">Ver detalhes</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table></div>

        <p>
            <strong>Total faturado:</strong>
            <fmt:formatNumber value="${totalFaturado}" type="currency" currencySymbol="R$" />
        </p>
    </c:if>
    <form class="qp-plain-form" action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="Inicio">
        <input class="btn btn-outline-secondary" type="submit" value="Voltar">
    </form>
</main>
</body>
</html>
