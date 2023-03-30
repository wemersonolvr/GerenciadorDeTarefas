import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.io.Console;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GerenciadorTarefas {
    private ArrayList<Tarefa> tarefasPendentes;
    private ArrayList<Tarefa> tarefasConcluidas;

    public GerenciadorTarefas() {
        this.tarefasPendentes = new ArrayList<>();
        this.tarefasConcluidas = new ArrayList<>();
    }

    public void adicionarTarefa() {
    int id=1;
    for (Tarefa tarefa : this.tarefasPendentes) {
        if (tarefa.getId() >= id) {
            id = tarefa.getId() + 1;
        }
    }
    for (Tarefa tarefa : this.tarefasConcluidas) {
        if (tarefa.getId() >= id) {
            id = tarefa.getId() + 1;
        }
    }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o título de sua tarefa: ");
        String titulo = scanner.nextLine();
        System.out.println("Digite a descrição de sua tarefa: ");
        String descricao = scanner.nextLine();
        // adiciona a data atual como data de criação
        LocalDateTime dataCriacao = LocalDateTime.now();
        Tarefa novaTarefa = new Tarefa(id, titulo, descricao, dataCriacao);
        this.tarefasPendentes.add(novaTarefa);
        System.out.println("Tarefa Criada com sucesso!");
        System.out.println("Pressione ENTER para voltar ao Menu.");
        String entrada = scanner.nextLine();
        // scanner.nextLine();
        scanner.close();
    }

    public void concluirTarefa() {
        Scanner scanner = new Scanner(System.in);
        // Verifica se há tarefas pendentes
        if (this.tarefasPendentes.isEmpty()) {
            System.out.println("Não há tarefas pendentes para concluir.");
            System.out.println("Pressione ENTER para voltar ao Menu.");
            String entrada = scanner.nextLine();
            return;
        }

        // Exibe todas as tarefas pendentes com seus títulos
        else{
        System.out.println("Tarefas Pendentes:");
         
         for (Tarefa tarefa : this.tarefasPendentes) {
            System.out.println(tarefa.getId() + " - " + tarefa.getTitulo() + "\n Descrição: " + tarefa.getDescricao());
}

        System.out.println("Digite o ID da tarefa que deseja concluir: ");
        int idTarefa = scanner.nextInt();
        scanner.nextLine();        
        // scanner.nextLine();

        // Procura a tarefa com o título fornecido pelo usuário na lista de tarefas pendentes

        for (Tarefa tarefa : this.tarefasPendentes) {
            if (tarefa.getId() == idTarefa) {                
            // Define a data de conclusão da tarefa e move da lista de tarefas pendentes para a lista de tarefas concluídas
                tarefa.setDataConclusao(LocalDateTime.now());
                //define o status da tarefa para true.
                tarefa.setStatus(true);
                // Remove a tarefa da lista de tarefas pendentes e adiciona à lista de tarefas concluídas.
                this.tarefasPendentes.remove(tarefa);
                this.tarefasConcluidas.add(tarefa);
                System.out.println("Tarefa \"" + tarefa.getTitulo() + "\" concluída em " + tarefa.getDataConclusao() + " com sucesso!");
                System.out.println("Pressione ENTER para voltar ao Menu.");
                String entrada = scanner.nextLine();
                return;
                
            }
          }
            // Se nenhuma tarefa com o título fornecido foi encontrada, exibe uma mensagem de erro
            // else{
            // System.out.println("Tarefa \"" + tituloTarefa + "\" não encontrada.");
            // }
        }
        scanner.close();

    }

    public void exibirTarefasPendentes() {
        Scanner scanner = new Scanner(System.in);
           if(!this.tarefasPendentes.isEmpty()){
                for (Tarefa tarefa : tarefasPendentes) {
                         System.out.println("\n===========\n" + "ID: " + tarefa.getId() + "\nTitulo: " + tarefa.getTitulo() + "\n" + "Descrição: "+ tarefa.getDescricao() + "\nData de Criação: " + tarefa.getDataCriacao() + "\nconcluída? " + tarefa.isStatus() + "\n===========\n");
                                        
        }
                         System.out.println("Pressione ENTER para voltar ao Menu.");
                         String entrada = scanner.nextLine();
    }
                       else{
                       System.out.println("Não há tarefas pendentes.");
                       System.out.println("Pressione ENTER para voltar ao Menu.");
                       String entrada = scanner.nextLine();
                       return;
        }
      scanner.close();
  }

    public void exibirTarefasConcluidas() {
        Scanner scanner = new Scanner(System.in);
        if(!this.tarefasConcluidas.isEmpty()){
        System.out.println("Tarefas concluídas:");
        for (Tarefa tarefa : tarefasConcluidas) {
            System.out.println("\n===========\n" + "Titulo: " + tarefa.getTitulo() + "\n" + "Descrição: " + tarefa.getDescricao() + "\nData de Conclusão: " + tarefa.getDataConclusao() + "\nconcluída? " + tarefa.isStatus() + "\n===========\n");
        }
    }
          else{
              System.out.println("Não há tarefas Concluídas.");
              System.out.println("Pressione ENTER para voltar ao Menu.");
              String entrada = scanner.nextLine();
              return; 
   }
             scanner.close();
}
    public void limparConsole() {
        Console console = System.console();
        if (console != null) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
      public void salvarTarefas(String nomeUsuario) {
            String nomeArquivo = nomeUsuario + ".txt"; // Altera o nome do arquivo para incluir o nome do usuário
            File arquivo = new File(nomeArquivo);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            // Escreve as tarefas pendentes no arquivo
            for (Tarefa tarefa : this.tarefasPendentes) {
                writer.write("PENDENTE;");
                writer.write(tarefa.getId() + ";");
                writer.write(tarefa.getTitulo() + ";");
                writer.write(tarefa.getDescricao() + ";");
                writer.write(tarefa.isStatus() + ";");
                writer.write(tarefa.getDataCriacao().toString() + ";");
                writer.write("\n");
            }
            // Escreve as tarefas concluídas no arquivo
            for (Tarefa tarefa : this.tarefasConcluidas) {
                writer.write("CONCLUIDA;");
                writer.write(tarefa.getTitulo() + ";");
                writer.write(tarefa.getDescricao() + ";");
                writer.write(tarefa.getDataCriacao().toString() + ";");
                writer.write(tarefa.getDataConclusao().toString() + ";");
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar as tarefas no arquivo " + nomeArquivo + ": " + e.getMessage());
        }
    }
    
   public String CadastrarUsuario(){
    Scanner scanner = new Scanner(System.in);
    System.out.println("Olá! Seja bem-vindo ao seu gerenciador de tarefas.\nQual o seu nome?");
    String nomeUsuario = scanner.nextLine();
    scanner.close();
    
    File arquivo = new File(nomeUsuario + ".txt"); // Cria um objeto File com o nome do arquivo
    if (!arquivo.exists()) { // Verifica se o arquivo não existe
        try {
            arquivo.createNewFile(); // Cria um novo arquivo
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    } else {
        System.out.println("Usuário já cadastrado.");
    }
    return nomeUsuario;
   }
}
/*public void carregarTarefas(String nomeArquivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] campos = linha.split(";");
                String status = campos[0];
                String titulo = campos[1];
                String descricao = campos[2];
                LocalDateTime dataCriacao = LocalDateTime.parse(campos[3]);
                if (status.equals("PENDENTE")) {
                    Tarefa carregartarefa = new Tarefa(titulo, descricao, dataCriacao);
                    this.tarefasPendentes.add(tarefa);
                } else if (status.equals("CONCLUIDA")) {
                    LocalDateTime dataConclusao = LocalDateTime.parse(campos[4]);
                    Tarefa carregartarefa = new Tarefa(titulo, descricao, dataCriacao);
                    tarefa.setDataConclusao(dataConclusao);
                    this.tarefasConcluidas.add(tarefa);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar as tarefas do arquivo " + nomeArquivo + ": " + e.getMessage());
        }
    }
   
         public void exibirTarefas(String nomePessoa) {
    // Verifica se já existe um arquivo com o nome da pessoa
    File arquivo = new File(nomePessoa + ".txt");
    if (arquivo.exists()) {
        // Carrega as tarefas do arquivo
        carregarTarefas(nomePessoa + ".txt");
        // Exibe as tarefas pendentes
        System.out.println("Tarefas pendentes:");
        for (Tarefa tarefa : this.tarefasPendentes) {
            System.out.println(tarefa);
        }
        // Exibe as tarefas concluídas
        System.out.println("Tarefas concluídas:");
        for (Tarefa tarefa : this.tarefasConcluidas) {
            System.out.println(tarefa);
        }
    } else {
        System.out.println("Não existem tarefas registradas para " + nomePessoa);
    }
}
}
*/