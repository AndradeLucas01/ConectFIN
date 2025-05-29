package service;

import java.sql.SQLException;
import java.util.List;
import model.FormaPagamento;
import repository.FormaPagamentoRepository;

public class FormaPagamentoService {
    private FormaPagamentoRepository repository;
    
    public FormaPagamentoService() {
        this.repository = new FormaPagamentoRepository();
    }
    
    public void cadastrarForma(FormaPagamento forma) throws SQLException {
        repository.inserirForma(forma);
    }
    
    public void atualizarForma(FormaPagamento forma) throws SQLException {
        repository.atualizarForma(forma);
    }
    
    public void deletarForma(int id) throws SQLException {
        repository.deletarForma(id);
    }
    
    public List<FormaPagamento> listarFormas() throws SQLException {
        return repository.listarFormas();
    }
} 