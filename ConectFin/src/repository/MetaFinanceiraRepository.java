package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.MetaFinanceira;
import util.Conexao;

public class MetaFinanceiraRepository {
    
    public void inserirMeta(MetaFinanceira meta) throws SQLException {
        String sql = "INSERT INTO meta_financeira (saldo_atual, meta, data_prevista, usuario_cpf) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, meta.getSaldoAtual());
            stmt.setBigDecimal(2, meta.getMeta());
            stmt.setDate(3, java.sql.Date.valueOf(meta.getDataPrevista()));
            stmt.setString(4, meta.getUsuarioCpf());
            
            stmt.executeUpdate();
        }
    }
    
    public void atualizarMeta(MetaFinanceira meta) throws SQLException {
        String sql = "UPDATE meta_financeira SET saldo_atual = ?, meta = ?, data_prevista = ? WHERE id = ? AND usuario_cpf = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, meta.getSaldoAtual());
            stmt.setBigDecimal(2, meta.getMeta());
            stmt.setDate(3, java.sql.Date.valueOf(meta.getDataPrevista()));
            stmt.setInt(4, meta.getId());
            stmt.setString(5, meta.getUsuarioCpf());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Meta não encontrada ou você não tem permissão para editá-la");
            }
        }
    }
    
    public void deletarMeta(int id, String usuarioCpf) throws SQLException {
        String sql = "DELETE FROM meta_financeira WHERE id = ? AND usuario_cpf = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.setString(2, usuarioCpf);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Meta não encontrada ou você não tem permissão para excluí-la");
            }
        }
    }
    
    public List<MetaFinanceira> listarMetasPorUsuario(String cpf) throws SQLException {
        String sql = "SELECT * FROM meta_financeira WHERE usuario_cpf = ?";
        List<MetaFinanceira> metas = new ArrayList<>();
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MetaFinanceira meta = new MetaFinanceira();
                    meta.setId(rs.getInt("id"));
                    meta.setSaldoAtual(rs.getBigDecimal("saldo_atual"));
                    meta.setMeta(rs.getBigDecimal("meta"));
                    meta.setDataPrevista(rs.getDate("data_prevista").toLocalDate());
                    meta.setUsuarioCpf(rs.getString("usuario_cpf"));
                    metas.add(meta);
                }
            }
        }
        
        return metas;
    }
} 