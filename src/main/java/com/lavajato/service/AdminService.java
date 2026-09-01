package com.lavajato.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavajato.model.Cliente;
import com.lavajato.model.Faturamento;
import com.lavajato.model.OrdemServico;
import com.lavajato.model.Servico;
import com.lavajato.model.Usuario;
import com.lavajato.model.Veiculo;
import com.lavajato.repository.ClienteRepository;
import com.lavajato.repository.FaturamentoRepository;
import com.lavajato.repository.OrdemServicoRepository;
import com.lavajato.repository.ServicoRepository;
import com.lavajato.repository.UsuarioRepository;
import com.lavajato.repository.VeiculoRepository;

@Service
public class AdminService {

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ServicoRepository servicoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    VeiculoRepository veiculoRepository;
    @Autowired
    OrdemServicoRepository ordemServicoRepository;
    @Autowired
    OrdemServicoService ordemServicoService;
    @Autowired
    FaturamentoRepository faturamentoRepository;

    public String gerarOrdensServico(Map<String, Object> dados) {
        String dataParametro = dados.get("data") != null ? dados.get("data").toString() : null;
        DateTimeFormatter dataFormatada;
        String datas[] = dataParametro != null ? dataParametro.split("-") : null;
        List<Usuario> funcionarios = usuarioRepository.findByTipoUsuario(Usuario.tipoUsuario.FUNCIONARIO);
        List<Cliente> clientes = clienteRepository.findAll();
        List<Servico> servicos = servicoRepository.findAll();
        List<Veiculo> veiculos = veiculoRepository.findAll();

        if (datas != null && datas.length == 3) {
            // Dia
            dataFormatada = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime data = LocalDate.parse(dataParametro, dataFormatada).atTime(LocalTime.now());
            for (Usuario f : funcionarios) {
                gerarOrdemServicoTeste(f, clientes, servicos, veiculos, data);
            }
        } else if (datas != null && datas.length == 2) {
            // Mês
            dataFormatada = DateTimeFormatter.ofPattern("MM-yyyy");
            YearMonth mes = YearMonth.parse(dataParametro, dataFormatada);
            for (int i = 1; i < mes.lengthOfMonth() + 1; i++) {
                for (Usuario f : funcionarios) {
                    dataFormatada = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String mesAtual = mes.getMonthValue() < 10 ? "0" + mes.getMonthValue()
                            : String.valueOf(mes.getMonthValue());
                    String diaAtual = i < 10 ? "0" + String.valueOf(i) : String.valueOf(i);
                    String diaMesAno = diaAtual + "-" + mesAtual + "-" + mes.getYear();
                    LocalDateTime data = LocalDate.parse(diaMesAno, dataFormatada).atTime(LocalTime.now());
                    gerarOrdemServicoTeste(f, clientes, servicos, veiculos, data);
                }
            }
        } else if (datas != null && datas.length == 1) {
            // Ano
            Year ano = Year.parse(dataParametro);
            for (int i = 1; i < 13; i++) {
                dataFormatada = DateTimeFormatter.ofPattern("MM-yyyy");
                String mesAtual = i < 10 ? "0" + String.valueOf(i) : String.valueOf(i);
                dataParametro = mesAtual + "-" + ano.getValue();
                YearMonth anoMes = YearMonth.parse(dataParametro, dataFormatada);
                for (int j = 1; j < anoMes.lengthOfMonth() + 1; j++) {
                    for (Usuario f : funcionarios) {
                        dataFormatada = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String diaAtual = j < 10 ? "0" + String.valueOf(j) : String.valueOf(j);
                        String diaMesAno = diaAtual + "-" + mesAtual + "-" + ano.getValue();
                        LocalDateTime data = LocalDate.parse(diaMesAno, dataFormatada).atTime(LocalTime.now());
                        gerarOrdemServicoTeste(f, clientes, servicos, veiculos, data);
                    }
                }
            }
        }

        return "Ordens geradas";
    }

    public void gerarOrdemServicoTeste(Usuario f, List<Cliente> clientes, List<Servico> servicos,
            List<Veiculo> veiculos, LocalDateTime data) {
        Random random = new Random();
        Cliente cliente = clientes.get(random.nextInt(clientes.size()));
        Servico servico = servicos.get(random.nextInt(servicos.size()));
        List<Veiculo> veiculosCliente = veiculos.stream().filter(vc -> vc.getCliente().getId() == cliente.getId())
                .toList();
        Veiculo veiculoSelecionado = veiculosCliente.get(random.nextInt(veiculosCliente.size()));

        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setFuncionario(f);
        ordemServico.setCliente(cliente);
        ordemServico.setVeiculo(veiculoSelecionado);
        ordemServico.setServico(servico);
        ordemServico.setDataCriacao(data.minusHours(2));
        ordemServico.setStatus(OrdemServico.Status.EM_ANDAMENTO);
        ordemServico = ordemServicoService.gerarPrecoOrdemServico(ordemServico);
        ordemServico = ordemServicoRepository.save(ordemServico);

        // Atualizar
        ordemServico.setStatus(OrdemServico.Status.FINALIZADO);
        ordemServico.setDataAtualizacao(data);
        ordemServico = ordemServicoService.atualizarFidelidadeCliente(ordemServico);
        ordemServico = ordemServicoRepository.save(ordemServico);

        // Atualizar data do faturamento
        Faturamento faturamento = faturamentoRepository.findByOrdemServicoId(ordemServico.getId());
        faturamento.setDataCriacao(data);
        faturamento = faturamentoRepository.save(faturamento);
    }
}
