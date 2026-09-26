CREATE TABLE IF NOT EXISTS veiculo (
    id BIGSERIAL PRIMARY KEY,
    modelo VARCHAR(100) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    cor VARCHAR(50),
    placa VARCHAR(20),
    tipo VARCHAR(20) NOT NULL,
    cliente_id BIGINT NOT NULL,
    ativo boolean not null default true,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP,
    CONSTRAINT fk_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

INSERT INTO veiculo(id, modelo, marca, cor, placa, tipo, cliente_id, data_criacao)
VALUES
    (1,'Creta', 'Hyundai', 'preto', '', 'CARRO', 2, NOW()),
    (2,'Gol', 'Volkswagen', 'prata', '', 'CARRO', 3, NOW()),
    (3,'Lancer', 'Mitsubishi', 'branco', '', 'CARRO', 4, NOW()),
    (4, 'Civic', 'Honda', 'azul', '', 'CARRO', 2, NOW()),
    (5, 'Pop', 'Honda', 'vermelho', '', 'MOTO', 1, NOW()),
    (6, 'Biz', 'Honda', 'preto', '', 'MOTO', 5, NOW()),
    (7, 'Cg 160', 'Honda', 'prata', '', 'MOTO', 3, NOW()),
    (8, 'Amarok', 'Volkswagen', 'branco', '', 'CAMINHONETE', 4, NOW()),
    (9, 'Toro', 'Fiat', 'preto', '', 'CAMINHONETE', 3, NOW()),
    (10, 'Hilux', 'Toyota', 'azul', '', 'CAMINHONETE', 5, NOW()),
    (11, 'FH540', 'Volvo', 'vermelho', '', 'CAMINHAO', 1, NOW()),
    (12, '1620', 'Mercedes-Benz', 'branco', '', 'CAMINHAO', 6, NOW());

SELECT setval('veiculo_id_seq', (SELECT MAX(id) FROM veiculo));
