package com.lavajato.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavajato.dto.ordemServico.ClienteOrdemServico;
import com.lavajato.dto.ordemServico.OrdemServicoPaginadoResponse;
import com.lavajato.dto.ordemServico.OrdemServicoResponse;
import com.lavajato.dto.usuario.UsuarioResponse;
import com.lavajato.model.Cliente;
import com.lavajato.model.OrdemServico;
import com.lavajato.model.Servico;
import com.lavajato.model.Usuario;
import com.lavajato.model.Veiculo;
import com.lavajato.repository.ClienteRepository;
import com.lavajato.repository.OrdemServicoRepository;
import com.lavajato.repository.ServicoRepository;
import com.lavajato.repository.UsuarioRepository;
import com.lavajato.repository.VeiculoRepository;

@Service
public class OrdemServicoService {

    @Autowired
    OrdemServicoRepository ordemServicoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    VeiculoRepository veiculoRepository;
    @Autowired
    ServicoRepository servicoRepository;
    @Autowired
    FaturamentoService faturamentoService;

    public OrdemServicoResponse gerar(Map<String, Object> dados) {
        OrdemServico ordemServico = new OrdemServico();

        Long funcionarioId = Long.valueOf(dados.get("funcionarioId").toString());
        Long clienteId = Long.valueOf(dados.get("clienteId").toString());
        Long veiculoId = Long.valueOf(dados.get("veiculoId").toString());
        Long servicoId = Long.valueOf(dados.get("servicoId").toString());

        Usuario funcionario = usuarioRepository.findById(funcionarioId).orElse(null);
        Veiculo veiculo = veiculoRepository.findById(veiculoId).orElse(null);
        Servico servico = servicoRepository.findById(servicoId).orElse(null);
        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);
        
        ordemServico.setFuncionario(funcionario);
        ordemServico.setCliente(cliente);
        ordemServico.setVeiculo(veiculo);
        ordemServico.setServico(servico);
        ordemServico.setStatus(OrdemServico.Status.EM_ANDAMENTO);
        ordemServico.setDataCriacao(LocalDateTime.now());

        ordemServico = gerarPrecoOrdemServico(ordemServico);
        
