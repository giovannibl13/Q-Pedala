<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/includes/bootstrap-head.jspf" %>
<title>Ordens pendentes</title>

</head>
<body>
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />
<main class="container py-4">
	<h1>Ordens de serviço</h1>
    <form action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="ListagemServicos">
            <label class="form-label" for="status">Status</label>
            <select class="form-select" name="status" id="status">
                <option value="">Agendadas e executadas</option>
                <c:forEach var="opcao" items="${statusDisponiveis}">
                    <option value="${opcao}" ${statusFiltro == opcao.name() ? 'selected' : ''}><c:out value="${opcao.descricao}" /></option>
                </c:forEach>
            </select>
            <br>
            <label class="form-label" for="dataInicial">Agendamento a partir de</label>
            <input class="form-control" type="date" name="dataInicial" id="dataInicial" value="<c:out value='${dataInicialFiltro}' />">
            <br>
            <label class="form-label" for="dataFinal">Agendamento até</label>
            <input class="form-control" type="date" name="dataFinal" id="dataFinal" value="<c:out value='${dataFinalFiltro}' />">
            <br>
        <button class="btn btn-primary" type="submit">Filtrar</button>
    </form>
    <form class="qp-plain-form" action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="ListagemServicos">
        <button class="btn btn-outline-secondary" type="submit">Limpar filtros</button>
    </form>
    <p>As datas incluem os dias informados. Para consultar um único dia, preencha as duas datas com o mesmo dia.</p>
    <c:if test="${not empty erro}">
        <p class="alert alert-danger" role="alert"><c:out value="${erro}" /></p>
    </c:if>

		<div class="table-responsive"><table class="table table-striped table-hover align-middle">
		    <tr>
		        <th>Número</th>
		        <th>Cliente</th>
		        <th>Bicicleta</th>
		        <th>Data</th>
		        <th>Serviços</th>
		        <th>Status</th>
		        <th>Detalhes</th>
		    </tr>
		
			<c:if test="${empty servicos and empty erro}">
	    		<tr><td colspan="7">Nenhuma ordem encontrada para os filtros informados.</td></tr>
			</c:if>
		    <c:forEach var="servico" items="${servicos}">
		        <tr>
		            <td><c:out value="${servico.id}" /></td>
		            <td><c:out value="${servico.cliente.nome}" /></td>
		            <td><c:out value="${servico.bicicleta.marca}" /> <c:out value="${servico.bicicleta.modelo}" /></td>
		            <td><fmt:formatDate value="${servico.dataAgendamentoParaExibicao}" pattern="dd/MM/yyyy" /></td>
                    <td><ul>
                        <c:forEach var="item" items="${servico.itens}">
                            <li><c:out value="${item.servico.nome}" /></li>
                        </c:forEach>
                    </ul></td>
		            <td>
		            	<c:out value="${servico.status.descricao}" />
		            	
		            	<c:if test="${servico.status == 'AGENDADA' || servico.status == 'EXECUTADA'}">
						    <form action="${pageContext.request.contextPath}/mvc" method="post">
						        <input type="hidden" name="logica" value="AlterarStatusOrdem">
						        <input type="hidden" name="ordemId" value="${servico.id}">
						
						        <select class="form-select" name="acao" required aria-label="Ação da ordem ${servico.id}">
						            <option value="">Selecione uma ação</option>
						
						            <c:if test="${servico.status == 'AGENDADA'}">
						                <option value="executar">Registrar execução</option>
						                <option value="cancelar">Cancelar OS</option>
						            </c:if>
						
						            <c:if test="${servico.status == 'EXECUTADA'}">
						                <option value="pagar">Registrar pagamento</option>
						            </c:if>
						        </select>
						
					        <button class="btn btn-primary btn-sm" type="submit">Continuar</button>
						    </form>
		            	</c:if>
		            </td>
		            <td>
		                <form action="${pageContext.request.contextPath}/mvc" method="post">
		                    <input type="hidden" name="logica" value="DetalhesOrdem">
		                    <input type="hidden" name="ordemId" value="${servico.id}">
		                    <input type="hidden" name="origem" value="ListagemServicos">
		                    <input type="hidden" name="status" value="${statusFiltro}">
		                    <input type="hidden" name="dataInicial" value="${dataInicialFiltro}">
		                    <input type="hidden" name="dataFinal" value="${dataFinalFiltro}">
		                    <button class="btn btn-outline-primary btn-sm" type="submit">Ver detalhes</button>
		                </form>
		            </td>
		        </tr>
		    </c:forEach>
		</table></div>
    
    <form class="qp-plain-form" action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="Inicio">
        <input class="btn btn-outline-secondary" type="submit" value="Voltar">
    </form>
</main>
</body>
</html>
