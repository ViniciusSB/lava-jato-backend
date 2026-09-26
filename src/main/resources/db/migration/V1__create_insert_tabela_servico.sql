CREATE TABLE IF NOT EXISTS servico (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(255) NOT NULL,
    detalhes TEXT,
    preco_base DOUBLE PRECISION NOT NULL,
    ativo boolean not null default true,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP
);

INSERT INTO servico (tipo, detalhes, preco_base, data_criacao)
VALUES 
    ('Lavagem Simples', 'Lavagem do exterior do veículo', 40, NOW()),
    ('Lavagem Completa', 'Lavagem interna e externa', 70, NOW()),
    ('Lavagem Completa + Cera', 'Lavagem interna e externa + cera de alta durabilidade', 90, NOW());

SELECT setval('servico_id_seq', (SELECT MAX(id) FROM servico));
