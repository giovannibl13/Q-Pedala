-- Dados de demonstracao do sistema Q-Pedala.
-- Execute depois de 01-criar-tabelas.sql, em um banco vazio.
-- Administrador de demonstracao: usuario admin / senha admin123

BEGIN;

INSERT INTO administrador (usuario, senha_hash) VALUES
('admin', '600000:5zV8ieLqqzfn+RzBtMtmVQ==:bJaTPt4jyvK3CJBN1mmBMbqVTg9ucwuI45rE0DR0lrs=');

INSERT INTO cliente (cpf, nome, data_nascimento, email, telefone) VALUES
('52998224725', 'Ana Souza', '1990-04-12', 'ana.souza@example.com', '(11) 99991-1001'),
('11144477735', 'Bruno Lima', '1985-09-23', 'bruno.lima@example.com', '(11) 99992-1002'),
('12345678909', 'Carla Mendes', '1997-02-08', 'carla.mendes@example.com', '(11) 99993-1003');

INSERT INTO bicicleta
    (marca, modelo, tipo, tamanho_quadro, numero_serie, ano_fabricacao, proprietario_id)
VALUES
('Trek', 'Marlin 7', 'MTB', 'M', 'QP-TRK-001', 2023,
    (SELECT id FROM cliente WHERE cpf = '52998224725')),
('Caloi', 'Strada Racing', 'ROAD', '54', 'QP-CAL-002', 2022,
    (SELECT id FROM cliente WHERE cpf = '52998224725')),
('Cannondale', 'Topstone 4', 'GRAVEL', 'L', 'QP-CAN-003', 2024,
    (SELECT id FROM cliente WHERE cpf = '11144477735')),
('Specialized', 'Turbo Vado', 'ELETRICA', 'S', 'QP-SPE-004', 2025,
    (SELECT id FROM cliente WHERE cpf = '12345678909'));

INSERT INTO servico (nome, valor) VALUES
('Revisão básica', 150.00),
('Revisão completa', 250.00),
('Regulagem de câmbio', 50.00),
('Regulagem de freios', 40.00),
('Sangria de freio hidráulico', 80.00),
('Troca de corrente', 40.00),
('Troca de cassete', 50.00),
('Alinhamento de roda', 60.00),
('Troca de pneu', 30.00),
('Limpeza e lubrificação', 50.00);

-- OS 1: agendada, com dois servicos previstos.
WITH nova_os AS (
    INSERT INTO ordem_servico
        (cliente_id, bicicleta_id, data_agendamento, status)
    SELECT c.id, b.id, CURRENT_DATE + 3, 'AGENDADA'
    FROM cliente c
    JOIN bicicleta b ON b.proprietario_id = c.id
    WHERE c.cpf = '52998224725' AND b.numero_serie = 'QP-TRK-001'
    RETURNING id
)
INSERT INTO item_ordem_servico (ordem_id, servico_id)
SELECT nova_os.id, s.id
FROM nova_os
CROSS JOIN servico s
WHERE s.nome IN ('Revisão básica', 'Regulagem de freios');

-- OS 2: agendada.
WITH nova_os AS (
    INSERT INTO ordem_servico
        (cliente_id, bicicleta_id, data_agendamento, status)
    SELECT c.id, b.id, CURRENT_DATE + 4, 'AGENDADA'
    FROM cliente c
    JOIN bicicleta b ON b.proprietario_id = c.id
    WHERE c.cpf = '52998224725' AND b.numero_serie = 'QP-CAL-002'
    RETURNING id
)
INSERT INTO item_ordem_servico (ordem_id, servico_id)
SELECT nova_os.id, s.id
FROM nova_os
CROSS JOIN servico s
WHERE s.nome IN ('Troca de pneu', 'Alinhamento de roda');

-- OS 3: executada, total de R$ 190,00.
WITH nova_os AS (
    INSERT INTO ordem_servico
        (cliente_id, bicicleta_id, data_agendamento, data_execucao,
         status, subtotal, desconto, valor_final)
    SELECT c.id, b.id, CURRENT_DATE - 22, CURRENT_DATE - 20,
           'EXECUTADA', 190.00, 0.00, 190.00
    FROM cliente c
    JOIN bicicleta b ON b.proprietario_id = c.id
    WHERE c.cpf = '52998224725' AND b.numero_serie = 'QP-TRK-001'
    RETURNING id
)
INSERT INTO item_ordem_servico
    (ordem_id, servico_id, realizado, valor_executado)
SELECT nova_os.id, s.id, TRUE,
       CASE s.nome
           WHEN 'Revisão básica' THEN 150.00
           WHEN 'Regulagem de freios' THEN 40.00
       END
FROM nova_os
CROSS JOIN servico s
WHERE s.nome IN ('Revisão básica', 'Regulagem de freios');

