import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class GerenciadorTarefas {
    private ArrayList<Tarefa> tarefasPendentes;
    private ArrayList<Tarefa> tarefasConcluidas;

    public GerenciadorTarefas() {
        this.tarefasPendentes = new ArrayList<>();
        this.tarefasConcluidas = new ArrayList<>();
    }

    public void adicionarTarefa(Tarefa t) {
        this.tarefasPendentes.add(t);
    }

    public void concluirTarefa(Tarefa t) {
        t.setDataConclusao(new Date());
        this.tarefasPendentes.remove(t);
        this.tarefasConcluidas.add(t);
    }

    public void exibirTarefasPendentes() {
        System.out.println("Tarefas pendentes:");
        for (Tarefa t : tarefasPendentes) {
            System.out.println(t.getTitulo());
        }
    }

    public void exibirTarefasConcluidas() {
        System.out.println("Tarefas concluídas:");
        for (Tarefa t : tarefasConcluidas) {
            System.out.println(t.getTitulo());
        }
    }
}
