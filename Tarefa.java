import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;


public class Tarefa {
    private int id;
    private String titulo;
    private String descricao;
    private String categoria;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;
    private boolean status;
    private List<Tarefa> subtarefas;
    
    //construtor da classe Tarefa
    public Tarefa(int id, String titulo, String descricao, LocalDateTime dataCriacao, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.status = false;
        this.categoria = categoria;
        this.subtarefas = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Titulo: ").append(titulo).append("\n");
        builder.append("Descrição: ").append(descricao).append("\n");
        builder.append("Categoria: ").append(categoria).append("\n");
        builder.append("Data de Criação: ").append(dataCriacao).append("\n");
        builder.append("Concluída? ").append(status).append("\n");
        return builder.toString();
    }
        
    //respectivos getters and setters
    public List<Tarefa> getSubtarefas() {
        return subtarefas;
    }

    public void adicionarSubtarefa(Tarefa subtarefa) {
        subtarefas.add(subtarefa);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoria(){
        return this.categoria;
    }

    public void setCategoria(String categoria){
        this.categoria=categoria;
    }
}