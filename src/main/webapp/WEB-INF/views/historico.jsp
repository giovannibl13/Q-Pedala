<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
    
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/includes/bootstrap-head.jspf" %>
<title>Histórico</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />
<main class="container py-4">
	<h1>Histórico de serviços</h1>
	
	<form action="mvc" method="post">
            <input type="hidden" name="logica" value="Historico">
            <input type="hidden" name="tipo" value="cliente">
            
            <label class="form-label" for="cliente">Cliente:</label>
            <select class="form-select" name="id" id="cliente" required>
                <option value="">Selecione um cliente</option>

                <c:forEach var="cliente" items="${clientes}">
                    <option value="${cliente.id}"
                        ${tipoSelecionado == 'cliente' && idSelecionado == cliente.id ? 'selected' : ''}>
                        <c:out value="${cliente.nome}" />
                        — CPF: <c:out value="${cliente.cpf}" />
                    </option>
                </c:forEach>
            </select>
            <input class="btn btn-primary" type="submit" value="Consultar Cliente">
     </form>
     
     <form action="mvc" method="post">
            <input type="hidden" name="logica" value="Historico">
            <input type="hidden" name="tipo" value="bicicleta">

            <label class="form-label" for="bicicleta">Bicicleta</label>
            <select class="form-select" name="id" id="bicicleta" required>
                <option value="">Selecione uma bicicleta</option>

                <c:forEach var="bicicleta" items="${bicicletas}">
                    <option value="${bicicleta.id}"
                        ${tipoSelecionado == 'bicicleta' && idSelecionado == bicicleta.id ? 'selected' : ''}>
                        <c:out value="${bicicleta.marca}" />
                        <c:out value="${bicicleta.modelo}" />
                        — Série: <c:out value="${bicicleta.numeroSerie}" />
                    </option>
                </c:forEach>
            </select>

            <button class="btn btn-primary" type="submit">Consultar Bicicleta</button>
        </form>
        
     <c:if test="${pesquisou}">
        <h2>Resultado da consulta</h2>

        <div class="tabela">
            <div class="table-responsive"><table class="table table-striped table-hover align-middle">
                    <tr>
                        <th>OS</th>
                        <th>Data de execução</th>
                        <th>Cliente registrado na OS</th>
                        <th>Bicicleta</th>
                        <th>Status</th>
                        <th>Valor final</th>
                        <th>Detalhes</th>
                    </tr>

                    <c:if test="${empty ordens}">
                        <tr>
                            <td colspan="7">Nenhuma ordem executada ou paga encontrada.</td>
                        </tr>
                    </c:if>

                    <c:forEach var="ordem" items="${ordens}">
                        <tr>
                            <td><c:out value="${ordem.id}" /></td>

                            <td><fmt:formatDate value="${ordem.dataExecucaoParaExibicao}" pattern="dd/MM/yyyy" /></td>

                            <td><c:out value="${ordem.cliente.nome}" /></td>

                            <td>
                                <c:out value="${ordem.bicicleta.marca}" />
                                <c:out value="${ordem.bicicleta.modelo}" />
                                <br>
                                Série:
                                <c:out value="${ordem.bicicleta.numeroSerie}" />
                            </td>

                            <td><c:out value="${ordem.status.descricao}" /></td>

                            <td><fmt:formatNumber value="${ordem.valorFinal}" type="currency" currencySymbol="R$" /></td>

                            <td>
                                <form action="${pageContext.request.contextPath}/mvc" method="post">
                                    <input type="hidden" name="logica" value="DetalhesOrdem">
                                    <input type="hidden" name="ordemId" value="${ordem.id}">
                                    <input type="hidden" name="origem" value="Historico">
                                    <input type="hidden" name="tipo" value="${tipoSelecionado}">
                                    <input type="hidden" name="id" value="${idSelecionado}">
                                    <button class="btn btn-outline-primary btn-sm" type="submit">Ver detalhes</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table></div>
        </div>
    </c:if>
    <form class="qp-plain-form" action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="Inicio">
        <input class="btn btn-outline-secondary" type="submit" value="Voltar">
    </form>
</main>
</body>
</html>
