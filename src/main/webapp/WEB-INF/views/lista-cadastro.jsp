<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/includes/bootstrap-head.jspf" %>
<title>Listagens</title>
</head>

<body>
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />
<main class="container py-4">

    <h1>Listagens</h1>

    <form action="${pageContext.request.contextPath}/mvc" method="post">

        <input type="hidden" name="logica" value="ListaCadastro">

        <label class="form-label" for="tipo">Escolha o que deseja listar:</label>

        <select class="form-select" id="tipo" name="tipo" required>
            <option value="">Selecione</option>
            <option value="clientes">Clientes</option>
            <option value="bicicletas">Bicicletas</option>
            <option value="servicos">Serviços</option>
        </select>

        <input class="btn btn-primary" type="submit" value="Listar">

    </form>

    <c:if test="${param.tipo == 'clientes'}">

        <h2>Clientes</h2>

        <div class="table-responsive"><table class="table table-striped table-hover align-middle">
            <tr>
                <th>ID</th>
                <th>CPF</th>
                <th>Nome</th>
                <th>Data de nascimento</th>
                <th>E-mail</th>
                <th>Telefone</th>
            </tr>

            <c:forEach var="cliente" items="${clientes}">
                <tr>
                    <td><c:out value="${cliente.id}"/></td>
                    <td><c:out value="${cliente.cpf}"/></td>
                    <td><c:out value="${cliente.nome}"/></td>
                    <td><fmt:formatDate value="${cliente.dataNascimentoParaExibicao}" pattern="dd/MM/yyyy" /></td>
                    <td><c:out value="${cliente.email}"/></td>
                    <td><c:out value="${cliente.telefone}"/></td>
                </tr>
            </c:forEach>
        </table></div>

    </c:if>

    <c:if test="${param.tipo == 'bicicletas'}">

        <h2>Bicicletas</h2>

        <div class="table-responsive"><table class="table table-striped table-hover align-middle">
            <tr>
                <th>ID</th>
                <th>Marca</th>
                <th>Modelo</th>
                <th>Tipo</th>
                <th>Tamanho do quadro</th>
                <th>Número de série</th>
                <th>Ano</th>
                <th>Proprietário</th>
            </tr>

            <c:forEach var="bicicleta" items="${bicicletas}">
                <tr>
                    <td><c:out value="${bicicleta.id}"/></td>
                    <td><c:out value="${bicicleta.marca}"/></td>
                    <td><c:out value="${bicicleta.modelo}"/></td>
                    <td><c:out value="${bicicleta.tipo}"/></td>
                    <td><c:out value="${bicicleta.tamanhoQuadro}"/></td>
                    <td><c:out value="${bicicleta.numeroSerie}"/></td>
                    <td><c:out value="${bicicleta.anoFabricacao}"/></td>
                    <td><c:out value="${bicicleta.proprietario.nome}"/></td>
                </tr>
            </c:forEach>
        </table></div>

    </c:if>

    <c:if test="${param.tipo == 'servicos'}">

        <h2>Serviços</h2>

        <div class="table-responsive"><table class="table table-striped table-hover align-middle">
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Valor</th>
            </tr>

            <c:forEach var="servico" items="${servicos}">
                <tr>
                    <td><c:out value="${servico.id}"/></td>
                    <td><c:out value="${servico.nome}"/></td>
                    <td>
                        <fmt:formatNumber value="${servico.valor}" type="currency" currencySymbol="R$" />
                    </td>
                </tr>
            </c:forEach>
        </table></div>

    </c:if>

    <br>

    <form class="qp-plain-form" action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="Inicio">
        <input class="btn btn-outline-secondary" type="submit" value="Voltar">
    </form>

</main>
</body>
</html>
