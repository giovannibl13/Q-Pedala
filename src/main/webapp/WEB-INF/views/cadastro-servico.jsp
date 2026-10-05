<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/includes/bootstrap-head.jspf" %>
<title>Cadastro Serviço</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />
<main class="container py-4">
	<h1>Cadastrar serviço</h1>

	<c:if test="${not empty erro}">
		<p class="alert alert-danger" role="alert"><c:out value="${erro}" /></p>
	</c:if>
	
	<form action="${pageContext.request.contextPath}/mvc" method="post">
		<input type="hidden" name="logica" value="CadastroServico">
		
		<label class="form-label" for="nome">Nome do Serviço</label>
		<input class="form-control" type="text" id="nome" name="nome"
		       value="<c:out value='${param.nome}' />" required>
		
		<label class="form-label" for="preco">Preço do serviço</label>
		<input class="form-control" type="number" id="preco" name="preco" min="0" step="0.01"
		       value="<c:out value='${param.preco}' />" required>
		
		<input class="btn btn-primary" type="submit" value="Cadastrar">
		</form>  
		
	    <form class="qp-plain-form" action="${pageContext.request.contextPath}/mvc" method="post">
	        <input type="hidden" name="logica" value="AlteraValorServico">
	        <input class="btn btn-outline-primary" type="submit" value="Alterar serviço">
	    </form>
	    
	    <form class="qp-plain-form" action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="Inicio">
        <input class="btn btn-outline-secondary" type="submit" value="Voltar">
    </form>
</main>
</body>
</html>
