package com.lavajato.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lavajato.model.OrdemServico;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    @Query(nativeQuery = true, value = """
            SELECT COUNT(id) FROM ordem_servico;
            """)
    Long totalOrdemServico();

    List<OrdemServico> findAllByOrderById();

    @Query(nativeQuery = true, value = """
            SELECT *
            FROM ordem_servico
            ORDER BY id
            LIMIT :limit OFFSET :offset;
            """)
    List<OrdemServico> listarOrdensServicoPaginado(Long limit, Long offset);

    @Query(nativeQuery = true, value = """
            SELECT os.*
            FROM ordem_servico os
            INNER JOIN cliente c ON os.cliente_id = c.id
            WHERE LOWER(c.nome) LIKE CONCAT('%', LOWER(:termo), '%')
            ORDER BY os.id
            LIMIT :limit OFFSET :offset;
            """)
    List<OrdemServico> listarOrdensServicoPaginadoCliente(Long limit, Long offset, String termo);

    @Query(nativeQuery = true, value = """
            SELECT COUNT(os.id)
            FROM ordem_servico os
            INNER JOIN cliente c ON os.cliente_id = c.id
            WHERE LOWER(c.nome) LIKE CONCAT('%', LOWER(:termo), '%')
            """)
    long contarOrdensServicoCliente(String termo);

    @Query(nativeQuery = true, value = """
            SELECT os.*
            FROM ordem_servico os
            INNER JOIN veiculo v ON os.veiculo_id = v.id
            WHERE LOWER(v.tipo) LIKE CONCAT('%', LOWER(:termo), '%')
            ORDER BY os.id
            LIMIT :limit OFFSET :offset;
            """)
    List<OrdemServico> listarOrdensServicoPaginadoVeiculo(Long limit, Long offset, String termo);

    @Query(nativeQuery = true, value = """
            SELECT COUNT(os.id)
            FROM ordem_servico os
            INNER JOIN veiculo v ON os.veiculo_id = v.id
            WHERE LOWER(v.tipo) LIKE CONCAT('%', LOWER(:termo), '%')
            """)
    long contarOrdensServicoVeiculo(String termo);

    @Query(nativeQuery = true, value = """
            SELECT os.*
            FROM ordem_servico os
            INNER JOIN usuario u ON os.funcionario_id = u.id
            WHERE LOWER(u.nome) LIKE CONCAT('%', LOWER(:termo), '%')
            ORDER BY os.id
            LIMIT :limit OFFSET :offset;
            """)
    List<OrdemServico> listarOrdensServicoPaginadoFuncionario(Long limit, Long offset, String termo);

    @Query(nativeQuery = true, value = """
            SELECT COUNT(os.id)
            FROM ordem_servico os
            INNER JOIN usuario u ON os.funcionario_id = u.id
            WHERE LOWER(u.nome) LIKE CONCAT('%', LOWER(:termo), '%')
            """)
    Long contarOrdensServicoFuncionario(String termo);

    @Query(nativeQuery = true, value = """
            SELECT os.*
            FROM ordem_servico os
            INNER JOIN servico s ON os.servico_id = s.id
            WHERE LOWER(s.tipo) = LOWER(:termo)
            ORDER BY os.id
            LIMIT :limit OFFSET :offset;
            """)
    List<OrdemServico> listarOrdensServicoPaginadoServico(Long limit, Long offset, String termo);

    @Query(nativeQuery = true, value = """
            SELECT COUNT(os.id)
            FROM ordem_servico os
            INNER JOIN servico s ON os.servico_id = s.id
            WHERE LOWER(s.tipo) LIKE CONCAT('%', LOWER(:termo), '%')
            """)
    Long contarOrdensServicoServico(String termo);

    @Query(nativeQuery = true, value = """
            SELECT os.*
            FROM ordem_servico os
            WHERE LOWER(os.status) LIKE CONCAT('%', LOWER(:termo), '%')
            ORDER BY os.id
            LIMIT :limit OFFSET :offset;
            """)
    List<OrdemServico> listarOrdensServicoPaginadoStatus(Long limit, Long offset, String termo);

    @Query(nativeQuery = true, value = """
            SELECT COUNT(os.id)
            FROM ordem_servico os
            WHERE LOWER(os.status) LIKE CONCAT('%', LOWER(:termo), '%')
            """)
    Long contarOrdensServicoStatus(String termo);
}
