<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/includes/bootstrap-head.jspf" %>
<title>Agendar Serviços</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />
<main class="container py-4">
	<h1>Agendar Serviços</h1>
	<c:if test="${not empty erro}">
		<p class="alert alert-danger" role="alert"><c:out value="${erro}" /></p>
	</c:if>
	
	<h2>Cliente</h2>
	<form action="${pageContext.request.contextPath}/mvc" method="post">
				<input type="hidden" name="logica" value="NovaOrdemServico">
		 <label class="form-label" for="clienteId">Qual o Cliente:</label>
				<select class="form-select" id="clienteId" name="clienteId" required>
					<option value="">Sem Cliente</option>
					
					<c:forEach var="cliente"  items="${clientes}">
						<option value="${cliente.id}" ${param.clienteId == cliente.id ? 'selected' : ''}>
		                <c:out value="${cliente.nome}" />
		                — CPF: <c:out value="${cliente.cpf}" />
		            </option>
					</c:forEach>
				</select>
				<input class="btn btn-primary" type="submit" value="Buscar bicicletas">
	</form>

	<c:if test="${not empty bicicletas}">
	<form action="${pageContext.request.contextPath}/mvc" method="post">
		<input type="hidden" name="logica" value="CadastroOrdemServico">
		<input type="hidden" name="clienteId" value="${param.clienteId}">
				
		<h2>Bicicleta</h2>
		 <label class="form-label" for="bicicletaId">Qual a bicicleta do cliente:</label>
				<select class="form-select" id="bicicletaId" name="bicicletaId" required>
					<option value="">Bicicleta</option>
					
					<c:forEach var="bicicleta"  items="${bicicletas}">
						<option value="${bicicleta.id}" ${param.bicicletaId == bicicleta.id ? 'selected' : ''}>
		                <c:out value="${bicicleta.marca}" />
		            </option>
					</c:forEach>
				</select>
				
		<br>

		<label class="form-label" for="dataAgendamento">Data do agendamento:</label>
		<input class="form-control" type="date" id="dataAgendamento" name="dataAgendamento" value="<c:out value='${param.dataAgendamento}' />" required>
		<br><br>
		
		<label class="form-label" for="servico">O que o cliente quer:</label>
		<br>
		<c:forEach var="servico" items="${servicos}">

	    <c:set var="selecionado" value="false" />
	    <c:forEach var="idSelecionado" items="${paramValues.servicoId}">
	        <c:if test="${idSelecionado == servico.id}">
	            <c:set var="selecionado" value="true" />
	        </c:if>
	    </c:forEach>
	    <input class="form-check-input" type="checkbox"
	           id="servico${servico.id}"
	           name="servicoId"
	           value="${servico.id}" ${selecionado ? 'checked' : ''}>
	
	    <label class="form-label" for="servico${servico.id}">
	        <c:out value="${servico.nome}"/>
	        — <fmt:formatNumber value="${servico.valor}" type="currency" currencySymbol="R$" />
	    </label>
	    <br>
	</c:forEach>
		<input class="btn btn-primary" type="submit" value="Agendar serviço">
	</form>
	</c:if>
	
	<form class="qp-plain-form" action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="Inicio">
        <input class="btn btn-outline-secondary" type="submit" value="Voltar">
    </form>
</main>
</body>
</html>
