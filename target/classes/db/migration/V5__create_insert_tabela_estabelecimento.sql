CREATE TABLE IF NOT EXISTS estabelecimento(
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    chave_pix VARCHAR(100) NOT NULL,
    porcentagem_funcionario INT,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP
);

INSERT INTO estabelecimento (id, nome, chave_pix, porcentagem_funcionario, data_criacao)
VALUES(1, 'Coyote Lava Jato', 'coyotelavajato@gmail.com', 40, NOW());

SELECT setval('estabelecimento_id_seq', (SELECT MAX(id) FROM estabelecimento));