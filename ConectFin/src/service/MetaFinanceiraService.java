package service;

import java.sql.SQLException;
import java.util.List;
import model.MetaFinanceira;
import repository.MetaFinanceiraRepository;

public class MetaFinanceiraService {
    private MetaFinanceiraRepository repository;
    private String usuarioLogadoCpf;
    
    public MetaFinanceiraService(String usuarioLogadoCpf) {
        this.repository = new MetaFinanceiraRepository();
        this.usuarioLogadoCpf = usuarioLogadoCpf;
    }
    
    public void cadastrarMeta(MetaFinanceira meta) throws SQLException {
        if (meta.getMeta().compareTo(meta.getSaldoAtual()) < 0) {
            throw new IllegalArgumentException("A meta deve ser maior ou igual ao saldo atual");
        }
        meta.setUsuarioCpf(usuarioLogadoCpf);
        repository.inserirMeta(meta);
    }
    
    public void atualizarMeta(MetaFinanceira meta) throws SQLException {
        if (meta.getMeta().compareTo(meta.getSaldoAtual()) < 0) {
            throw new IllegalArgumentException("A meta deve ser maior ou igual ao saldo atual");
        }
        meta.setUsuarioCpf(usuarioLogadoCpf);
        repository.atualizarMeta(meta);
    }
    
    public void deletarMeta(int id) throws SQLException {
        repository.deletarMeta(id, usuarioLogadoCpf);
    }
    
    public List<MetaFinanceira> listarMetas() throws SQLException {
        return repository.listarMetasPorUsuario(usuarioLogadoCpf);
    }
} 