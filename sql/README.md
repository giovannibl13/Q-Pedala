# Banco Q-Pedala

1. No pgAdmin, crie um banco chamado `qpedala`.
2. Abra o Query Tool desse banco.
3. Abra e execute `01-criar-tabelas.sql` uma vez.
4. Em seguida, execute `02-dados-exemplo.sql` uma vez.

O primeiro script cria seis tabelas dentro de uma transação. O segundo inclui o administrador e os dados usados na demonstração. Eles não apagam registros existentes e devem ser executados, na ordem indicada, sobre um banco vazio. Caso ocorra um erro no Query Tool, finalize a transação com `ROLLBACK;` antes de tentar novamente.

O administrador de demonstração possui o usuário `admin` e a senha `admin123`. A senha é armazenada no banco como hash PBKDF2, nunca como texto puro.

## Correspondência com Java

| PostgreSQL | Java |
| --- | --- |
| administrador | Administrador |
| cliente | Cliente |
| bicicleta | Bicicleta |
| servico | Servico |
| ordem_servico | OrdemServico |
| item_ordem_servico | ItemOrdemServico |

Os IDs são gerados por seis sequências explícitas (`CREATE SEQUENCE seq_nome_da_tabela`), começando em 1. Cada coluna id usa `DEFAULT nextval('seq_nome_da_tabela')`, e a sequência é vinculada à coluna com `OWNED BY`. Nos INSERTs, omita o id para gerar o próximo valor automaticamente. CPF é salvo sem pontuação e seus dígitos verificadores são validados pela aplicação. Tipo e status usam o nome do enum Java (`name()`); a descrição serve para exibição nas telas.

`proprietario_id` pode ser nulo na bicicleta. `cliente_id` da ordem registra o cliente do agendamento e não acompanha mudanças do proprietário da bicicleta. `valor_executado` fica nulo até o serviço ser realizado: nessa etapa será copiado o preço do catálogo. O campo `desconto` representa um valor em reais, não uma porcentagem.

As chaves estrangeiras impedem apagar registros referenciados por históricos. A aplicação valida a quantidade mínima de serviços, calcula os valores da execução, verifica a elegibilidade do desconto e controla as transições de status. Os exemplos contêm duas OS por status: Agendada, Executada, Paga e Cancelada. O script de dados inclui o usuário de demonstração `admin` (senha `admin123`, armazenada em hash).
