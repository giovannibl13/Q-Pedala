<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/includes/bootstrap-head.jspf" %>
<title>Altera servico</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />
<main class="container py-4">
	<h1>Alterar valor do serviço</h1> 

	<c:if test="${not empty erro}">
		<p class="alert alert-danger" role="alert"><c:out value="${erro}" /></p>
	</c:if>
	<c:if test="${not empty sucesso}">
		<p class="alert alert-success" role="status"><c:out value="${sucesso}" /></p>
	</c:if>
	
	<form action="${pageContext.request.contextPath}/mvc" method="post">
		<input type="hidden" name="logica" value="AtualizaServico">
		<label class="form-label" for="servicoId">Serviço:</label>
		<select class="form-select" id="servicoId" name="servicoId" required>
			<option value="">Sem Servico</option>
			
			<c:forEach var="servico"  items="${servicos}">
				<option value="${servico.id}"
				        ${param.servicoId == servico.id ? 'selected' : ''}>
	               <c:out value="${servico.nome}" /> :
	               <fmt:formatNumber value="${servico.valor}" type="currency" currencySymbol="R$" />
	           </option>
			</c:forEach>
		</select>
		<label class="form-label" for="novoValor">Novo valor:</label>

    <input class="form-control" type="number" id="novoValor" name="novoValor" min="0" step="0.01"
           value="<c:out value='${param.novoValor}' />" required>
    <br>
		<input class="btn btn-primary" type="submit" value="Salvar novo valor">	
	</form>
	<form class="qp-plain-form" action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="Inicio">
        <input class="btn btn-outline-secondary" type="submit" value="Voltar">
    </form>
</main>
</body>
</html>
