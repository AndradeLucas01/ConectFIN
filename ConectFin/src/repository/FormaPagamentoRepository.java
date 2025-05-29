package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.FormaPagamento;
import util.Conexao;

public class FormaPagamentoRepository {
    
    public void inserirForma(FormaPagamento forma) throws SQLException {
        String sql = "INSERT INTO forma_pagamento (formato) VALUES (?)";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, forma.getFormato());
            stmt.executeUpdate();
        }
    }
    
    public void atualizarForma(FormaPagamento forma) throws SQLException {
        String sql = "UPDATE forma_pagamento SET formato = ? WHERE id = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, forma.getFormato());
            stmt.setInt(2, forma.getId());
            stmt.executeUpdate();
        }
    }
    
    public void deletarForma(int id) throws SQLException {
        String sql = "DELETE FROM forma_pagamento WHERE id = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public List<FormaPagamento> listarFormas() throws SQLException {
        String sql = "SELECT * FROM forma_pagamento";
        List<FormaPagamento> formas = new ArrayList<>();
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                FormaPagamento forma = new FormaPagamento();
                forma.setId(rs.getInt("id"));
                forma.setFormato(rs.getString("formato"));
                formas.add(forma);
            }
        }
        
        return formas;
    }
} 