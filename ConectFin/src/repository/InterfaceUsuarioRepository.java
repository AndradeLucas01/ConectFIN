package repository;

import model.Usuario;
import java.util.List;
import java.sql.SQLException;

public interface InterfaceUsuarioRepository {
    Usuario inserirUsuario(Usuario usuario) throws SQLException;
    Usuario atualizarUsuario(Usuario usuario) throws SQLException;
    void excluirUsuario(int id) throws SQLException;
    Usuario buscarPorId(int id) throws SQLException;
    Usuario buscarPorEmail(String email) throws SQLException;
    Usuario buscarPorEmailOuCpf(String identificador, String senha) throws SQLException;
    List<Usuario> listarUsuarios() throws SQLException;
} 