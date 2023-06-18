public class Usuario {
    private String nome;
    private String senha;

    //constutor da classe usuário
    public Usuario(String nome, String senha) {
        this.nome = nome;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha(){
        return senha;
    }
}