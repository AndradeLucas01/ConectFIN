package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;

public class UsuarioRepository implements InterfaceUsuarioRepository {
    private Connection conexao;

    public UsuarioRepository(Connection conexao) {
        this.conexao = conexao;
    }

    @Override
    public Usuario inserirUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, papel, cpf) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getPapel());
            stmt.setString(5, usuario.getCpf());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }
            return usuario;
        }
    }

    @Override
    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        }
        
        return null;
    }

    @Override
    public Usuario buscarPorEmailOuCpf(String identificador, String senha) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE (email = ? OR cpf = ?) AND senha = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, identificador);
            stmt.setString(2, identificador);
            stmt.setString(3, senha);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        }
        
        return null;
    }

    @Override
    public List<Usuario> listarUsuarios() throws SQLException {
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }
        
        return usuarios;
    }

    @Override
    public Usuario atualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, papel = ?, cpf = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getPapel());
            stmt.setString(5, usuario.getCpf());
            stmt.setInt(6, usuario.getId());
            stmt.executeUpdate();
            return usuario;
        }
    }

    @Override
    public void excluirUsuario(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        }
        
        return null;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setPapel(rs.getString("papel"));
        usuario.setCpf(rs.getString("cpf"));
        return usuario;
    }
} 