package com.lavajato.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lavajato.model.OrdemServico;

@Repository 
public interface RelatorioRepository extends JpaRepository<OrdemServico, Long> {
    
    /* Gerente */
    @Query(value = """
        SELECT u.nome, u.email, COUNT(os.id) AS servicos_finalizados, SUM(f.valor_bruto) AS total_bruto, SUM(f.valor_liquido) AS total_liquido, SUM(f.taxa_funcionario) AS total_funcionario, MAX(os.data_atualizacao) AS ultimo_servico_finalizado
        FROM usuario u
        LEFT JOIN ordem_servico os ON u.id = os.funcionario_id AND os.status = 'FINALIZADO'
		LEFT JOIN faturamento f ON f.ordem_servico_id = os.id
        WHERE u.tipo_usuario = 'FUNCIONARIO'
        AND (:dia IS NULL OR EXTRACT(DAY FROM f.data_criacao) = :dia)
        AND (:mes IS NULL OR EXTRACT(MONTH FROM f.data_criacao) = :mes)
        AND (:ano IS NULL OR EXTRACT(YEAR FROM f.data_criacao) = :ano)
        GROUP BY u.id, u.nome, u.email
        ORDER BY u.id ASC;
        """, nativeQuery = true)
    public List<Map<String, Object>> relatorioFuncionarios(Integer dia, Integer mes, Integer ano);

    @Query(value = """
    SELECT c.nome, c.celular, c.fidelidade, COUNT(os.id) AS lavagens, SUM(f.valor_liquido) AS total_liquido, MAX(os.data_atualizacao) AS ultima_lavagem_concluida
    FROM cliente c
    LEFT JOIN ordem_servico os ON c.id = os.cliente_id AND os.status = 'FINALIZADO'
	LEFT JOIN faturamento f ON f.ordem_servico_id = os.id
    WHERE (:dia IS NULL OR EXTRACT(DAY FROM f.data_criacao) = :dia)
	AND (:mes IS NULL OR EXTRACT(MONTH FROM f.data_criacao) = :mes)
	AND (:ano IS NULL OR EXTRACT(YEAR FROM f.data_criacao) = :ano)
    GROUP BY c.id, c.nome, c.celular, c.fidelidade
    ORDER BY lavagens DESC;
        """, nativeQuery = true)
    public List<Map<String, Object>> relatorioClientes(Integer dia, Integer mes, Integer ano);

    @Query(value = """
    SELECT u.nome as funcionario, c.nome as cliente, f.data_criacao as finalido_em, f.valor_bruto as bruto, f.taxa_funcionario as taxa_funcionario, f.valor_liquido as liquido
    FROM ordem_servico os
    INNER JOIN faturamento f ON os.id = f.ordem_servico_id
    INNER JOIN cliente c ON c.id = os.cliente_id
    INNER JOIN usuario u ON u.id = os.funcionario_id
	WHERE (:dia IS NULL OR EXTRACT(DAY FROM f.data_criacao) = :dia)
	AND (:mes IS NULL OR EXTRACT(MONTH FROM f.data_criacao) = :mes)
	AND (:ano IS NULL OR EXTRACT(YEAR FROM f.data_criacao) = :ano)
    ORDER BY f.data_criacao ASC;
        """, nativeQuery = true)
    public List<Map<String, Object>> relatorioFaturamento(Integer dia, Integer mes, Integer ano);

    /* Funcionário */
    @Query(value = """
        SELECT u.nome AS nome, c.nome as cliente, v.tipo as veiculo, s.tipo as servico, f.taxa_funcionario as faturamento, os.data_criacao as inicio, f.data_criacao as finalizado_em
        FROM usuario u
        INNER JOIN ordem_servico os ON u.id = os.funcionario_id AND os.status = 'FINALIZADO'
		INNER JOIN servico s ON s.id = os.servico_id
		INNER JOIN veiculo v ON v.id = os.veiculo_id
		INNER JOIN cliente c ON c.id = os.cliente_id
		INNER JOIN faturamento f ON f.ordem_servico_id = os.id
        WHERE u.id = :id
        AND (:dia IS NULL OR EXTRACT(DAY FROM f.data_criacao) = :dia)
        AND (:mes IS NULL OR EXTRACT(MONTH FROM f.data_criacao) = :mes)
        AND (:ano IS NULL OR EXTRACT(YEAR FROM f.data_criacao) = :ano)
        ORDER BY f.data_criacao ASC;
        """, nativeQuery = true)
    public List<Map<String, Object>> relatorioFuncionario(Long id, Integer dia, Integer mes, Integer ano);
}