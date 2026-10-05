<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/includes/bootstrap-head.jspf" %>

    <title>Executar Ordem</title>
</head>

<body>
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />
<main class="container py-4">
    <h1>Registrar serviços realizados</h1>

    <c:if test="${not empty erro}">
        <p class="alert alert-danger" role="alert">
            <c:out value="${erro}" />
        </p>
    </c:if>

    <h2>
    	Ordem número
        <c:out value="${ordem.id}" />
    </h2>

    <p>
        Cliente:
        <c:out value="${ordem.cliente.nome}" />
    </p>

    <p>
        Bicicleta:
        <c:out value="${ordem.bicicleta.marca}" />

        <c:out value="${ordem.bicicleta.modelo}" />
    </p>

    <p>
        Data do agendamento:
        <fmt:formatDate value="${ordem.dataAgendamentoParaExibicao}" pattern="dd/MM/yyyy" />
    </p>

    <form action="${pageContext.request.contextPath}/mvc"  method="post">

        <input type="hidden" name="logica" value="RegistrarExecucao">
        <input type="hidden" name="ordemId" value="${ordem.id}">

        <label class="form-label" for="dataExecucao">Data da execução:</label>

        <input class="form-control" type="date" id="dataExecucao" name="dataExecucao"
               value="${empty param.dataExecucao ? dataHoje : param.dataExecucao}"
               required>

        <h3>Serviços da ordem</h3>

        <c:forEach var="item" items="${ordem.itens}">
            <p>
                <input class="form-check-input" type="checkbox" id="item${item.id}" name="itemId" value="${item.id}">

                <label class="form-label" for="item${item.id}">
                    <c:out
                        value="${item.servico.nome}" />

                    — <fmt:formatNumber value="${item.servico.valor}"
                        type="currency" currencySymbol="R$" />
                </label>
            </p>

        </c:forEach>

        <input class="btn btn-primary" type="submit" value="Confirmar execução">
    </form>

    <form class="qp-plain-form"
        action="${pageContext.request.contextPath}/mvc"  method="post">

        <input type="hidden" name="logica" value="ListagemServicos">

        <input class="btn btn-outline-secondary" type="submit" value="Voltar">
    </form>
</main>
</body>

</html>
