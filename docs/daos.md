# DAOs implementados

Os DAOs ficam em `ti.mvc.modelo.dao`. Seguem o padrão de SQL com PreparedStatement usado na aula e fecham Connection, PreparedStatement e ResultSet com try-with-resources.

| DAO | Operações |
| --- | --- |
| ClienteDAO | adiciona, atualiza, remove, buscaPorId, lista |
| BicicletaDAO | adiciona, atualiza (inclui proprietário), remove, buscaPorId, lista |
| ServicoDAO | adiciona, atualiza (inclui preço), remove, buscaPorId, lista |
| AdministradorDAO | adiciona com hash fornecido, buscaPorUsuario, alteraSenha com hash fornecido |
| OrdemServicoDAO | adiciona com itens em transação, cancela, atualiza execução, atualiza pagamento, buscaPorId, lista, listaFiltrada, listaAgendadas, historicoPorCliente, historicoPorBicicleta |
| ItemOrdemServicoDAO | listaPorOrdem, inserção e atualização interna usadas pelo DAO da ordem |

Os cadastros recebem objetos dos modelos. Nos INSERTs, o ID é omitido para usar a sequência do PostgreSQL; RETURNING id preenche o objeto depois da inserção. Consultas por ID ou usuário retornam null quando não encontram o registro.

Remoções de clientes, bicicletas e serviços usados por outros registros são bloqueadas pelas chaves estrangeiras. Ordens são canceladas, não excluídas. Não há atualização genérica de OS nem CRUD independente de itens, para evitar mudanças arbitrárias no histórico. A edição do proprietário atual da bicicleta não altera cliente_id das ordens.

`Servico.valor`, `ItemOrdemServico.valorExecutado` e os totais de `OrdemServico` usam Double e aceitam valores com centavos. Para exibir o preço histórico, usar valorExecutado do item, não o preço atual do serviço.

No modelo Administrador, `getSenha()` retorna o conteúdo armazenado em `senha_hash`. O hash é gerado e verificado por `SenhaUtil`; o DAO persiste o hash recebido. Nunca passar senha em texto puro aos métodos de cadastro e troca de hash.

## Integração com a aplicação

As ações da aplicação chamam os DAOs e preparam os atributos das JSPs. A execução atualiza os itens realizados e a OS na mesma transação. O pagamento atualiza status, desconto e valor final. Ordens são canceladas, não excluídas; não há CRUD independente de itens para preservar o histórico.

Os valores monetários usam `Double` no Java e `NUMERIC(12,2)` no PostgreSQL. Os IDs continuam usando `Long`. As exceções SQL são encapsuladas em exceções de execução com mensagens destinadas às ações da aplicação.
