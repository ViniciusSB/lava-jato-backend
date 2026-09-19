CREATE TABLE IF NOT EXISTS usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(20) NOT NULL,
    ativo boolean not null default true,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP
);

INSERT INTO usuario (nome, email, senha, tipo_usuario, data_criacao)
VALUES
    ('Admin', 'admin@lavajato.com', '$2a$10$wjpLuaY1GBxGwiGG7Owm2.sIpMQJ4a/L0p8qAwGDrTJBDcMjEufNm', 'ADM', NOW()),
    ('Gerente', 'gerente@lavajato.com', '$2a$10$wjpLuaY1GBxGwiGG7Owm2.sIpMQJ4a/L0p8qAwGDrTJBDcMjEufNm', 'GERENTE', NOW()),
    ('João', 'joao@gmail.com', '$2a$10$wjpLuaY1GBxGwiGG7Owm2.sIpMQJ4a/L0p8qAwGDrTJBDcMjEufNm', 'FUNCIONARIO', NOW()),
    ('Leo', 'leonardo@gmail.com', '$2a$10$wjpLuaY1GBxGwiGG7Owm2.sIpMQJ4a/L0p8qAwGDrTJBDcMjEufNm', 'FUNCIONARIO', NOW()),
    ('Marcos', 'marcos@gmail.com', '$2a$10$wjpLuaY1GBxGwiGG7Owm2.sIpMQJ4a/L0p8qAwGDrTJBDcMjEufNm', 'FUNCIONARIO', NOW()),
    ('Pedro', 'pedro@gmail.com', '$2a$10$wjpLuaY1GBxGwiGG7Owm2.sIpMQJ4a/L0p8qAwGDrTJBDcMjEufNm', 'FUNCIONARIO', NOW()),
    ('Rafael', 'rafael@gmail.com', '$2a$10$wjpLuaY1GBxGwiGG7Owm2.sIpMQJ4a/L0p8qAwGDrTJBDcMjEufNm', 'FUNCIONARIO', NOW()),
    ('Matheus', 'matheus@gmail.com', '$2a$10$wjpLuaY1GBxGwiGG7Owm2.sIpMQJ4a/L0p8qAwGDrTJBDcMjEufNm', 'FUNCIONARIO', NOW());
    
SELECT setval('usuario_id_seq', (SELECT MAX(id) FROM usuario));