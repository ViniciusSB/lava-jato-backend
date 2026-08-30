package com.lavajato.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lavajato.model.OrdemServico;

@Repository
public interface DashboardRepository extends JpaRepository<OrdemServico, Long> {

    /* GERENTE */
    // Total de funcionarios, tipos de usuarios, funcionario destaque
    @Query(value = """
            SELECT u.nome, COUNT(os.id) AS servicos_finalizados, u.tipo_usuario
            FROM usuario u
            LEFT JOIN ordem_servico os ON u.id = os.funcionario_id AND os.status = 'FINALIZADO'
            AND (:dia IS NULL OR EXTRACT(DAY FROM os.data_atualizacao) = :dia)
            AND (:mes IS NULL OR EXTRACT(MONTH FROM os.data_atualizacao) = :mes)
            AND (:ano IS NULL OR EXTRACT(YEAR FROM os.data_atualizacao) = :ano)
            GROUP BY u.nome, u.tipo_usuario
            ORDER BY count(os.id) DESC;
                        """, nativeQuery = true)
    List<Map<String, Object>> gerenteTotalFuncionarios(Integer dia, Integer mes, Integer ano);
    
    //Faturamento bruto e liquido
    @Query(value = """
            SELECT valor_bruto, valor_liquido, data_criacao
            FROM faturamento
            WHERE (:dia IS NULL OR EXTRACT(DAY FROM data_criacao) = :dia)
            AND (:mes IS NULL OR EXTRACT(MONTH FROM data_criacao) = :mes)
            AND (:ano IS NULL OR EXTRACT(YEAR FROM data_criacao) = :ano);
                        """, nativeQuery = true)
    List<Map<String, Object>> gerenteFaturamento(Integer dia, Integer mes, Integer ano);

    //Total de clientes atendidos, serviços finalidos e ordens em andamento
    @Query(value = """
            SELECT COUNT(status) as atendidos, 
            SUM(CASE WHEN status = 'EM_ANDAMENTO' THEN 1 ELSE 0 END) AS em_andamento, 
            SUM(CASE WHEN status = 'FINALIZADO' THEN 1 ELSE 0 END) AS finalizados
            FROM ordem_servico
            WHERE (:dia IS NULL OR EXTRACT(DAY FROM data_criacao) = :dia)
            AND (:mes IS NULL OR EXTRACT(MONTH FROM data_criacao) = :mes)
            AND (:ano IS NULL OR EXTRACT(YEAR FROM data_criacao) = :ano);
                        """, nativeQuery = true)
    Map<String, Object> gerenteInformacoesServico(Integer dia, Integer mes, Integer ano);

    //Tipos de veiculos com ordem finalizadas na data especificada
    @Query(value = """
            SELECT v.tipo, COUNT(v.tipo) as quantidade
            FROM ordem_servico os
            INNER JOIN veiculo v ON v.id = os.veiculo_id
            WHERE os.status = 'FINALIZADO'
            AND (:dia IS NULL OR EXTRACT(DAY FROM os.data_atualizacao) = :dia)
            AND (:mes IS NULL OR EXTRACT(MONTH FROM os.data_atualizacao) = :mes)
            AND (:ano IS NULL OR EXTRACT(YEAR FROM os.data_atualizacao) = :ano)
            GROUP BY v.tipo;
                        """, nativeQuery = true)
    List<Map<String, Object>> gerenteTipoVeiculosFinalizados(Integer dia, Integer mes, Integer ano);

    /* FUNCIONÁRIO */

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
