<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/includes/bootstrap-head.jspf" %>
<title>Cadastro de Bicicleta</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />
<main class="container py-4">
	<h1>Cadastrar bicicleta</h1>
	<c:if test="${not empty erro}">
		<p class="alert alert-danger" role="alert"><c:out value="${erro}" /></p>
	</c:if>
		<form action="${pageContext.request.contextPath}/mvc" method="post">
	    <input type="hidden" name="logica" value="CadastroBicicleta">
	
	        <label class="form-label" for="marca">Marca:</label>
		        <input class="form-control" type="text" id="marca" name="marca" value="${param.marca}" required>
	        <br>
	        <label class="form-label" for="modelo">Modelo:</label>
		        <input class="form-control" type="text" id="modelo" name="modelo" value="${param.modelo}" required>
	        <br>
	        <label class="form-label" for="tipo">Tipo:</label>
	        <select class="form-select" id="tipo" name="tipo" required>
	            <option value="">Selecione</option>
		            <option value="MTB" ${param.tipo == 'MTB' ? 'selected' : ''}>MTB</option>
		            <option value="ROAD" ${param.tipo == 'ROAD' ? 'selected' : ''}>Road</option>
		            <option value="GRAVEL" ${param.tipo == 'GRAVEL' ? 'selected' : ''}>Gravel</option>
		            <option value="BMX" ${param.tipo == 'BMX' ? 'selected' : ''}>BMX</option>
		            <option value="ELETRICA" ${param.tipo == 'ELETRICA' ? 'selected' : ''}>Elétrica</option>
	        </select>
			<br>
	        <label class="form-label" for="tamanhoQuadro">Tamanho do quadro:</label>
		        <input class="form-control" type="text" id="tamanhoQuadro" name="tamanhoQuadro" value="${param.tamanhoQuadro}" required>
			<br>
	        <label class="form-label" for="numeroSerie">Número de série:</label>
		        <input class="form-control" type="text" id="numeroSerie" name="numeroSerie" value="${param.numeroSerie}" required>
			<br>
	        <label class="form-label" for="anoFabricacao">Ano de fabricação:</label>
	        <input class="form-control" type="number" id="anoFabricacao" name="anoFabricacao" min="1800" value="${param.anoFabricacao}" required>
			<br>
	        <label class="form-label" for="proprietarioId">Proprietário:</label>
			<select class="form-select" id="proprietarioId" name="proprietarioId">
				<option value="">Sem Proprietario</option>
				
				<c:forEach var="cliente"  items="${clientes}">
					<option value="${cliente.id}" ${param.proprietarioId == cliente.id ? 'selected' : ''}>
	                <c:out value="${cliente.nome}" />
	                — CPF: <c:out value="${cliente.cpf}" />
	            </option>
				</c:forEach>
			</select>
	    <button class="btn btn-primary" type="submit">Cadastrar bicicleta</button>
	</form>
    <form class="qp-plain-form" action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="Inicio">
        <input class="btn btn-outline-secondary" type="submit" value="Voltar">
    </form>
</main>
</body>
</html>
