package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transacao {
    private int id;
    private BigDecimal valor;
    private LocalDate data;
    private String descricao;
    private int usuarioId;
    private int categoriaId;
    private int formaPagamentoId;
    private String tipo; // E para Entrada, S para Saída

    public Transacao() {
    }

    public Transacao(BigDecimal valor, LocalDate data, String descricao, int usuarioId, int categoriaId, int formaPagamentoId, String tipo) {
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.formaPagamentoId = formaPagamentoId;
        this.tipo = tipo;
    }

    public Transacao(int id, BigDecimal valor, LocalDate data, String descricao, int usuarioId, int categoriaId, int formaPagamentoId, String tipo) {
        this.id = id;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.formaPagamentoId = formaPagamentoId;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public int getFormaPagamentoId() {
        return formaPagamentoId;
    }

    public void setFormaPagamentoId(int formaPagamentoId) {
        this.formaPagamentoId = formaPagamentoId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Transacao{" +
                "id=" + id +
                ", valor=" + valor +
                ", data=" + data +
                ", descricao='" + descricao + '\'' +
                ", usuarioId=" + usuarioId +
                ", categoriaId=" + categoriaId +
                ", formaPagamentoId=" + formaPagamentoId +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
