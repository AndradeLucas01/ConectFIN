package model;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String papel;
    private String cpf;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha, String papel, String cpf) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.papel = papel;
        this.cpf = cpf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", papel='" + papel + '\'' +
                ", cpf='" + cpf + '\'' +
                '}';
    }
}
