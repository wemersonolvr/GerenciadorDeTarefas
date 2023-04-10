import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.File;

public class GerenciadorTarefas {
    private ArrayList<Tarefa> tarefasPendentes;
    private ArrayList<Tarefa> tarefasConcluidas;

    // construtor
    public GerenciadorTarefas() {
        this.tarefasPendentes = new ArrayList<>();
        this.tarefasConcluidas = new ArrayList<>();
    }

    // método do gerenciador de tarefas em si, onde o usuário realiza as tarefas e
    // conclui as tarefas
    public void iniciarGerenciador() {
        int opcao = 0;
        Scanner scanner = new Scanner(System.in);
        criarPasta();
        String nomeUsuario = cadastrarUsuario();
        String nomeArquivo = nomeUsuario + ".txt";
        try {
            carregarTarefas(nomeArquivo);
        } catch (IOException e) {
            System.out.println("");
        }

        while (opcao != 5) {
            System.out.println("=== Menu ===");
            System.out.println("1. Criar nova tarefa");
            System.out.println("2. Concluir tarefa");
            System.out.println("3. Exibir tarefas pendentes");
            System.out.println("4. Exibir tarefas concluídas");
            System.out.println("5. Sair e salvar");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Opção inválida. Por favor, digite um número inteiro.");
                scanner.next(); // limpa o buffer do scanner
                continue;
            }

            switch (opcao) {
                case 1:
                    adicionarTarefa();

                    break;
                case 2:
                    concluirTarefa();
                    break;
                case 3:
                    exibirTarefasPendentes();
                    break;
                case 4:
                    exibirTarefasConcluidas();
                    break;
                case 5:
                    System.out.println("Salvando tarefas...");
                    salvarTarefas(nomeArquivo);
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }

        scanner.close();
    }

