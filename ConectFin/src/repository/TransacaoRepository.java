package repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Transacao;

public class TransacaoRepository implements InterfaceTransacaoRepository {
    private Connection connection;

    public TransacaoRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Transacao inserirTransacao(Transacao transacao) throws SQLException {
        String sql = "INSERT INTO transacao (valor, data, descricao, usuario_id, categoria_id, tipo) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setBigDecimal(1, transacao.getValor());
            stmt.setDate(2, Date.valueOf(transacao.getData()));
            stmt.setString(3, transacao.getDescricao());
            stmt.setInt(4, transacao.getUsuarioId());
            stmt.setInt(5, transacao.getCategoriaId());
            stmt.setString(6, transacao.getTipo());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir transação, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transacao.setId(generatedKeys.getInt(1));
                    return transacao;
                } else {
                    throw new SQLException("Falha ao inserir transação, nenhum ID obtido.");
                }
            }
        }
    }

    @Override
    public Transacao atualizarTransacao(Transacao transacao) throws SQLException {
        String sql = "UPDATE transacao SET valor = ?, data = ?, descricao = ?, categoria_id = ?, tipo = ? WHERE id = ? AND usuario_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, transacao.getValor());
            stmt.setDate(2, Date.valueOf(transacao.getData()));
            stmt.setString(3, transacao.getDescricao());
            stmt.setInt(4, transacao.getCategoriaId());
            stmt.setString(5, transacao.getTipo());
            stmt.setInt(6, transacao.getId());
            stmt.setInt(7, transacao.getUsuarioId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao atualizar transação, nenhuma linha afetada.");
            }
            return transacao;
        }
    }

    @Override
    public void excluirTransacao(int id, int usuarioId) throws SQLException {
        String sql = "DELETE FROM transacao WHERE id = ? AND usuario_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, usuarioId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao excluir transação, nenhuma linha afetada.");
            }
        }
    }

    @Override
    public Transacao buscarPorId(int id, int usuarioId) throws SQLException {
        String sql = "SELECT * FROM transacao WHERE id = ? AND usuario_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, usuarioId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearTransacao(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Transacao> listarPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM transacao WHERE usuario_id = ? ORDER BY data DESC";
        List<Transacao> transacoes = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transacoes.add(mapearTransacao(rs));
                }
            }
        }
        return transacoes;
    }

    @Override
    public List<Transacao> listarTodas() throws SQLException {
        String sql = "SELECT * FROM transacao ORDER BY data DESC";
        List<Transacao> transacoes = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                transacoes.add(mapearTransacao(rs));
            }
        }
        return transacoes;
    }

    @Override
    public List<Transacao> listarPorPeriodo(LocalDate inicio, LocalDate fim, Integer usuarioId) throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT t.*, c.nome as categoria_nome, c.tipo as categoria_tipo " +
                     "FROM transacao t " +
                     "LEFT JOIN categoria c ON t.categoria_id = c.id " +
                     "WHERE t.data BETWEEN ? AND ? " +
                     "AND (? IS NULL OR t.usuario_id = ?) " +
                     "ORDER BY t.data DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(inicio));
            stmt.setDate(2, java.sql.Date.valueOf(fim));
            stmt.setObject(3, usuarioId);
            stmt.setObject(4, usuarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transacoes.add(mapearTransacao(rs));
                }
            }
        }
        return transacoes;
    }

    @Override
    public List<Transacao> listarPorTipo(String tipo, Integer usuarioId) throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT t.*, c.nome as categoria_nome, c.tipo as categoria_tipo " +
                     "FROM transacao t " +
                     "LEFT JOIN categoria c ON t.categoria_id = c.id " +
                     "WHERE t.tipo = ? " +
                     "AND (? IS NULL OR t.usuario_id = ?) " +
                     "ORDER BY t.data DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipo);
            stmt.setObject(2, usuarioId);
            stmt.setObject(3, usuarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transacoes.add(mapearTransacao(rs));
                }
            }
        }
        return transacoes;
    }

    private Transacao mapearTransacao(ResultSet rs) throws SQLException {
        return new Transacao(
            rs.getInt("id"),
            rs.getBigDecimal("valor"),
            rs.getDate("data").toLocalDate(),
            rs.getString("descricao"),
            rs.getInt("usuario_id"),
            rs.getInt("categoria_id"),
            rs.getString("tipo")
        );
    }
} 