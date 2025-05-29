package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.Categoria;
import repository.CategoriaRepository;
import util.DatabaseConnection;

public class CategoriaService {
    private CategoriaRepository categoriaRepository;

    public CategoriaService() {
        try {
            Connection conexao = DatabaseConnection.getConnection();
            this.categoriaRepository = new CategoriaRepository(conexao);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    public void cadastrarCategoria(Categoria categoria) {
        try {
            categoriaRepository.inserirCategoria(categoria);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar categoria: " + e.getMessage());
        }
    }

    public List<Categoria> listarCategorias() {
        try {
            return categoriaRepository.listarCategorias();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar categorias: " + e.getMessage());
        }
    }

    public void atualizarCategoria(Categoria categoria) {
        try {
            categoriaRepository.atualizarCategoria(categoria);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar categoria: " + e.getMessage());
        }
    }

    public void excluirCategoria(int id) {
        try {
            categoriaRepository.excluirCategoria(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir categoria: " + e.getMessage());
        }
    }
} 