package com.lavajato.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavajato.dto.dashboard.funcionario.DashboardFuncionarioRequest;
import com.lavajato.dto.dashboard.funcionario.DashboardFuncionarioResponse;
import com.lavajato.dto.dashboard.funcionario.Grafico;
import com.lavajato.dto.usuario.UsuarioRequest;
import com.lavajato.dto.usuario.UsuarioResponse;
import com.lavajato.model.Usuario;
import com.lavajato.repository.UsuarioRepository;
import com.lavajato.util.SenhaUtil;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public UsuarioResponse cadastrarUsuario(Map<String, Object> dados) {
        String nome = dados.get("nome") != null ? (String) dados.get("nome") : null;
        String email = dados.get("email") != null ? (String) dados.get("email") : null;
        String senha = dados.get("senha") != null ? (String) dados.get("senha") : null;
        String tipo = dados.get("tipo") != null ? (String) dados.get("tipo") : null;

        senha = senha != null ? SenhaUtil.criptografar(senha) : null;

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setTipoUsuario(Usuario.tipoUsuario.valueOf(tipo.toUpperCase()));

        usuario = usuarioRepository.save(usuario);

        UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNome(),
                usuario.getEmail(), usuario.getTipoUsuario().toString());

        return response;
    }

    public List<UsuarioResponse> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(u -> {
            UsuarioResponse response = new UsuarioResponse(u.getId(), u.getNome(),
                    u.getEmail(), u.getTipoUsuario().toString());
            return response;
        }).collect(Collectors.toList());
    }

    public UsuarioResponse listarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNome(),
                    usuario.getEmail(), usuario.getTipoUsuario().toString());
            return response;
        }
        return null;
    }

    public UsuarioResponse atualizarUsuario(UsuarioRequest request) {
        Usuario banco = usuarioRepository.findById(request.getId()).orElse(null);
        if (banco != null) {
            if (request.getNome() != null)
                banco.setNome(request.getNome());
            if (request.getEmail() != null)
                banco.setEmail(request.getEmail());
            if (request.getTipo() != null)
                banco.setTipoUsuario(Usuario.tipoUsuario.valueOf(request.getTipo()));
            usuarioRepository.save(banco);
            return new UsuarioResponse(banco.getId(), banco.getNome(),
                    banco.getEmail(), banco.getTipoUsuario().toString());
        }
        return null;
    }

    public boolean deletarUsuario(Long usuarioId) {
        if (usuarioRepository.existsById(usuarioId)) {
            usuarioRepository.deleteById(usuarioId);
            return true;
        } else {
            return false;
        }
    }

    public DashboardFuncionarioResponse dashboardFuncionario(Long id, DashboardFuncionarioRequest request) {
        String tipo = request.getTipo() != null ? request.getTipo() : "";
        String periodo = request.getPeriodo() != null ? request.getPeriodo() : "";

        List<Map<String, Object>> dados = new ArrayList<>();
        List<Map<String, Object>> dadosGrafico = new ArrayList<>();

        LocalDate localDate = LocalDate.now();
        Integer dia = localDate.getDayOfMonth();
        Integer mes = localDate.getMonthValue();
        Integer ano = localDate.getYear();

        if (!tipo.isEmpty()) {
            String[] periodos = periodo.split("-");
            switch (tipo) {
                case "dia":
                    if (periodos.length >= 1)
                        dia = Integer.parseInt(periodos[0]);
                    if (periodos.length >= 2)
                        mes = Integer.parseInt(periodos[1]);
                    if (periodos.length == 3)
                        ano = Integer.parseInt(periodos[2]);
                    dados = usuarioRepository.dashboardTextoFuncionarioDia(id, dia, mes, ano);
                    dadosGrafico = usuarioRepository.dashboardGraficoFuncionarioDia(id, dia, mes, ano);
                    break;
                case "mes":
                    if (periodos.length >= 1)
                        dia = Integer.parseInt(periodos[0]);
                    if (periodos.length >= 2)
                        mes = Integer.parseInt(periodos[1]);
                    dados = usuarioRepository.dashboardTextoFuncionarioMes(id, mes, ano);
                    dadosGrafico = usuarioRepository.dashboardGraficoFuncionarioMes(id, mes, ano);
                    break;
                case "ano":
                    ano = periodo.length() == 4 ? Integer.parseInt(periodo) : ano;
                    dados = usuarioRepository.dashboardTextoFuncionarioAno(id, ano);
                    dadosGrafico = usuarioRepository.dashboardGraficoFuncionarioAno(id, ano);
                    break;
                default:
                    break;
            }
        }

        Long qtdOrdensFinalizadas = dados.stream().filter(qof -> qof.get("status").equals("FINALIZADO")).count();
        Long qtdOrdensEmAndamento = dados.stream().filter(qoe -> qoe.get("status").equals("EM_ANDAMENTO")).count();
        Double faturamentoTotal = dados.stream().mapToDouble(ft -> ft.get("taxa_funcionario") != null ? Double.parseDouble(ft.get("taxa_funcionario").toString()) : 0).sum();

        List<Grafico> graficos = dadosGrafico.stream().map(dg -> {
            Grafico grafico = new Grafico();
            grafico.setFaturamento(dg.get("taxa_funcionario") != null ? Double.parseDouble(dg.get("taxa_funcionario").toString()) : 0);
            
            LocalDateTime dataHora = (LocalDateTime) dg.get("data_criacao");
            grafico.setAno(String.valueOf(dataHora.getYear()));
            grafico.setMes(String.valueOf(dataHora.getMonthValue()));
            grafico.setDia(String.valueOf(dataHora.getDayOfMonth()));
            grafico.setHora(String.valueOf(dataHora.getHour()) + ":" + String.valueOf(dataHora.getMinute()));
            return grafico;
        }).collect(Collectors.toList());

        return new DashboardFuncionarioResponse(faturamentoTotal, qtdOrdensFinalizadas, qtdOrdensEmAndamento, graficos);
    }

}
