# Configurar e testar a conexão

O projeto utiliza PostgreSQL no banco `qpedala`, em `localhost:5432`. A classe `ConnectionFactory` carrega o driver JDBC e abre as conexões.

A `ConnectionFactory` carrega URL, usuário e senha de
`src/main/resources/banco.properties`. Copie o arquivo
`banco.properties.example`, renomeie a cópia para `banco.properties` e preencha
as credenciais do PostgreSQL local. O arquivo preenchido é ignorado pelo Git.

Para testar a conexão no Eclipse, execute `ti.teste.TesteConexao` com **Run As → Java Application**. O teste consulta o nome do banco e o usuário.

As credenciais do PostgreSQL são diferentes da senha do administrador Q-Pedala. A senha do administrador é armazenada como hash PBKDF2 no banco. Não publique credenciais pessoais ao compartilhar o projeto.
