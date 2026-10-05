<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"
    uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt"
    uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/includes/bootstrap-head.jspf" %>
<title>Pagar Ordem</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />
<main class="container py-4">
	<h1>Pagamento da ordem nº ${ordem.id}</h1>

	<c:if test="${not empty erro}">
		<p class="alert alert-danger" role="alert"><c:out value="${erro}" /></p>
	</c:if>
	
	<p>Cliente: ${ordem.cliente}</p>
	<p>Bicicleta: ${ordem.bicicleta}</p>
	<p>Data do Agendamento:
	    <fmt:formatDate value="${ordem.dataAgendamentoParaExibicao}" pattern="dd/MM/yyyy" />
	</p>
	<p>Data da execução:
	    <fmt:formatDate value="${ordem.dataExecucaoParaExibicao}" pattern="dd/MM/yyyy" />
	</p>
	
	<p>Serviços:</p>
	<c:forEach var="item" items="${ordem.itens}">
		<c:if test="${item.realizado}">
			<p><c:out value="${item.servico.nome}" />
            — <fmt:formatNumber value="${item.valorExecutado}"
                type="currency" currencySymbol="R$" /></p>
		</c:if>
	</c:forEach>
	
	<p>Subtotal:
		 <fmt:formatNumber value="${ordem.subtotal}"
		     type="currency" currencySymbol="R$" />
    </p>
    <form action="${pageContext.request.contextPath}/mvc" method="post">
    	<input type="hidden" name="logica" value="RegistraPagamento">
        <input type="hidden" name="ordemId" value="${ordem.id}">
        
         <c:if test="${podeAplicarDesconto}">
            <p>Esta ordem possui pelo menos três serviços realizados.</p>

            <input class="form-check-input" type="checkbox" id="aplicarDesconto" name="aplicarDesconto" value="true">

            <label class="form-label" for="aplicarDesconto">Aplicar desconto de 10%</label>
        </c:if>
        
        <c:if test="${not podeAplicarDesconto}">
            <p>O desconto exige pelo menos três serviços realizados.</p>
        </c:if>

        <br>

        <input class="btn btn-primary" type="submit" value="Confirmar pagamento">
    </form>
    
    <form class="qp-plain-form" action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="ListagemServicos">
        <input class="btn btn-outline-secondary" type="submit" value="Voltar">
    </form>
</main>
</body>
</html>
