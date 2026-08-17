CREATE TABLE IF NOT EXISTS servico (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(255) NOT NULL,
    detalhes TEXT,
    preco_base DOUBLE PRECISION NOT NULL
);

INSERT INTO servico (tipo, detalhes, preco_base)
VALUES 
    ('Lavagem Simples', 'Lavagem do exterior do veículo', 40),
    ('Lavagem Completa', 'Lavagem interna e externa', 70),
    ('Lavagem Completa + Cera', 'Lavagem interna e externa + cera de alta durabilidade', 90);

SELECT setval('servico_id_seq', (SELECT MAX(id) FROM servico));
