import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.io.Console;

public class GerenciadorTarefas {
    private ArrayList<Tarefa> tarefasPendentes;
    private ArrayList<Tarefa> tarefasConcluidas;

    public GerenciadorTarefas() {
        this.tarefasPendentes = new ArrayList<>();
        this.tarefasConcluidas = new ArrayList<>();
    }

    public void adicionarTarefa() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o título de sua tarefa: ");
        String titulo = scanner.nextLine();
        System.out.println("Digite a descrição de sua tarefa: ");
        String descricao = scanner.nextLine();
        // adiciona a data atual como data de criação
        LocalDateTime dataCriacao = LocalDateTime.now();
        Tarefa novaTarefa = new Tarefa(titulo, descricao, dataCriacao);
        this.tarefasPendentes.add(novaTarefa);
        System.out.println("Tarefa Criada com sucesso!");
        // scanner.nextLine();

    }

    public void concluirTarefa() {
        Scanner scanner = new Scanner(System.in);
        // Verifica se há tarefas pendentes
        if (this.tarefasPendentes.isEmpty()) {
            System.out.println("Não há tarefas pendentes para concluir.");
            return;
        }

        // Exibe todas as tarefas pendentes com seus títulos
        System.out.println("Tarefas Pendentes:");
        for (Tarefa tarefa : this.tarefasPendentes) {
            System.out.println(tarefa.getTitulo());
        }

        // Pergunta ao usuário qual tarefa ele deseja concluir
        System.out.println("Digite o título da tarefa que deseja concluir: ");
        String tituloTarefa = scanner.nextLine();
        // scanner.nextLine();

        // Procura a tarefa com o título fornecido pelo usuário na lista de tarefas pendentes

        for (Tarefa tarefa : this.tarefasPendentes) {
            if (tarefa.getTitulo().equalsIgnoreCase(tituloTarefa)) {
                // Define a data de conclusão da tarefa e move da lista de tarefas pendentes para a lista de tarefas concluídas
                tarefa.setDataConclusao(LocalDateTime.now());
                
                // Remove a tarefa da lista de tarefas pendentes e adiciona à lista de tarefas concluídas
                this.tarefasPendentes.remove(tarefa);
                this.tarefasConcluidas.add(tarefa);
                System.out.println(
                        "Tarefa \"" + tituloTarefa + "\" concluída em " + tarefa.getDataConclusao() + " com sucesso!");
                System.out.println("Pressione ENTER para voltar ao Menu.");
                String entrada = scanner.nextLine();
                return;
                
            }
            // Se nenhuma tarefa com o título fornecido foi encontrada, exibe uma mensagem de erro
            // else{
            // System.out.println("Tarefa \"" + tituloTarefa + "\" não encontrada.");
            // }
        }

    }

    public void exibirTarefasPendentes() {
        System.out.println("Tarefas pendentes:");
        for (Tarefa tarefa : tarefasPendentes) {
            System.out.println("\n===========\n" + "Titulo: " + tarefa.getTitulo() + "\n" + "Descrição: "
                    + tarefa.getDescricao() + "\nData de Criação: " + tarefa.getDataCriacao() + "\n===========\n");
        }
    }

    public void exibirTarefasConcluidas() {
        System.out.println("Tarefas concluídas:");
        for (Tarefa tarefa : tarefasConcluidas) {
            System.out.println("\n===========\n" + "Titulo: " + tarefa.getTitulo() + "\n" + "Descrição: "
                    + tarefa.getDescricao() + "\nData de Conclusão: " + tarefa.getDataConclusao() + "\n===========\n");
        }
    }

    public void limparConsole() {
        Console console = System.console();
        if (console != null) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}