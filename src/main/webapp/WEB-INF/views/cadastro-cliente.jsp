<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/includes/bootstrap-head.jspf" %>
<title>Cadastro</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />
<main class="container py-4">
	<h1>Cadastrar cliente</h1>
	<c:if test="${not empty erro}">
		<p class="alert alert-danger" role="alert"><c:out value="${erro}" /></p>
	</c:if>
		<form action="${pageContext.request.contextPath}/mvc" method="post">
		<input type="hidden" name="logica" value="CadastroCliente">
		
	    <label class="form-label" for="cpf">CPF:</label>
		    <input class="form-control" type="text" id="cpf" name="cpf" value="${param.cpf}" placeholder="000.000.000-00" required>
		<br>
	    <label class="form-label" for="nome">Nome:</label>
		    <input class="form-control" type="text" id="nome" name="nome" value="${param.nome}" required>
		<br>
	    <label class="form-label" for="dataNascimento">Data de nascimento:</label>
		    <input class="form-control" type="date" id="dataNascimento" name="dataNascimento" value="${param.dataNascimento}" required>
	    <br>
	    <label class="form-label" for="email">E-mail:</label>
		    <input class="form-control" type="email" id="email" name="email" value="${param.email}" required>
	    <br>
	    <label class="form-label" for="telefone">Telefone:</label>
		    <input class="form-control" type="tel" id="telefone" name="telefone" value="${param.telefone}" placeholder="(11) 99999-9999" required>
		<br>
		<input class="btn btn-primary" type="submit" value="Cadastrar">
	</form>
	    <form class="qp-plain-form" action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="Inicio">
        <input class="btn btn-outline-secondary" type="submit" value="Voltar">
    </form>
</main>
</body>
</html>
