package repository;

import model.Categoria;
import java.util.List;
import java.sql.SQLException;

public interface InterfaceCategoriaRepository {
    Categoria inserirCategoria(Categoria categoria) throws SQLException;
    Categoria atualizarCategoria(Categoria categoria) throws SQLException;
    void excluirCategoria(int id) throws SQLException;
    Categoria buscarPorId(int id) throws SQLException;
    List<Categoria> listarCategorias() throws SQLException;
} 