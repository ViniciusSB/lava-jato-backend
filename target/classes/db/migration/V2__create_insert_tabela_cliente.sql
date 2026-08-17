CREATE TABLE IF NOT EXISTS cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    celular VARCHAR(20),
    fidelidade INT DEFAULT 0
);

INSERT INTO cliente(id, nome, celular, fidelidade)
VALUES
    (1, 'Marina Liz', '(69)99362-6409', 0),
    (2, 'Esther Renata', '(11)99819-6219', 0),
    (3, 'Caleb Yago', '(95)99913-7382', 0),
    (4, 'Pedro Henrique', '(85)98280-3794', 0),
    (5, 'Luiz Joaquim', '(83)98402-4439', 0),
    (6, 'Natália Manuela', '(62)98636-8576', 0);

SELECT setval('cliente_id_seq', (SELECT MAX(id) FROM cliente));