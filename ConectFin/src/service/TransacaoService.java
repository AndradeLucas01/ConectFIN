package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import model.Transacao;
import repository.TransacaoRepository;
import util.DatabaseConnection;

public class TransacaoService {
    private TransacaoRepository transacaoRepository;

    public TransacaoService() {
        try {
            Connection conexao = DatabaseConnection.getConnection();
            this.transacaoRepository = new TransacaoRepository(conexao);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    public Transacao cadastrarTransacao(Transacao transacao) throws SQLException {
        validarTipoTransacao(transacao.getTipo());
        return transacaoRepository.inserirTransacao(transacao);
    }

    public List<Transacao> listarTransacoes(int usuarioId) throws SQLException {
        return transacaoRepository.listarPorUsuario(usuarioId);
    }

    public Transacao atualizarTransacao(Transacao transacao) throws SQLException {
        validarTipoTransacao(transacao.getTipo());
        return transacaoRepository.atualizarTransacao(transacao);
    }

    public void excluirTransacao(int id, int usuarioId) throws SQLException {
        transacaoRepository.excluirTransacao(id, usuarioId);
    }

    public Transacao buscarTransacao(int id, int usuarioId) throws SQLException {
        return transacaoRepository.buscarPorId(id, usuarioId);
    }

    public List<Transacao> listarTodasTransacoes(String papelUsuario) throws SQLException {
        if (!"ADMIN".equalsIgnoreCase(papelUsuario)) {
            throw new RuntimeException("Acesso negado: apenas administradores podem listar todas as transações.");
        }
        return transacaoRepository.listarTodas();
    }

    public List<Transacao> listarPorPeriodo(LocalDate inicio, LocalDate fim, Integer usuarioId, String papelUsuario) throws SQLException {
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data inicial maior que a final.");
        }

        if ("ADMIN".equalsIgnoreCase(papelUsuario) && usuarioId == null) {
            return transacaoRepository.listarPorPeriodo(inicio, fim, null);
        } else {
            return transacaoRepository.listarPorPeriodo(inicio, fim, usuarioId);
        }
    }

    public List<Transacao> listarPorTipo(String tipo, Integer usuarioId, String papelUsuario) throws SQLException {
        if (!tipo.equals("E") && !tipo.equals("S")) {
            throw new IllegalArgumentException("Tipo inválido. Use 'E' para Entrada ou 'S' para Saída.");
        }

        if ("ADMIN".equalsIgnoreCase(papelUsuario) && usuarioId == null) {
            return transacaoRepository.listarPorTipo(tipo, null);
        } else {
            return transacaoRepository.listarPorTipo(tipo, usuarioId);
        }
    }

    private void validarTipoTransacao(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo inválido. Use 'E' para Entrada ou 'S' para Saída.");
        }
        
        String tipoNormalizado = tipo.trim().toUpperCase();
        if (!tipoNormalizado.equals("E") && !tipoNormalizado.equals("S")) {
            throw new IllegalArgumentException("Tipo inválido. Use 'E' para Entrada ou 'S' para Saída.");
        }
    }
} 