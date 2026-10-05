<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/includes/bootstrap-head.jspf" %>
    <title>Erro - Q-Pedala</title>
</head>
<body>
    <jsp:include page="/WEB-INF/views/includes/navbar.jsp" />
    <main class="container py-4">
        <h1>Não foi possível concluir a operação</h1>

        <p class="alert alert-danger" role="alert">
            <c:choose>
                <c:when test="${not empty mensagemErro}">
                    <c:out value="${mensagemErro}" />
                </c:when>
                <c:otherwise>
                    Ocorreu um erro inesperado. Tente novamente.
                </c:otherwise>
            </c:choose>
        </p>

        <form class="qp-plain-form" action="${pageContext.request.contextPath}/mvc" method="post">
            <input type="hidden" name="logica" value="Inicio">
            <button class="btn btn-outline-secondary" type="submit">Voltar ao início</button>
        </form>
    </main>
</body>
</html>
