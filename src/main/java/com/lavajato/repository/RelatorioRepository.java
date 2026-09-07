package com.lavajato.repository;

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
        GROUP BY u.id, u.nome, u.email
        ORDER BY u.id ASC;
        """, nativeQuery = true)
    public Map<String, Object> relatorioFuncionarios();

    @Query(value = """
    SELECT c.nome, c.celular, c.fidelidade, COUNT(os.id) AS lavagens, SUM(f.valor_liquido) AS total_liquido, MAX(os.data_atualizacao) AS ultima_lavagem_concluida
    FROM cliente c
    LEFT JOIN ordem_servico os ON c.id = os.cliente_id AND os.status = 'FINALIZADO'
	LEFT JOIN faturamento f ON f.ordem_servico_id = os.id
    GROUP BY c.id, c.nome, c.celular, c.fidelidade
    ORDER BY lavagens DESC;
        """, nativeQuery = true)
    public Map<String, Object> relatorioClientes();

    @Query(value = """
    SELECT u.nome as funcionario, c.nome as cliente, f.data_criacao as finalido_em, f.valor_bruto as bruto, f.taxa_funcionario as taxa_funcionario, f.valor_liquido as liquido
    FROM ordem_servico os
    INNER JOIN faturamento f ON os.id = f.ordem_servico_id
    INNER JOIN cliente c ON c.id = os.cliente_id
    INNER JOIN usuario u ON u.id = os.funcionario_id
    ORDER BY f.data_criacao DESC;
        """, nativeQuery = true)
    public Map<String, Object> relatorioFaturamento();

    /* Funcionário */
    @Query(value = """
        SELECT c.nome as cliente, v.tipo as veiculo, s.tipo as servico, f.taxa_funcionario as faturamento, os.data_criacao as inicio, f.data_criacao as finalizado_em
        FROM usuario u
        INNER JOIN ordem_servico os ON u.id = os.funcionario_id AND os.status = 'FINALIZADO'
		INNER JOIN servico s ON s.id = os.servico_id
		INNER JOIN veiculo v ON v.id = os.veiculo_id
		INNER JOIN cliente c ON c.id = os.cliente_id
		INNER JOIN faturamento f ON f.ordem_servico_id = os.id
        WHERE u.id = :id
        ORDER BY f.data_criacao ASC;
        """, nativeQuery = true)
    public Map<String, Object> relatorioFuncionario(Long id);
}