        ordemServico = ordemServicoRepository.save(ordemServico);
        return ordemServicoToResponse(ordemServico);
    }

    public OrdemServicoResponse atualizar(Map<String, Object> dados) {
        Long ordemServicoId = dados.get("ordemServicoId") != null ? Long.parseLong(dados.get("ordemServicoId").toString()) : null;
        OrdemServico ordemServico = ordemServicoRepository.findById(ordemServicoId).orElse(null);

        Long funcionarioId = Long.valueOf(dados.get("funcionarioId").toString());
        Long clienteId = Long.valueOf(dados.get("clienteId").toString());
        Long veiculoId = Long.valueOf(dados.get("veiculoId").toString());
        Long servicoId = Long.valueOf(dados.get("servicoId").toString());
    
        String status = dados.get("status") != null ? (String) dados.get("status") : null;

        Usuario funcionario = usuarioRepository.findById(funcionarioId).orElse(null);
        Veiculo veiculo = veiculoRepository.findById(veiculoId).orElse(null);
        Servico servico = servicoRepository.findById(servicoId).orElse(null);
        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);
        
        ordemServico.setFuncionario(funcionario != null ? funcionario : ordemServico.getFuncionario());
        ordemServico.setCliente(cliente != null ? cliente : ordemServico.getCliente());
        ordemServico.setVeiculo(veiculo != null ? veiculo : ordemServico.getVeiculo());
        ordemServico.setServico(servico != null ? servico : ordemServico.getServico());
        ordemServico.setStatus(status != null ? OrdemServico.Status.valueOf(status.toUpperCase()) : ordemServico.getStatus());

        ordemServico = gerarPrecoOrdemServico(ordemServico);

        ordemServico = atualizarFidelidadeCliente(ordemServico);

        ordemServico.setDataAtualizacao(LocalDateTime.now());

        ordemServico = ordemServicoRepository.save(ordemServico);
        
        return ordemServicoToResponse(ordemServico);
    }

    public OrdemServicoPaginadoResponse listar(Map<String, Object> filtros) {
        List<OrdemServico> ordemServicos = new ArrayList<>();

        String tipo = filtros.get("tipo") != null ? filtros.get("tipo").toString() : "";
        String termo = filtros.get("termo") != null ? filtros.get("termo").toString() : "";
        Long paginacao = filtros.get("paginacao") != null ? Long.parseLong(filtros.get("paginacao").toString()) : 10;
        Long pagina = filtros.get("pagina") != null ? Long.parseLong(filtros.get("pagina").toString()) : 1;
        String intervaloTempo = filtros.get("intervaloTempo") != null ? filtros.get("intervaloTempo").toString() : "";
        String periodo = filtros.get("periodo") != null ? filtros.get("periodo").toString() : "";
        
        LocalDate localDate = LocalDate.now();
        Integer dia = localDate.getDayOfMonth();
        Integer mes = localDate.getMonthValue();
        Integer ano = localDate.getYear();

        String[] periodos = periodo.split("-");
        switch (intervaloTempo) {
            case "dia":
                if (periodos.length >= 1)
                    dia = Integer.parseInt(periodos[0]);
                if (periodos.length >= 2)
                    mes = Integer.parseInt(periodos[1]);
                if (periodos.length == 3)
                    ano = Integer.parseInt(periodos[2]);
                break;
            case "mes":
                dia = null;
                if (periodos.length >= 1)
                    mes = Integer.parseInt(periodos[0]);
                if (periodos.length >= 2)
                    ano = Integer.parseInt(periodos[1]);
                break;
            case "ano":
                dia = null;
                mes = null;
                ano = periodo.length() == 4 ? Integer.parseInt(periodo) : ano;
                break;
            default:
                dia = null;
                mes = null;
                ano = null;
                break;
        }

        Long contagem = 0L;
        long totalPaginas = 0;
        Long offset = (pagina - 1) == 0 ? 0 : (pagina - 1) * paginacao;

        switch (tipo) {
            case "cliente":
                ordemServicos = ordemServicoRepository.listarOrdensServicoPaginadoCliente(termo, dia, mes, ano, paginacao, offset);
                contagem = ordemServicoRepository.contarOrdensServicoCliente(termo, dia, mes, ano);
                totalPaginas = contagem / paginacao;
                if (contagem % paginacao > 0)
                    totalPaginas += 1;
                break;
            case "veiculo":
                ordemServicos = ordemServicoRepository.listarOrdensServicoPaginadoVeiculo(termo, dia, mes, ano, paginacao, offset);
                contagem = ordemServicoRepository.contarOrdensServicoVeiculo(termo, dia, mes, ano);
                totalPaginas = contagem / paginacao;
                if (contagem % paginacao > 0)
                    totalPaginas += 1;
                break;
            case "funcionario":
                ordemServicos = ordemServicoRepository.listarOrdensServicoPaginadoFuncionario(termo, dia, mes, ano, paginacao, offset);
                contagem = ordemServicoRepository.contarOrdensServicoFuncionario(termo, dia, mes, ano);
                totalPaginas = contagem / paginacao;
                if (contagem % paginacao > 0)
                    totalPaginas += 1;
                break;
            case "servico":
                ordemServicos = ordemServicoRepository.listarOrdensServicoPaginadoServico(termo, dia, mes, ano, paginacao, offset);
                contagem = ordemServicoRepository.contarOrdensServicoServico(termo, dia, mes, ano);
                totalPaginas = contagem / paginacao;
                if (contagem % paginacao > 0)
                    totalPaginas += 1;
                break;
            case "status":
                ordemServicos = ordemServicoRepository.listarOrdensServicoPaginadoStatus(termo, dia, mes, ano, paginacao, offset);
                contagem = ordemServicoRepository.contarOrdensServicoStatus(termo, dia, mes, ano);
                totalPaginas = contagem / paginacao;
                if (contagem % paginacao > 0)
                    totalPaginas += 1;
                break;
            default:
                ordemServicos = ordemServicoRepository.listarOrdensServicoPaginado(paginacao, offset);
                contagem = ordemServicoRepository.count();
                totalPaginas = contagem / paginacao;
                if (contagem % paginacao > 0)
                    totalPaginas += 1;
                break;
        }

        List<OrdemServicoResponse> lista = ordemServicos.stream().map(os -> {
            OrdemServicoResponse response = ordemServicoToResponse(os);
            return response;
        }).collect(Collectors.toList());
        OrdemServicoPaginadoResponse ospr = new OrdemServicoPaginadoResponse(contagem, totalPaginas, pagina, lista);
        return ospr;
    }

    public OrdemServicoResponse listarPorId(Long id) {
        OrdemServico ordemServico = ordemServicoRepository.findById(id).orElse(null);
        if (ordemServico != null) {
            return ordemServicoToResponse(ordemServico);
        } else {
            return null;
        }
    }

    public boolean deletar(Long id) {
        if (ordemServicoRepository.existsById(id)) {
            ordemServicoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public OrdemServicoResponse ordemServicoToResponse(OrdemServico ordemServico) {
        OrdemServicoResponse response = new OrdemServicoResponse();
        response.setId(ordemServico.getId());
        response.setFuncionario(new UsuarioResponse(ordemServico.getFuncionario().getId(), ordemServico.getFuncionario().getNome(), ordemServico.getFuncionario().getEmail(), ordemServico.getFuncionario().getTipoUsuario().toString(), ordemServico.getFuncionario().getUrlFoto(), ""));
        response.setCliente(new ClienteOrdemServico(ordemServico.getCliente().getId(),ordemServico.getCliente().getNome(), ordemServico.getCliente().getCelular(), ordemServico.getCliente().getFidelidade()));
        response.setVeiculo(ordemServico.getVeiculo());
        response.setServico(ordemServico.getServico());
        response.setPreco(ordemServico.getPreco());
        response.setStatus(ordemServico.getStatus().toString());
        String[] data = ordemServico.getDataCriacao().toString().split("T");
        String[] datas = data[0].split("-");
        response.setDataInicio(datas[2] + "-" + datas[1] + "-" + datas[0]);
        return response;
    }

    public OrdemServico gerarPrecoOrdemServico(OrdemServico ordemServico) {
        if (ordemServico.getCliente().getFidelidade() == 10) {
            ordemServico.setPreco(0);
        } else {
            switch (ordemServico.getVeiculo().getTipo().toString()) {
                case "MOTO":
                    ordemServico.setPreco(ordemServico.getServico().getPrecoBase());
                    break;
                case "CARRO":
                    ordemServico.setPreco(ordemServico.getServico().getPrecoBase() + 15);
                    break;
                case "CAMINHONETE":
                    ordemServico.setPreco(ordemServico.getServico().getPrecoBase() + 25);
                    break;
                case "CAMINHAO":
                    ordemServico.setPreco(ordemServico.getServico().getPrecoBase() + 40);
                    break;
                default:
                    break;
            }
        }
        return ordemServico;
    }

    public OrdemServico atualizarFidelidadeCliente(OrdemServico ordemServico) {
        if (ordemServico.getStatus() == OrdemServico.Status.FINALIZADO) {
            if (ordemServico.getCliente().getFidelidade() == 10) {
                ordemServico.getCliente().setFidelidade(0);
                clienteRepository.save(ordemServico.getCliente());
            } else {
                ordemServico.getCliente().setFidelidade(ordemServico.getCliente().getFidelidade() + 1);
                clienteRepository.save(ordemServico.getCliente());
            }
            faturamentoService.gerarFaturamento(ordemServico);
        }
        return ordemServico;
    }
    
}