-- OS 4: executada, total de R$ 310,00.
WITH nova_os AS (
    INSERT INTO ordem_servico
        (cliente_id, bicicleta_id, data_agendamento, data_execucao,
         status, subtotal, desconto, valor_final)
    SELECT c.id, b.id, CURRENT_DATE - 18, CURRENT_DATE - 15,
           'EXECUTADA', 310.00, 0.00, 310.00
    FROM cliente c
    JOIN bicicleta b ON b.proprietario_id = c.id
    WHERE c.cpf = '11144477735' AND b.numero_serie = 'QP-CAN-003'
    RETURNING id
)
INSERT INTO item_ordem_servico
    (ordem_id, servico_id, realizado, valor_executado)
SELECT nova_os.id, s.id, TRUE,
       CASE s.nome
           WHEN 'Revisão completa' THEN 250.00
           WHEN 'Alinhamento de roda' THEN 60.00
       END
FROM nova_os
CROSS JOIN servico s
WHERE s.nome IN ('Revisão completa', 'Alinhamento de roda');

-- OS 5: paga, com tres servicos e desconto de 10%.
WITH nova_os AS (
    INSERT INTO ordem_servico
        (cliente_id, bicicleta_id, data_agendamento, data_execucao,
         status, subtotal, desconto, valor_final)
    SELECT c.id, b.id, CURRENT_DATE - 12, CURRENT_DATE - 10,
           'PAGA', 350.00, 35.00, 315.00
    FROM cliente c
    JOIN bicicleta b ON b.proprietario_id = c.id
    WHERE c.cpf = '52998224725' AND b.numero_serie = 'QP-CAL-002'
    RETURNING id
)
INSERT INTO item_ordem_servico
    (ordem_id, servico_id, realizado, valor_executado)
SELECT nova_os.id, s.id, TRUE,
       CASE s.nome
           WHEN 'Revisão completa' THEN 250.00
           WHEN 'Regulagem de câmbio' THEN 50.00
           WHEN 'Limpeza e lubrificação' THEN 50.00
       END
FROM nova_os
CROSS JOIN servico s
WHERE s.nome IN (
    'Revisão completa',
    'Regulagem de câmbio',
    'Limpeza e lubrificação'
);

-- OS 6: paga, sem desconto porque possui apenas dois servicos.
WITH nova_os AS (
    INSERT INTO ordem_servico
        (cliente_id, bicicleta_id, data_agendamento, data_execucao,
         status, subtotal, desconto, valor_final)
    SELECT c.id, b.id, CURRENT_DATE - 7, CURRENT_DATE - 5,
           'PAGA', 120.00, 0.00, 120.00
    FROM cliente c
    JOIN bicicleta b ON b.proprietario_id = c.id
    WHERE c.cpf = '12345678909' AND b.numero_serie = 'QP-SPE-004'
    RETURNING id
)
INSERT INTO item_ordem_servico
    (ordem_id, servico_id, realizado, valor_executado)
SELECT nova_os.id, s.id, TRUE,
       CASE s.nome
           WHEN 'Sangria de freio hidráulico' THEN 80.00
           WHEN 'Troca de corrente' THEN 40.00
       END
FROM nova_os
CROSS JOIN servico s
WHERE s.nome IN ('Sangria de freio hidráulico', 'Troca de corrente');

-- OS 7: cancelada antes da execucao.
WITH nova_os AS (
    INSERT INTO ordem_servico
        (cliente_id, bicicleta_id, data_agendamento, status)
    SELECT c.id, b.id, CURRENT_DATE + 1, 'CANCELADA'
    FROM cliente c
    JOIN bicicleta b ON b.proprietario_id = c.id
    WHERE c.cpf = '11144477735' AND b.numero_serie = 'QP-CAN-003'
    RETURNING id
)
INSERT INTO item_ordem_servico (ordem_id, servico_id)
SELECT nova_os.id, s.id
FROM nova_os
CROSS JOIN servico s
WHERE s.nome IN ('Troca de cassete', 'Limpeza e lubrificação');

-- OS 8: cancelada antes da execucao.
WITH nova_os AS (
    INSERT INTO ordem_servico
        (cliente_id, bicicleta_id, data_agendamento, status)
    SELECT c.id, b.id, CURRENT_DATE + 2, 'CANCELADA'
    FROM cliente c
    JOIN bicicleta b ON b.proprietario_id = c.id
    WHERE c.cpf = '12345678909' AND b.numero_serie = 'QP-SPE-004'
    RETURNING id
)
INSERT INTO item_ordem_servico (ordem_id, servico_id)
SELECT nova_os.id, s.id
FROM nova_os
CROSS JOIN servico s
WHERE s.nome IN ('Troca de pneu', 'Regulagem de freios');

COMMIT;
