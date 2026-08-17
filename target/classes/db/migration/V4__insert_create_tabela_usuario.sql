CREATE TABLE IF NOT EXISTS usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(20) NOT NULL
);

INSERT INTO usuario (nome, email, senha, tipo_usuario)
VALUES
    ('Admin', 'admin@lavajato.com', '$2a$10$wjpLuaY1GBxGwiGG7Owm2.sIpMQJ4a/L0p8qAwGDrTJBDcMjEufNm', 'ADM'),
    ('Gerente', 'gerente@lavajato.com', '$2a$10$wjpLuaY1GBxGwiGG7Owm2.sIpMQJ4a/L0p8qAwGDrTJBDcMjEufNm', 'GERENTE'),
    ('Funcionario', 'func@lavajato.com', '$2a$10$wjpLuaY1GBxGwiGG7Owm2.sIpMQJ4a/L0p8qAwGDrTJBDcMjEufNm', 'FUNCIONARIO'),
    ('Leo', 'leonardo@gmail.com', '$2a$10$wjpLuaY1GBxGwiGG7Owm2.sIpMQJ4a/L0p8qAwGDrTJBDcMjEufNm', 'FUNCIONARIO'),
    ('Marcos', 'marcos@lavajato.com', '$2a$10$wjpLuaY1GBxGwiGG7Owm2.sIpMQJ4a/L0p8qAwGDrTJBDcMjEufNm', 'FUNCIONARIO');

SELECT setval('usuario_id_seq', (SELECT MAX(id) FROM usuario));