# Q-Pedala — modelagem e organização

Modelagem da aplicação Q-Pedala, baseada nos requisitos do trabalho.

## Classes e relacionamentos

| Classe | Atributos principais | Relações |
| --- | --- | --- |
| Administrador | id, usuario, senhaHash | Acesso administrativo |
| Cliente | id, cpf, nome, dataNascimento, email, telefone | Pode possuir várias bicicletas e ordens |
| Bicicleta | id, marca, modelo, tipo, tamanhoQuadro, numeroSerie, anoFabricacao, proprietario | Proprietário opcional; várias ordens |
| Servico | id, nome, valor | Referenciado por vários itens de ordem |
| OrdemServico | id, cliente, bicicleta, dataAgendamento, dataExecucao, status, subtotal, desconto, valorFinal | Uma bicicleta, um cliente registrado no agendamento, um ou mais itens |
| ItemOrdemServico | id, ordem, servico, realizado, valorExecutado | Liga a ordem ao serviço e guarda o preço da execução |

Usar LocalDate para datas. Todos os valores monetários usam Double e aceitam valores com centavos (150.50 representa R$ 150,50). No PostgreSQL, os campos monetários usam NUMERIC(12,2). Status: Agendada, Executada, Paga e Cancelada. Tipos: MTB, Road, Gravel, BMX e Elétrica.

## Organização MVC

- `ti.mvc.servlet.ControleServlet`: única Servlet, em `/mvc`. Lê a lógica solicitada, verifica autenticação e encaminha a ação.
- `ti.mvc.logica.Logica`: interface das ações da aplicação.
- `ti.mvc.logica`: ações de cadastro, listagem, agendamento, execução, pagamento, histórico, relatório e detalhes.
- `ti.mvc.modelo`: entidades com atributos, getters e setters.
- `ti.mvc.modelo.dao`: consultas e gravações com PreparedStatement.
- `ti.mvc.connection.ConnectionFactory`: conexão JDBC com PostgreSQL.
- `src/main/webapp/WEB-INF/views`: JSPs com JSTL e dados recebidos pela requisição.

Fluxo: navegador → ControleServlet → Logica → DAO → PostgreSQL. A resposta volta para a JSP por RequestDispatcher. As JSPs não instanciam DAOs nem consultam o banco.

A controladora carrega a classe de lógica indicada pelo parâmetro `logica` usando reflexão. As ações protegidas verificam a sessão no servidor.

## Regras importantes

1. O proprietário atual da bicicleta pode mudar. O cliente registrado na OS permanece o mesmo.
2. No agendamento, a bicicleta precisa pertencer ao cliente selecionado.
3. Impedir duas ordens Agendadas para a mesma bicicleta e data, inclusive com índice único parcial no banco.
4. Agendada pode passar para Executada ou Cancelada; Executada pode passar para Paga.
5. Uma OS precisa de pelo menos um serviço; a execução precisa de pelo menos um item realizado.
6. Copiar o valor atual do serviço para o item no momento da execução. Mudanças posteriores no catálogo não alteram esse preço.
7. Somar somente os itens realizados. Na confirmação do pagamento, oferecer desconto de 10% para três ou mais serviços realizados. Aplicar apenas com confirmação do administrador.
8. Históricos incluem Executadas e Pagas. Pendências incluem somente Agendadas.
9. Relatório filtra pela data de execução e apresenta valores, descontos e total final. Identificar separadamente valores recebidos das ordens Pagas.
10. Validar CPF, e-mail, datas e referências no servidor. Não aceitar nascimento futuro.
11. Autenticação obrigatória, logout e sessão de dois minutos. Guardar JSPs em WEB-INF e verificar a sessão em todas as ações protegidas.
12. Armazenar senha administrativa com hash e salt. Fechar conexões e tratar falhas sem expor detalhes do banco nas telas.

## Situação implementada

As seis entidades e os enums TipoBicicleta e StatusOrdem estão implementados em `src/main/java/ti/mvc/modelo`. O script `sql/01-criar-tabelas.sql` define tabelas, chaves e restrições; `sql/02-dados-exemplo.sql` inclui administrador e dados de demonstração.

As ações implementam cadastros, consultas, agendamento, execução, cancelamento, pagamento, históricos, relatório e detalhes da OS. O banco impede agendamentos duplicados para a mesma bicicleta e data, referências inexistentes, valores negativos e combinações inválidas entre execução e preço dos itens. Validações de entrada e regras de estado também são verificadas pelas ações e pelos DAOs.

Os valores monetários são representados por `Double` no Java e por `NUMERIC(12,2)` no PostgreSQL. A senha administrativa usa PBKDF2 com salt. A sessão expira após dois minutos de inatividade. A conexão PostgreSQL é configurada atualmente na classe `ConnectionFactory`.
