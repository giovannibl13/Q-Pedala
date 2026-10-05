-- Execute este arquivo no banco qpedala, criado previamente no pgAdmin.
-- O script cria as tabelas; nao remove nem substitui tabelas existentes.
BEGIN;

CREATE SEQUENCE seq_administrador AS BIGINT START WITH 1 INCREMENT BY 1;

CREATE TABLE administrador (
    id BIGINT DEFAULT nextval('seq_administrador') PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE CHECK (btrim(usuario) <> ''),
    -- Armazenar formato codificado com algoritmo, parametros, salt e hash.
    senha_hash VARCHAR(255) NOT NULL CHECK (btrim(senha_hash) <> '')
);

CREATE SEQUENCE seq_cliente AS BIGINT START WITH 1 INCREMENT BY 1;

CREATE TABLE cliente (
    id BIGINT DEFAULT nextval('seq_cliente') PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE CHECK (cpf ~ '^[0-9]{11}$'),
    nome VARCHAR(150) NOT NULL CHECK (btrim(nome) <> ''),
    data_nascimento DATE NOT NULL CHECK (data_nascimento <= CURRENT_DATE),
    email VARCHAR(254) NOT NULL CHECK (btrim(email) <> ''),
    telefone VARCHAR(20) NOT NULL CHECK (btrim(telefone) <> '')
);

CREATE SEQUENCE seq_bicicleta AS BIGINT START WITH 1 INCREMENT BY 1;

CREATE TABLE bicicleta (
    id BIGINT DEFAULT nextval('seq_bicicleta') PRIMARY KEY,
    marca VARCHAR(80) NOT NULL CHECK (btrim(marca) <> ''),
    modelo VARCHAR(100) NOT NULL CHECK (btrim(modelo) <> ''),
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('MTB', 'ROAD', 'GRAVEL', 'BMX', 'ELETRICA')),
    tamanho_quadro VARCHAR(20) NOT NULL CHECK (btrim(tamanho_quadro) <> ''),
    numero_serie VARCHAR(100) NOT NULL UNIQUE CHECK (btrim(numero_serie) <> ''),
    ano_fabricacao INTEGER NOT NULL CHECK (ano_fabricacao BETWEEN 1800 AND EXTRACT(YEAR FROM CURRENT_DATE)),
    proprietario_id BIGINT REFERENCES cliente(id)
);

CREATE SEQUENCE seq_servico AS BIGINT START WITH 1 INCREMENT BY 1;

CREATE TABLE servico (
    id BIGINT DEFAULT nextval('seq_servico') PRIMARY KEY,
    nome VARCHAR(120) NOT NULL CHECK (btrim(nome) <> ''),
    valor NUMERIC(12,2) NOT NULL CHECK (valor >= 0)
);

CREATE SEQUENCE seq_ordem_servico AS BIGINT START WITH 1 INCREMENT BY 1;

CREATE TABLE ordem_servico (
    id BIGINT DEFAULT nextval('seq_ordem_servico') PRIMARY KEY,
    -- Cliente registrado no agendamento: nao acompanha trocas de proprietario.
    cliente_id BIGINT NOT NULL REFERENCES cliente(id),
    bicicleta_id BIGINT NOT NULL REFERENCES bicicleta(id),
    data_agendamento DATE NOT NULL,
    data_execucao DATE,
    status VARCHAR(10) NOT NULL DEFAULT 'AGENDADA'
        CHECK (status IN ('AGENDADA', 'EXECUTADA', 'PAGA', 'CANCELADA')),
    subtotal NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (subtotal >= 0),
    desconto NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (desconto >= 0 AND desconto <= subtotal),
    valor_final NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (valor_final = subtotal - desconto),
    CHECK ((status IN ('AGENDADA', 'CANCELADA') AND data_execucao IS NULL)
        OR (status IN ('EXECUTADA', 'PAGA') AND data_execucao IS NOT NULL)),
    CHECK (status = 'PAGA' OR desconto = 0),
    CHECK (status IN ('EXECUTADA', 'PAGA') OR subtotal = 0)
);

CREATE UNIQUE INDEX uq_bicicleta_agendamento
    ON ordem_servico (bicicleta_id, data_agendamento)
    WHERE status = 'AGENDADA';

CREATE SEQUENCE seq_item_ordem_servico AS BIGINT START WITH 1 INCREMENT BY 1;

CREATE TABLE item_ordem_servico (
    id BIGINT DEFAULT nextval('seq_item_ordem_servico') PRIMARY KEY,
    ordem_id BIGINT NOT NULL REFERENCES ordem_servico(id),
    servico_id BIGINT NOT NULL REFERENCES servico(id),
    realizado BOOLEAN NOT NULL DEFAULT FALSE,
    -- Preco copiado do catalogo na execucao, e nao no agendamento.
    valor_executado NUMERIC(12,2),
    UNIQUE (ordem_id, servico_id),
    CHECK ((realizado AND valor_executado IS NOT NULL AND valor_executado >= 0)
        OR (NOT realizado AND valor_executado IS NULL))
);

ALTER SEQUENCE seq_administrador OWNED BY administrador.id;
ALTER SEQUENCE seq_cliente OWNED BY cliente.id;
ALTER SEQUENCE seq_bicicleta OWNED BY bicicleta.id;
ALTER SEQUENCE seq_servico OWNED BY servico.id;
ALTER SEQUENCE seq_ordem_servico OWNED BY ordem_servico.id;
ALTER SEQUENCE seq_item_ordem_servico OWNED BY item_ordem_servico.id;

CREATE INDEX idx_bicicleta_proprietario ON bicicleta(proprietario_id);
CREATE INDEX idx_ordem_cliente ON ordem_servico(cliente_id);
CREATE INDEX idx_ordem_bicicleta ON ordem_servico(bicicleta_id);
CREATE INDEX idx_ordem_execucao ON ordem_servico(data_execucao);
CREATE INDEX idx_item_servico ON item_ordem_servico(servico_id);

-- A camada de servico deve salvar a OS e seus itens na mesma transacao,
-- exigindo pelo menos um item. Ela tambem validara CPF, email, transicoes
-- de status, proprietario, soma dos itens e confirmacao do desconto.
COMMIT;
