# Q-Pedala

Sistema web para gerenciamento dos serviços de manutenção da bicicletaria e oficina Q-Pedala. A aplicação permite controlar clientes, bicicletas, serviços oferecidos e todo o ciclo das Ordens de Serviço (OS), desde o agendamento até a execução e o pagamento.

O acesso é exclusivo do administrador e todas as funcionalidades passam por uma única Servlet controladora, seguindo o modelo MVC.

## Funcionalidades

- Login e logout do administrador.
- Encerramento da sessão após dois minutos de inatividade.
- Cadastro e listagem de clientes.
- Validação de CPF, e-mail e data de nascimento no servidor.
- Cadastro e listagem de bicicletas, com proprietário opcional.
- Cadastro de serviços e alteração de seus valores.
- Agendamento de uma OS com uma bicicleta e um ou mais serviços.
- Bloqueio de dois agendamentos para a mesma bicicleta na mesma data.
- Listagem das OS Agendadas e Executadas.
- Cancelamento de OS Agendadas.
- Registro dos serviços efetivamente realizados e da data de execução.
- Armazenamento do valor histórico de cada serviço no item da OS.
- Pagamento com desconto opcional de 10% para três ou mais serviços realizados.
- Histórico de OS por cliente e por bicicleta.
- Relatório por intervalo de datas com total faturado.
- Página genérica com os detalhes de qualquer Ordem de Serviço.
- Página amigável para erros inesperados.

## Tecnologias utilizadas

- Java 25
- Jakarta Servlet 6.1
- JSP
- Bootstrap 5.3.8 por CDN, com estilos complementares em `src/main/webapp/css/qpedala.css`.
- JSTL 3
- Maven
- PostgreSQL
- JDBC
- Apache Tomcat 11

## Organização do projeto

```text
src/main/java/ti/mvc
├── connection    Conexão JDBC
├── logica        Ações executadas pela controladora
├── modelo        Entidades e enums
├── modelo/dao    Acesso e persistência dos dados
├── servlet       Servlet controladora única
└── util          Utilitários, incluindo hash de senha

src/main/webapp
├── login.jsp
└── WEB-INF/views JSPs protegidas contra acesso direto

sql
├── 01-criar-tabelas.sql
└── 02-dados-exemplo.sql
```

O fluxo principal é:

```text
Navegador → ControleServlet → Logica → DAO → PostgreSQL
                                    ↓
                                   JSP
```

A Servlet está mapeada para `/mvc`. O parâmetro `logica` identifica a ação responsável por processar a requisição.

## Pré-requisitos

Para executar o projeto, instale:

- JDK 25;
- Maven 3.9 ou superior;
- PostgreSQL;
- Apache Tomcat 11 ou outro servidor compatível com Jakarta Servlet 6.1.

Confirme as instalações:

```bash
java -version
mvn -version
```

## Configuração do banco de dados

Crie no PostgreSQL um banco vazio chamado:

```text
qpedala
```

Depois execute os scripts nesta ordem:

1. [`sql/01-criar-tabelas.sql`](sql/01-criar-tabelas.sql)
2. [`sql/02-dados-exemplo.sql`](sql/02-dados-exemplo.sql)

O primeiro script cria as tabelas, sequências, chaves, índices e restrições. O segundo insere:

- 1 administrador;
- 3 clientes;
- 4 bicicletas;
- 10 serviços;
- 8 Ordens de Serviço;
- exemplos com os status Agendada, Executada, Paga e Cancelada.

Os scripts foram preparados para execução, uma única vez, em um banco vazio.

Antes de iniciar a aplicação, copie `src/main/resources/banco.properties.example`
para `src/main/resources/banco.properties` e informe a URL, o usuário e a senha
do seu PostgreSQL. O arquivo com a senha local é ignorado pelo Git.

```text
jdbc:postgresql://localhost:5432/qpedala
```

Mais detalhes estão em [`sql/README.md`](sql/README.md).

## Administrador de demonstração

Após executar o script de dados de exemplo, utilize:

```text
Usuário: admin
Senha: admin123
```

A senha não é armazenada em texto puro. O sistema utiliza PBKDF2 com HMAC-SHA-256, salt aleatório e 600.000 iterações. O script contém somente o resultado do hash necessário para a autenticação do administrador de demonstração.

## Compilação com Maven

Na pasta que contém o `pom.xml`, execute:

```bash
mvn clean package
```

O arquivo WAR será gerado em:

```text
target/GiovanniTrabalho-0.0.1-SNAPSHOT.war
```

## Execução no Tomcat

Uma forma de executar é copiar o WAR gerado para a pasta `webapps` do Tomcat e iniciar o servidor.

O endereço dependerá do nome utilizado no deploy. Com o nome padrão do projeto, será semelhante a:

```text
http://localhost:8080/GiovanniTrabalho/
```

Também é possível importar o projeto Maven no Eclipse, adicionar um servidor Tomcat 11 e executar com **Run on Server**.

## Regras de negócio principais

- Um cliente pode possuir várias bicicletas.
- Uma bicicleta pode estar associada a, no máximo, um cliente.
- O proprietário da bicicleta é opcional no cadastro.
- Uma bicicleta não pode possuir duas OS Agendadas para a mesma data.
- Uma OS deve possuir pelo menos um serviço.
- Uma OS pode assumir os status Agendada, Executada, Paga ou Cancelada.
- Somente uma OS Agendada pode ser executada ou cancelada.
- Somente uma OS Executada pode ser paga.
- O valor atual do serviço é copiado para o item da OS no momento da execução.
- Alterações posteriores no catálogo não modificam valores históricos.
- O cliente registrado na OS é preservado mesmo após uma troca de proprietário da bicicleta.
- O desconto de 10% só pode ser aplicado com confirmação do administrador e quando pelo menos três serviços foram realizados.
- OS Pagas e Canceladas não aparecem na listagem de ordens pendentes.

## Segurança e validações

- Todas as ações, exceto o login, verificam a sessão no servidor.
- As JSPs funcionais ficam dentro de `WEB-INF` e não podem ser abertas diretamente.
- A sessão expira após 120 segundos de inatividade.
- O cookie de sessão utiliza a opção `HttpOnly`.
- CPF, e-mail, datas, IDs, valores e transições de status são validados no servidor.
- Consultas e alterações no banco utilizam `PreparedStatement`.
- Exceções inesperadas são registradas no servidor e encaminhadas para uma página de erro sem exposição de detalhes internos.

## Modelagem

A descrição das entidades e de seus relacionamentos está disponível em [`docs/modelagem.md`](docs/modelagem.md). A aplicação possui as entidades:

- `Administrador`;
- `Cliente`;
- `Bicicleta`;
- `Servico`;
- `OrdemServico`;
- `ItemOrdemServico`.

`ItemOrdemServico` representa a associação entre uma OS e um serviço, indicando se o serviço foi realizado e preservando o valor cobrado no momento da execução.
