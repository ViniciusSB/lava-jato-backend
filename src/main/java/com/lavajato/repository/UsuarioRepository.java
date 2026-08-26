package com.lavajato.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lavajato.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = """
            SELECT os.status, f.taxa_funcionario 
            FROM ordem_servico os 
            LEFT JOIN faturamento f 
            ON os.id = f.ordem_servico_id 
            WHERE os.funcionario_id = :id 
            AND EXTRACT(DAY FROM os.data_criacao) = :dia
            AND EXTRACT(MONTH FROM os.data_criacao) = :mes
            AND EXTRACT(YEAR FROM os.data_criacao) = :ano;
        """, nativeQuery = true)
    List<Map<String, Object>> dashboardTextoFuncionarioDia(Long id, Integer dia, Integer mes, Integer ano);

    @Query(value = """
            SELECT os.status, f.taxa_funcionario
            FROM ordem_servico os
            LEFT JOIN faturamento f
            ON f.ordem_servico_id = os.id
            WHERE os.funcionario_id = :id 
            AND EXTRACT(MONTH FROM os.data_criacao) = :mes
            AND EXTRACT(YEAR FROM os.data_criacao) = :ano;
        """, nativeQuery = true)
    List<Map<String, Object>> dashboardTextoFuncionarioMes(Long id, Integer mes, Integer ano);

    @Query(value = """
            SELECT os.status, f.taxa_funcionario
            FROM ordem_servico os
            LEFT JOIN faturamento f
            ON f.ordem_servico_id = os.id
            WHERE os.funcionario_id = :id 
            AND EXTRACT(YEAR FROM os.data_criacao) = :ano;
        """, nativeQuery = true)
    List<Map<String, Object>> dashboardTextoFuncionarioAno(Long id, Integer ano);

    @Query(value = """
            SELECT f.taxa_funcionario, f.data_criacao
            FROM ordem_servico os
            INNER JOIN faturamento f
            ON f.ordem_servico_id = os.id
            WHERE os.funcionario_id = :id
            AND EXTRACT(DAY FROM f.data_criacao) = :dia
            AND EXTRACT(MONTH FROM f.data_criacao) = :mes
            AND EXTRACT(YEAR FROM f.data_criacao) = :ano;
        """, nativeQuery = true)
    List<Map<String, Object>> dashboardGraficoFuncionarioDia(Long id, Integer dia, Integer mes, Integer ano);

    @Query(value = """
            SELECT f.taxa_funcionario, f.data_criacao
            FROM ordem_servico os
            INNER JOIN faturamento f
            ON f.ordem_servico_id = os.id
            WHERE os.funcionario_id = :id
            AND EXTRACT(MONTH FROM f.data_criacao) = :mes
            AND EXTRACT(YEAR FROM f.data_criacao) = :ano;
        """, nativeQuery = true)
    List<Map<String, Object>> dashboardGraficoFuncionarioMes(Long id, Integer mes, Integer ano);

    @Query(value = """
            SELECT f.taxa_funcionario, f.data_criacao
            FROM ordem_servico os
            INNER JOIN faturamento f
            ON f.ordem_servico_id = os.id
            WHERE os.funcionario_id = :id
            AND EXTRACT(YEAR FROM f.data_criacao) = :ano;
        """, nativeQuery = true)
    List<Map<String, Object>> dashboardGraficoFuncionarioAno(Long id, Integer ano);
}
