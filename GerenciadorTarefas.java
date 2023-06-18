import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;

public class GerenciadorTarefas {
    private ArrayList<Tarefa> tarefasPendentes;
    private ArrayList<Tarefa> tarefasConcluidas;

    // construtor
    public GerenciadorTarefas() {
        this.tarefasPendentes = new ArrayList<>();
        this.tarefasConcluidas = new ArrayList<>();
    }

    // método do gerenciador de tarefas em si, onde o usuário realiza as tarefas e conclui as tarefas do codigo
    public void iniciarGerenciador() {
        criarPastaSenhas();
        criarPasta();
        String nomeUsuario = cadastrarUsuario();
        String nomeArquivo = nomeUsuario + ".txt";
        try {
            System.out.println("\nCarregando Tarefas...\n");
            carregarTarefas(nomeArquivo);
            System.out.println("Suas Tarefas foram carregadas.\n\n");
        } catch (IOException e) {
            System.out.println("Não foi possível Carregar suas Tarefas e/ou não existem.");
        }
        exibirMenu(nomeArquivo);
    }
    
    //método que exibe o menu do gerenciador de tarefas
    private void exibirMenu(String nomeArquivo) {
        int opcao = -1;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("=== Menu ===");
            System.out.println("1. Criar nova tarefa");
            System.out.println("2. Criar subtarefa de tarefa");
            System.out.println("3. Concluir tarefa");
            System.out.println("4. Exibir tarefas pendentes");
            System.out.println("5. Exibir tarefas concluídas");
            System.out.println("6. Buscar palavra(s) na(s) tarefa(s)");
            System.out.println("7. Filtrar por categoria");
            System.out.println("8. Editar Tarefa");
            System.out.println("9. Sair e salvar");
            System.out.println("============");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Opção inválida. Por favor, digite um número inteiro.");
                scanner.next(); // limpa o buffer do scanner
                continue;
            }
            executarOpcao(opcao);
        } while (opcao != 9);
        System.out.println("Salvando tarefas...");
        salvarTarefas(nomeArquivo);
        scanner.close();
    }

    //método que processa a opção selecionada pelo usuário e executa seu respectivo método
    private void executarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                adicionarTarefa();
                break;
            case 2:
                adicionarSubtarefa();
                break;
            case 3:
                concluirTarefa();
                break;
            case 4:
                exibirTarefasPendentes();
                break;
            case 5:
                exibirTarefasConcluidas();
                break;
            case 6:
                List<Tarefa> tarefasEncontradas = buscarPalavra();
                exibirTarefasEncontradas(tarefasEncontradas);
                break;
            case 7:
                filtrarTarefasPorCategoria();
                break;
            case 8:
                editarTarefa();
                break;
            case 9:
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    // método para adicionar Tarefas
    private void adicionarTarefa() {
        System.out.println("Digite o título de sua tarefa: ");
        String titulo = ScannerGlobal.nextLine();
        System.out.println("Digite a descrição de sua tarefa: ");
        String descricao = ScannerGlobal.nextLine();
        // adiciona a data atual como data de criação
        LocalDateTime dataCriacao = LocalDateTime.now();
        int id = proximoId();
        System.out.println("Defina a categoria da sua tarefa: ");
        String categoria = ScannerGlobal.nextLine();
        Tarefa novaTarefa = new Tarefa(id, titulo, descricao, dataCriacao, categoria);
        this.tarefasPendentes.add(novaTarefa);
        System.out.println("Tarefa Criada com sucesso!");
    }
    
    // método para incrementação de ID das tarefas após criadas
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

    //método para adicionar subtarefa(s) as tarefas
    public void adicionarSubtarefa() {
        exibirTarefasPendentes();
        System.out.print("Digite o ID da tarefa à qual deseja adicionar a subtarefa: ");
        int idTarefaPai = ScannerGlobal.nextInt();
        ScannerGlobal.nextLine(); // Consumir a quebra de linha pendente
        System.out.print("Digite o título da subtarefa: ");
        String titulo = ScannerGlobal.nextLine();
        System.out.print("Digite a descrição da subtarefa: ");
        String descricao = ScannerGlobal.nextLine();
        LocalDateTime dataCriacao = LocalDateTime.now(); // Usa a data atual como data de criação
        Tarefa subtarefa = new Tarefa(idTarefaPai, titulo, descricao, dataCriacao, " ");
        // Adicionar a subtarefa à tarefa pai (procurar pelo ID da tarefa pai)
        adicionarSubtarefaATarefaPai(idTarefaPai, subtarefa);
        System.out.println("\nSubtarefa criada com sucesso!\n\n");
    }

    // método para concluir as Tarefas
    private void concluirTarefa() {
        exibirTarefasPendentes();
        System.out.println("Digite o ID da tarefa que deseja concluir: ");
        int id = ScannerGlobal.nextInt();
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
    private void exibirTarefasPendentes() {
        if (this.tarefasPendentes.isEmpty()) {
            System.out.println("Não há tarefas pendentes.");
            return;
        }
        System.out.println("=== Tarefas pendentes ===");
        for (Tarefa tarefa : this.tarefasPendentes) {
            exibirTarefaComSubtarefas(tarefa, 0);
        }
    }
    // método para a exibição das tarefas concluídas
    private void exibirTarefasConcluidas() {
        if (this.tarefasConcluidas.isEmpty()) {
            System.out.println("Não há tarefas concluídas.");
            return;
        }
        System.out.println("=== Tarefas concluídas ===");
        for (Tarefa tarefa : this.tarefasConcluidas) {
            exibirTarefaComSubtarefas(tarefa, 0);
        }
    }

    private void exibirTarefaComSubtarefas(Tarefa tarefa, int nivel) {
        StringBuilder prefixo = new StringBuilder();
        for (int i = 0; i < nivel; i++) {
            prefixo.append("  ");
        }

        if (!tarefa.isStatus()) {
            imprimirTarefa(tarefa, prefixo.toString(), "Data de Criação");
        } else {
            imprimirTarefa(tarefa, prefixo.toString(), "Data de conclusão");
        }
        List<Tarefa> subtarefas = tarefa.getSubtarefas();

        for (Tarefa subtarefa : subtarefas) {
            System.out.println(prefixo.toString() + "======Subtarefa======\n" + prefixo.toString()
                    + "  ID: " + subtarefa.getId()
                    + prefixo.toString() + "\n  Título: " + subtarefa.getTitulo()
                    + "\n" + prefixo.toString() + "  Descrição: " + subtarefa.getDescricao()
                    + "\n" + prefixo.toString() + "  Data de Criação: " + subtarefa.getDataCriacao()
                    + "\n" + prefixo.toString() + "=================");
        }
    }

    private void imprimirTarefa(Tarefa tarefa, String prefixo, String dataLabel) {
        System.out.println(prefixo + "\n\n===========\n" + prefixo + "ID: " + tarefa.getId()
                + prefixo + "\nTítulo: " + tarefa.getTitulo() + "\n" + prefixo + "Descrição: "
                + tarefa.getDescricao() + "\n" + prefixo + "Categoria: " + tarefa.getCategoria() + "\n"
                + prefixo + dataLabel + ": " + (dataLabel.equals("Data de Criação") ? tarefa.getDataCriacao() : tarefa.getDataConclusao())
                + "\n" + prefixo + "Concluída: " + (tarefa.isStatus() ? "Sim" : "Não")
                + "\n" + prefixo + "===========");
    }

    private List<Tarefa> buscarPalavra() {
        System.out.print("Digite a(s) palavra(s) que deseja buscar: ");
        String stringChave = ScannerGlobal.nextLine().toLowerCase(); // Converter para minúsculas
        ArrayList<Tarefa> tarefasEncontradas = new ArrayList<>();
        for (Tarefa tarefa : tarefasPendentes) { // Verifica tarefas pendentes
            if (tarefa.getTitulo().toLowerCase().contains(stringChave) ||
                tarefa.getDescricao().toLowerCase().contains(stringChave) ||
                tarefa.getCategoria().toLowerCase().contains(stringChave) ||
                contemPalavraSubtarefas(tarefa.getSubtarefas(), stringChave)) {
                tarefasEncontradas.add(tarefa);
            }
        }
        for (Tarefa tarefa : tarefasConcluidas) { // Verifica tarefas concluídas
            if (tarefa.getTitulo().toLowerCase().contains(stringChave) ||
                tarefa.getDescricao().toLowerCase().contains(stringChave) ||
                tarefa.getCategoria().toLowerCase().contains(stringChave) ||
                contemPalavraSubtarefas(tarefa.getSubtarefas(), stringChave)) {
                tarefasEncontradas.add(tarefa);
            }
        }
        return tarefasEncontradas;
    }

    private boolean contemPalavraSubtarefas(List<Tarefa> subtarefas, String stringChave) {
        for (Tarefa subtarefa : subtarefas) {
            if (subtarefa.getTitulo().toLowerCase().contains(stringChave) ||
                    subtarefa.getDescricao().toLowerCase().contains(stringChave) ||
                    contemPalavraSubtarefas(subtarefa.getSubtarefas(), stringChave)) {
                return true;
            }
        }
        return false;
    }

    private void exibirTarefasEncontradas(List<Tarefa> tarefasEncontradas) {
        System.out.println("\nTarefas encontradas:");
        for (Tarefa tarefa : tarefasEncontradas) {
            exibirTarefaComSubtarefas(tarefa, 0);
        }
    }

    private void filtrarTarefasPorCategoria() {
        System.out.println("Digite a categoria desejada: ");
        String categoria = ScannerGlobal.nextLine();
        System.out.println("=== Tarefas com a categoria '" + categoria + "' ===");
        boolean encontrouTarefas = false;
        for (Tarefa tarefa : this.tarefasPendentes) {
            if (tarefa.getCategoria().equalsIgnoreCase(categoria)) {
                encontrouTarefas = true;
                exibirTarefaComSubtarefas(tarefa, 0);
            }
        }
        for (Tarefa tarefa : this.tarefasConcluidas) {
            if (tarefa.getCategoria().equalsIgnoreCase(categoria)) {
                encontrouTarefas = true;
                exibirTarefaComSubtarefas(tarefa, 0);
            }
        }
        if (!encontrouTarefas) {
            System.out.println("Não há tarefas com a categoria '" + categoria + "'.");
        }
    }

    private void editarTarefa() {
        System.out.println("\n=== Editor de Tarefas ===\n");
        for (Tarefa tarefa : this.tarefasPendentes) {
            System.out.println("=====\n" + tarefa.getId() + " - " + tarefa.getTitulo() + "\nDescrição: "
                    + tarefa.getDescricao() + "\nCategoria: " + tarefa.getCategoria() + "\n=====\n");
        }
        System.out.println("Digite o ID da tarefa que deseja editar: ");
        int id = ScannerGlobal.nextInt();
        boolean tarefaEncontrada = false;

        for (Tarefa tarefa : this.tarefasPendentes) {
            if (tarefa.getId() == id) {
                System.out.println("=== Editar Tarefa ===");
                System.out.println("1. Editar título");
                System.out.println("2. Editar descrição");
                System.out.println("3. Editar categoria");
                System.out.print("Escolha uma opção: \n");
                int opcao = ScannerGlobal.nextInt();

                switch (opcao) {
                    case 1:
                        System.out.println("Digite o novo título da tarefa: ");
                        ScannerGlobal.nextLine(); // Consumir a quebra de linha pendente
                        String novoTitulo = ScannerGlobal.nextLine();
                        tarefa.setTitulo(novoTitulo);
                        System.out.println("Título atualizado com sucesso!\n");
                        break;
                    case 2:
                        System.out.println("Digite a nova descrição da tarefa: ");
                        ScannerGlobal.nextLine(); // Consumir a quebra de linha pendente
                        String novaDescricao = ScannerGlobal.nextLine();
                        tarefa.setDescricao(novaDescricao);
                        System.out.println("Descrição atualizada com sucesso!\n");
                        break;
                    case 3:
                        System.out.println("Digite a nova categoria da tarefa: ");
                        ScannerGlobal.nextLine(); // Consumir a quebra de linha pendente
                        String novaCategoria = ScannerGlobal.nextLine();
                        tarefa.setCategoria(novaCategoria);
                        System.out.println("Categoria atualizada com sucesso!\n");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                        break;
                }
                tarefaEncontrada = true;
                break;
            }
        }
        if (!tarefaEncontrada) {
            System.out.println("Tarefa não encontrada.");
        }
    }

    // métodos para salvar as tarefas com as subtarefas do usuário em arquivos
    private void salvarSubtarefas(Tarefa tarefa, PrintWriter writer, DateTimeFormatter formatter) {
        List<Tarefa> subtarefas = tarefa.getSubtarefas();
        if (subtarefas != null && subtarefas.size() > 0) {
            for (Tarefa subtarefa : subtarefas) {
                writer.printf("S|%d|%s|%s|%s|%b|%s|%n", subtarefa.getId(), subtarefa.getTitulo(),
                        subtarefa.getDescricao(),
                        subtarefa.getDataCriacao().format(formatter), subtarefa.isStatus(), subtarefa.getCategoria());
                salvarSubtarefas(subtarefa, writer, formatter);
            }
        }
    }

    private void salvarTarefas(String nomeArquivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("usuarios/" + nomeArquivo))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            for (Tarefa tarefa : this.tarefasPendentes) {
                writer.printf("P|%d|%s|%s|%s|%b|%s|%n", tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(),
                        tarefa.getDataCriacao().format(formatter), tarefa.isStatus(), tarefa.getCategoria());
                salvarSubtarefas(tarefa, writer, formatter);
            }
            for (Tarefa tarefa : this.tarefasConcluidas) {
                writer.printf("C|%d|%s|%s|%s|%b|%s|%s%n", tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(),
                        tarefa.getDataCriacao().format(formatter), tarefa.isStatus(), tarefa.getCategoria(),
                        tarefa.getDataConclusao().format(formatter));
                salvarSubtarefas(tarefa, writer, formatter);
            }
            System.out.println("Tarefas salvas com sucesso!");
        } catch (IOException e) {
            System.out.println("Não foi possível salvar as tarefas." + e);
        }
    }

    // método em que cadastra ou faz o login do usuário
    private String cadastrarUsuario() {
        System.out.println("Digite o seu nome de usuário: ");
        String nomeUsuario = ScannerGlobal.nextLine();
        String nomeArquivoSenha = nomeUsuario + "senha.txt";
        File arquivoSenha = new File("senhas/" + nomeArquivoSenha);

        if (arquivoSenha.exists()) {
            System.out.println("Bem-vindo de volta, " + nomeUsuario + "!");
            // Loop para verificar a senha
            while (true) {
                if (verificarSenha(arquivoSenha)) {
                    System.out.println("Senha correta!");
                    break; // Sai do loop caso a senha esteja correta
                } else {
                    System.out.println("Senha incorreta!");
                    System.out.println("Digite a senha correta: ");
                    // Ler a senha digitada pelo usuário
                    String senhaDigitada = ScannerGlobal.nextLine();
                    // Comparar a senha digitada com a senha armazenada no arquivo
                    if (verificarSenhaDigitada(arquivoSenha, senhaDigitada)) {
                        System.out.println("Senha correta!");
                        break; // Sai do loop caso a senha esteja correta
                    }
                }
            }
            return nomeUsuario;
        } else {
            System.out.println("Bem-vindo, " + nomeUsuario + "!");
            // Solicitar a senha do usuário
            System.out.println("Digite a sua senha: ");
            String senhaUsuario = ScannerGlobal.nextLine();
            try {
                // Criar o arquivo de senha
                arquivoSenha.createNewFile();
                // Escrever a senha no arquivo
                FileWriter writer = new FileWriter(arquivoSenha);
                writer.write(senhaUsuario);
                writer.close();
                System.out.println("Usuário cadastrado com sucesso!");
            } catch (IOException e) {
                System.out.println("Erro ao criar o arquivo de senha.");
            }
            return nomeUsuario;
        }
    }

    private boolean verificarSenha(File arquivoSenha) {
        try {
            // Ler a senha do arquivo
            FileReader reader = new FileReader(arquivoSenha);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String senhaArmazenada = bufferedReader.readLine();
            bufferedReader.close();
            // Solicitar a senha atual
            System.out.println("Digite a senha atual: ");
            String senhaAtual = ScannerGlobal.nextLine();
            // Verificar se as senhas coincidem
            return senhaArmazenada.equals(senhaAtual);
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de senha.");
        }
        return false;
    }

    private boolean verificarSenhaDigitada(File arquivoSenha, String senhaDigitada) {
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivoSenha))) {
            String senhaArmazenada = reader.readLine();
            return senhaDigitada.equals(senhaArmazenada);
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de senha.");
        }
        return false;
    }

    // método para carregar as tarefas do usuário existente
    private void carregarTarefas(String nomeArquivo) throws IOException {
        BufferedReader leitor = new BufferedReader(new FileReader("usuarios/" + nomeArquivo));
        String linha;

        while ((linha = leitor.readLine()) != null) {
            String[] campos = linha.split("\\|");
            String tipo = campos[0];
            int id = Integer.parseInt(campos[1]);
            String titulo = campos[2];
            String descricao = campos[3];
            boolean status = Boolean.parseBoolean(campos[5]);
            LocalDateTime dataCriacao = LocalDateTime.parse(campos[4],
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            LocalDateTime dataConclusao = null;
            String categoria = campos[6];

            if (status) {
                dataConclusao = LocalDateTime.parse(campos[7], DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            }
            Tarefa tarefa = new Tarefa(id, titulo, descricao, dataCriacao, categoria);
            tarefa.setStatus(status);
            tarefa.setDataConclusao(dataConclusao);
            // Verificar se é uma subtarefa (inicia com "S")
            if (tipo.equals("S")) {
                // Adicionar a subtarefa à tarefa pai (procurar pelo ID da tarefa pai)
                adicionarSubtarefaATarefaPai(id, tarefa);
            } else {
                if (status) {
                    this.tarefasConcluidas.add(tarefa);
                } else {
                    this.tarefasPendentes.add(tarefa);
                }
            }
        }
        leitor.close();
    }

    private void adicionarSubtarefaATarefaPai(int idTarefaPai, Tarefa subtarefa) {
        // Encontre a tarefa pai pelo ID e adicione a subtarefa a ela
        for (Tarefa tarefa : this.tarefasPendentes) {
            if (tarefa.getId() == idTarefaPai) {
                tarefa.adicionarSubtarefa(subtarefa);
                return;
            }
        }
        for (Tarefa tarefa : this.tarefasConcluidas) {
            if (tarefa.getId() == idTarefaPai) {
                tarefa.adicionarSubtarefa(subtarefa);
                return;
            }
        }
    } 
    
    // método que cria a pasta usuário para salvar as tarefas de cada usuário
    private void criarPasta() {
        File folder = new File("usuarios");
        if (!folder.exists()) {
            folder.mkdir();
            System.out.println("Pasta de usuários criada com sucesso!");
        }
    }

    private void criarPastaSenhas() {
        File folder = new File("senhas");
        if (!folder.exists()) {
            folder.mkdir();
            System.out.println("Pasta de senhas criada com sucesso!");
        }
    }
}
