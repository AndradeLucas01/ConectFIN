package service;

import java.sql.SQLException;
import java.util.List;
import model.Usuario;
import repository.UsuarioRepository;

public class UsuarioService {
    private UsuarioRepository repository;

    public UsuarioService() {
        this.repository = new UsuarioRepository();
    }

    public void cadastrarUsuario(Usuario usuario) throws SQLException {
        repository.inserirUsuario(usuario);
    }

    public void atualizarUsuario(Usuario usuario) throws SQLException {
        repository.atualizarUsuario(usuario);
    }

    public void excluirUsuario(int id) throws SQLException {
        repository.excluirUsuario(id);
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        return repository.listarUsuarios();
    }

    public Usuario login(String identificador, String senha) throws SQLException {
        return repository.buscarPorIdentificador(identificador, senha);
    }

    public Usuario buscarUsuarioPorId(int id) throws SQLException {
        return repository.buscarPorId(id);
    }

    public Usuario buscarUsuarioPorEmail(String email) throws SQLException {
        return repository.buscarPorEmail(email);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }
        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }
        if (usuario.getPapel() == null || usuario.getPapel().trim().isEmpty()) {
            throw new IllegalArgumentException("Papel não pode ser vazio");
        }
        if (usuario.getCpf() == null || usuario.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }
    }
} 