<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<nav class="qp-navbar" aria-label="Navegação principal">
    <div class="container qp-navbar-inner">
    <a class="qp-brand" href="${pageContext.request.contextPath}/mvc?logica=Inicio">
        <span class="qp-brand-mark" aria-hidden="true">Q</span>
        <span>Q-Pedala</span>
    </a>
    <div class="qp-nav-links">
    <form action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="NovoCliente">
        <button class="qp-nav-button" type="submit">Clientes</button>
    </form>
    <form action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="NovaBicicleta">
        <button class="qp-nav-button" type="submit">Bicicletas</button>
    </form>
    <form action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="NovoServico">
        <button class="qp-nav-button" type="submit">Serviços</button>
    </form>
    <form action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="ListaCadastro">
        <button class="qp-nav-button" type="submit">Cadastros</button>
    </form>
    <form action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="NovaOrdemServico">
        <button class="qp-nav-button" type="submit">Agendar OS</button>
    </form>
    <form action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="ListagemServicos">
        <button class="qp-nav-button" type="submit">Ordens</button>
    </form>
    <form action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="Historico">
        <button class="qp-nav-button" type="submit">Histórico</button>
    </form>
    <form action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="Relatorio">
        <button class="qp-nav-button" type="submit">Relatório</button>
    </form>
    </div>
    <form class="qp-logout" action="${pageContext.request.contextPath}/mvc" method="post">
        <input type="hidden" name="logica" value="Logout">
        <button class="btn btn-warning btn-sm" type="submit">Sair</button>
    </form>
    </div>
</nav>
