package service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import model.Transacao;
import repository.TransacaoRepository;
import util.Conexao;

public class TransacaoService {
    private TransacaoRepository repository;

    public TransacaoService() {
        try {
            Connection connection = Conexao.conectar();
            this.repository = new TransacaoRepository();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public void adicionarTransacao(Transacao transacao) throws SQLException {
        repository.inserirTransacao(transacao);
    }

    public void atualizarTransacao(Transacao transacao) throws SQLException {
        repository.atualizarTransacao(transacao);
    }

    public void excluirTransacao(int id, int usuarioId) throws SQLException {
        repository.excluirTransacao(id, usuarioId);
    }

    public List<Transacao> listarTransacoes(int usuarioId) throws SQLException {
        return repository.listarPorUsuario(usuarioId);
    }

    public List<Transacao> listarTodasTransacoes() throws SQLException {
        return repository.listarTodas();
    }

    public List<Transacao> listarPorPeriodo(int usuarioId, LocalDate dataInicio, LocalDate dataFim) throws SQLException {
        return repository.listarPorPeriodo(dataInicio, dataFim, usuarioId);
    }

    public List<Transacao> listarPorTipo(int usuarioId, String tipo) throws SQLException {
        return repository.listarPorTipo(tipo, usuarioId);
    }

    public BigDecimal calcularSaldo(int usuarioId) throws SQLException {
        List<Transacao> transacoes = listarTransacoes(usuarioId);
        BigDecimal saldo = BigDecimal.ZERO;

        for (Transacao transacao : transacoes) {
            if (transacao.getTipo().equals("E")) {
                saldo = saldo.add(transacao.getValor());
            } else {
                saldo = saldo.subtract(transacao.getValor());
            }
        }

        return saldo;
    }
} 