    // método para adicionar Tarefas
    public void adicionarTarefa() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o título de sua tarefa: ");
        String titulo = scanner.nextLine();
        System.out.println("Digite a descrição de sua tarefa: ");
        String descricao = scanner.nextLine();
        // adiciona a data atual como data de criação
        LocalDateTime dataCriacao = LocalDateTime.now();
        int id = proximoId();
        Tarefa novaTarefa = new Tarefa(id, titulo, descricao, dataCriacao);
        this.tarefasPendentes.add(novaTarefa);
        System.out.println("Tarefa Criada com sucesso!");
    }

    // método para concluir as Tarefas
    public void concluirTarefa() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Tarefas Pendentes:");

        for (Tarefa tarefa : this.tarefasPendentes) {
            System.out.println(tarefa.getId() + " - " + tarefa.getTitulo() + "\nDescrição: " + tarefa.getDescricao());
        }
        System.out.println("Digite o ID da tarefa que deseja concluir: ");
        int id = scanner.nextInt();
        boolean tarefaEncontrada = false;
        for (Tarefa tarefa : this.tarefasPendentes) {
            if (tarefa.getId() == id) {
                tarefa.setStatus(true);
                tarefa.setDataConclusao(LocalDateTime.now());
                this.tarefasConcluidas.add(tarefa);
                this.tarefasPendentes.remove(tarefa);
                tarefaEncontrada = true;
                System.out.println("Tarefa concluída com sucesso!");
                break;
            }
        }

        if (!tarefaEncontrada) {
            System.out.println("Tarefa não encontrada.");
        }
    }

    // método para a exibição das tarefas pendentes
    public void exibirTarefasPendentes() {
        if (this.tarefasPendentes.isEmpty()) {
            System.out.println("Não há tarefas pendentes.");
            return;
        }
        System.out.println("=== Tarefas pendentes ===");
        for (Tarefa tarefa : this.tarefasPendentes) {
            System.out.println("\n===========\n" + "Titulo: " + tarefa.getTitulo() + "\n" + "Descrição: "
                    + tarefa.getDescricao() + "\nData de Criação: " + tarefa.getDataCriacao() + "\nconcluída? "
                    + tarefa.isStatus() + "\n===========\n");
        }
    }

    // método para a exibição das tarefas concluídas
    public void exibirTarefasConcluidas() {
        if (this.tarefasConcluidas.isEmpty()) {
            System.out.println("Não há tarefas concluídas.");
            return;
        }
        System.out.println("=== Tarefas concluídas ===");
        for (Tarefa tarefa : this.tarefasConcluidas) {
            System.out.println("\n===========\n" + "Titulo: " + tarefa.getTitulo() + "\n" + "Descrição: "
                    + tarefa.getDescricao() + "\nData de Conclusão: " + tarefa.getDataConclusao() + "\nconcluída? "
                    + tarefa.isStatus() + "\n===========\n");
        }
    }

    // método para salvar as tarefas do usuário em arquivos
    public void salvarTarefas(String nomeArquivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("usuarios/" + nomeArquivo))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            for (Tarefa tarefa : this.tarefasPendentes) {
                writer.printf("P|%d|%s|%s|%s|%b%n", tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(),
                        tarefa.getDataCriacao().format(formatter), tarefa.isStatus());
            }
            for (Tarefa tarefa : this.tarefasConcluidas) {
                writer.printf("C|%d|%s|%s|%s|%b|%s%n", tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(),
                        tarefa.getDataCriacao().format(formatter), tarefa.isStatus(),
                        tarefa.getDataConclusao().format(formatter));
            }
            System.out.println("Tarefas salvas com sucesso!");
        } catch (IOException e) {
            System.out.println("Não foi possível salvar as tarefas.");
        }
    }

    // método em que cadastra ou carrega as tarefas do usuário
    public String cadastrarUsuario() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o seu nome de usuário: ");
        String nomeUsuario = scanner.nextLine();
        File arquivoUsuario = new File("usuarios/" + nomeUsuario + ".txt");
        if (arquivoUsuario.exists()) {
            System.out.println("Bem-vindo de volta, " + nomeUsuario + "!");
            return nomeUsuario;
        } else {
            System.out.println("Bem-vindo, " + nomeUsuario + "!");
            return nomeUsuario;
        }
    }

    // método para incrementação de ID das tarefas
    private int proximoId() {
        int maiorId = 0;
        for (Tarefa tarefa : this.tarefasPendentes) {
            if (tarefa.getId() > maiorId) {
                maiorId = tarefa.getId();
            }
        }
        for (Tarefa tarefa : this.tarefasConcluidas) {
            if (tarefa.getId() > maiorId) {
                maiorId = tarefa.getId();
            }
        }
        return maiorId + 1;
    }

    //método para carregar as tarefas do usuário existente
    public void carregarTarefas(String nomeArquivo) throws IOException {
        BufferedReader leitor = new BufferedReader(new FileReader("usuarios/" + nomeArquivo));
        String linha;
            while ((linha = leitor.readLine()) != null) {
                String[] campos = linha.split("\\|");
                int id = Integer.parseInt(campos[1]);
                String titulo = campos[2];
                String descricao = campos[3];
                boolean status = Boolean.parseBoolean(campos[5]);
                LocalDateTime dataCriacao = LocalDateTime.parse(campos[4],
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                LocalDateTime dataConclusao = null;

                if (status) {
                    dataConclusao = LocalDateTime.parse(campos[6], DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                }

                Tarefa tarefa = new Tarefa(id, titulo, descricao, dataCriacao);
                if (campos[0].equals("C")) {
                    tarefa.setStatus(true);
                } else {
                    tarefa.setStatus(false);
                }
                tarefa.setDataConclusao(dataConclusao);

                if (status) {
                    this.tarefasConcluidas.add(tarefa);
                } else {
                    this.tarefasPendentes.add(tarefa);
                }
            }
            leitor.close();
    }

    //método que cria novas pastas
    public void criarPasta() {
        File folder = new File("usuarios");
        if (!folder.exists()) {
            folder.mkdir();
            System.out.println("Pasta criada com sucesso!");
        }
    }
}
