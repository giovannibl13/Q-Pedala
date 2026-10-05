<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/includes/bootstrap-head.jspf" %>
    <title>Login - Q-Pedala</title>
</head>
<body>
<main class="qp-login">
    <h1>Q-Pedala</h1>
    <h2>Login do administrador</h2>

    <c:if test="${not empty erro}">
        <p class="alert alert-danger" role="alert"><c:out value="${erro}" /></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="Login">

        <p>
            <label class="form-label" for="usuario">Usuário:</label>
            <input class="form-control" type="text" id="usuario" name="usuario"
                   autocomplete="username" required>
        </p>

        <p>
            <label class="form-label" for="senha">Senha:</label>
            <input class="form-control" type="password" id="senha" name="senha"
                   autocomplete="current-password" required>
        </p>

        <input class="btn btn-primary w-100" type="submit" value="Entrar">
    </form>
</main>
</body>
</html>
