CREATE TABLE IF NOT EXISTS cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    celular VARCHAR(20),
    fidelidade INT DEFAULT 0,
    ativo boolean not null default true,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP
);

INSERT INTO cliente(id, nome, celular, fidelidade, data_criacao)
VALUES
    (1, 'Marina Liz', '(69)99362-6409', 0, NOW()),
    (2, 'Esther Renata', '(11)99819-6219', 0, NOW()),
    (3, 'Caleb Yago', '(95)99913-7382', 0, NOW()),
    (4, 'Pedro Henrique', '(85)98280-3794', 0, NOW()),
    (5, 'Luiz Joaquim', '(83)98402-4439', 0, NOW()),
    (6, 'Natália Manuela', '(62)98636-8576', 0, NOW());

SELECT setval('cliente_id_seq', (SELECT MAX(id) FROM cliente));