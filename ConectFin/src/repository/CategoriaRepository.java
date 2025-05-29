package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Categoria;

public class CategoriaRepository implements InterfaceCategoriaRepository {
    private Connection conexao;

    public CategoriaRepository(Connection conexao) {
        this.conexao = conexao;
    }

    @Override
    public Categoria inserirCategoria(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (nome, tipo) VALUES (?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getTipo());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId(rs.getInt(1));
                }
            }
            return categoria;
        }
    }

    @Override
    public Categoria atualizarCategoria(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET nome = ?, tipo = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getTipo());
            stmt.setInt(3, categoria.getId());
            stmt.executeUpdate();
            return categoria;
        }
    }

    @Override
    public void excluirCategoria(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Categoria buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearCategoria(rs);
            }
        }
        
        return null;
    }

    @Override
    public List<Categoria> listarCategorias() throws SQLException {
        String sql = "SELECT * FROM categoria";
        List<Categoria> categorias = new ArrayList<>();
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categorias.add(mapearCategoria(rs));
            }
        }
        
        return categorias;
    }

    private Categoria mapearCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("id"));
        categoria.setNome(rs.getString("nome"));
        categoria.setTipo(rs.getString("tipo"));
        return categoria;
    }
} 