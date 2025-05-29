package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.Usuario;
import repository.UsuarioRepository;
import util.DatabaseConnection;

public class UsuarioService {
    private UsuarioRepository usuarioRepository;

    public UsuarioService() {
        try {
            Connection conexao = DatabaseConnection.getConnection();
            this.usuarioRepository = new UsuarioRepository(conexao);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    public void cadastrarUsuario(Usuario usuario) {
        try {
            // Validar CPF
            if (usuario.getCpf() == null || !usuario.getCpf().matches("\\d{11}")) {
                throw new RuntimeException("CPF deve conter 11 dígitos numéricos.");
            }

            // Verificar se CPF já existe
            Usuario usuarioExistente = usuarioRepository.buscarPorEmailOuCpf(usuario.getCpf(), "senhaDummy");
            if (usuarioExistente != null) {
                throw new RuntimeException("CPF já cadastrado.");
            }

            usuarioRepository.inserirUsuario(usuario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public Usuario login(String identificador, String senha) {
        if (identificador == null || identificador.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            throw new RuntimeException("Identificador e senha obrigatórios.");
        }

        try {
            Usuario usuario = usuarioRepository.buscarPorEmailOuCpf(identificador, senha);
            if (usuario == null) {
                throw new RuntimeException("Credenciais inválidas.");
            }
            return usuario;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao realizar login: " + e.getMessage());
        }
    }

    public Usuario buscarPorEmail(String email) {
        try {
            return usuarioRepository.buscarPorEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email: " + e.getMessage());
        }
    }

    public List<Usuario> listarUsuarios() {
        try {
            return usuarioRepository.listarUsuarios();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage());
        }
    }

    public void atualizarUsuario(Usuario usuario) {
        try {
            usuarioRepository.atualizarUsuario(usuario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public void excluirUsuario(int id) {
        try {
            usuarioRepository.excluirUsuario(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir usuário: " + e.getMessage());
        }
    }

    public Usuario buscarUsuarioPorId(int id) throws SQLException {
        return usuarioRepository.buscarPorId(id);
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