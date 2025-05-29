package repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import model.Transacao;

public interface InterfaceTransacaoRepository {
    Transacao inserirTransacao(Transacao transacao) throws SQLException;
    Transacao atualizarTransacao(Transacao transacao) throws SQLException;
    void excluirTransacao(int id, int usuarioId) throws SQLException;
    Transacao buscarPorId(int id, int usuarioId) throws SQLException;
    List<Transacao> listarPorUsuario(int usuarioId) throws SQLException;
    List<Transacao> listarTodas() throws SQLException;
    List<Transacao> listarPorPeriodo(LocalDate inicio, LocalDate fim, Integer usuarioId) throws SQLException;
    List<Transacao> listarPorTipo(String tipo, Integer usuarioId) throws SQLException;
} 