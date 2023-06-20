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

    // método do gerenciador de tarefas em si, onde o usuário realiza as tarefas e
    // conclui as tarefas do codigo
    public void iniciarGerenciador() {
        criarPastaSenhas();
        criarPasta();
        String nomeUsuario = cadastroLoginUsuario();
        String nomeArquivo = nomeUsuario + ".poo";
        try {
            System.out.println("\nCarregando Tarefas...\n");
            carregarTarefas(nomeArquivo);
            System.out.println("Suas Tarefas foram carregadas.");
        } catch (IOException e) {
            System.out.println("Não foi possível Carregar suas Tarefas e/ou não existem.");
        }
        exibirMenu(nomeArquivo);
    }

    // Método para cadastrar ou fazer login do usuário
    private String cadastroLoginUsuario() {
        System.out.println("Olá! Seja muito bem-vindo ao gerenciador de tarefas.\n");
        System.out.println("Digite o seu nome de usuário: ");
        String nomeUsuario = ScannerGlobal.nextLine();
        String nomeArquivoSenha = nomeUsuario + "senha.poo";
        File arquivoSenha = new File("senhas/" + nomeArquivoSenha);

        if (arquivoSenha.exists()) {
            return fazerLogin(nomeUsuario, arquivoSenha);
        } else {
            return cadastrarNovoUsuario(nomeUsuario, arquivoSenha);
        }
    }

    // Método para fazer login do usuário existente
    private String fazerLogin(String nomeUsuario, File arquivoSenha) {
        System.out.println("Usuário já existente. Deseja prosseguir para a senha? (S/N)");
        System.out.println("(Digite 'S' para digitar a sua senha ou 'N' para voltar e digitar outro usuário e/ou criar um novo.)");
        String opcao = ScannerGlobal.nextLine();
        if (opcao.equalsIgnoreCase("S")) {
            System.out.println("Bem-vindo de volta, " + nomeUsuario + "!");
            while (true) {
                if (verificarSenha(arquivoSenha)) {
                    System.out.println("Senha correta!");
                    break;
                } else {
                    System.out.println("Senha incorreta!");
                    System.out.println("Digite a senha correta: ");
                    String senhaDigitada = ScannerGlobal.nextLine();
                    if (verificarSenhaDigitada(arquivoSenha, senhaDigitada)) {
                        System.out.println("Senha correta!");
                        break;
                    }
                }
            }
            return nomeUsuario;
        } else if (opcao.equalsIgnoreCase("N")) {
            return cadastroLoginUsuario();
        } else {
            System.out.println("Opção inválida. Por favor, digite 'S' para prosseguir ou 'N' para voltar.");
            return cadastroLoginUsuario();
        }
    }

    // Método para cadastrar um novo usuário
    private String cadastrarNovoUsuario(String nomeUsuario, File arquivoSenha) {
        System.out.println("Bem-vindo, " + nomeUsuario + "!");
        System.out.println("Digite a sua senha: ");
        String senhaUsuario = ScannerGlobal.nextLine();
        try {
            arquivoSenha.createNewFile();
            FileWriter writer = new FileWriter(arquivoSenha);
            writer.write(senhaUsuario);
            writer.close();
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao criar o arquivo de senha.");
        }
        return nomeUsuario;
    }

    // métodos para verificar senha e senha digitada
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

    // método que exibe o menu do gerenciador de tarefas
    private void exibirMenu(String nomeArquivo) {
        int opcao = -1;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("\n\n=== Menu ===");
            System.out.println("1. Criar nova tarefa");
            System.out.println("2. Criar subtarefa de tarefa");
            System.out.println("3. Concluir tarefa");
            System.out.println("4. Exibir tarefas pendentes");
            System.out.println("5. Exibir tarefas concluídas");
            System.out.println("6. Buscar palavra(s) na(s) tarefa(s)");
            System.out.println("7. Filtrar por categoria");
            System.out.println("8. Editar Tarefa(BÔNUS)");
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

    // método que processa a opção selecionada pelo o usuário e executa seu
    // respectivo método
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

    // método para adicionar subtarefa(s) as tarefas
    public void adicionarSubtarefa() {
        exibirTarefasPendentes();
        int idTarefaPai;
        while (true) {
            System.out.print("Digite o ID da tarefa à qual deseja adicionar a subtarefa: ");
            idTarefaPai = verificaNumeroInteiro();
            if (verificarIdTarefaPai(idTarefaPai)) {
                break;
            } else {
                System.out.println("O ID da tarefa pai não existe. Por favor, digite novamente.");
            }
        }
        // ScannerGlobal.nextLine(); // Consumir a quebra de linha pendente
        System.out.print("Digite o título da subtarefa: ");
        String titulo = ScannerGlobal.nextLine();
        System.out.print("Digite a descrição da subtarefa: ");
        String descricao = ScannerGlobal.nextLine();
        LocalDateTime dataCriacao = LocalDateTime.now(); // Usa a data atual como data de criação
        Tarefa subtarefa = new Tarefa(idTarefaPai, titulo, descricao, dataCriacao, " ");
        // Adicionar a subtarefa à tarefa pai (procurar pelo ID da tarefa pai)
        adicionarSubtarefaATarefaPai(idTarefaPai, subtarefa);
        System.out.println("\nSubtarefa criada com sucesso!\n");
    }

    private boolean verificarIdTarefaPai(int id) {
        for (Tarefa tarefa : tarefasPendentes) {
            if (tarefa.getId() == id) {
                return true;
            }
        }
        return false;
    }

    // método para concluir as Tarefas
    private void concluirTarefa() {
        exibirTarefasPendentes();
        System.out.print("Digite o ID da tarefa que deseja concluir: ");
        int id = verificaNumeroInteiro();
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

    // métodos para exibição das tarefas com as subtarefas
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
                + prefixo + dataLabel + ": "
                + (dataLabel.equals("Data de Criação") ? tarefa.getDataCriacao() : tarefa.getDataConclusao())
                + "\n" + prefixo + "Concluída: " + (tarefa.isStatus() ? "Sim" : "Não")
                + "\n" + prefixo + "===========");
    }

    // métodos para buscar uma palavra chave nas tarefas e/ou subtarefas
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

    // método para filtrar tarefas por categoria
    private void filtrarTarefasPorCategoria() {
        System.out.println("Digite a categoria desejada: ");
        String pegacategoria = ScannerGlobal.nextLine();
        System.out.println("=== Tarefas com a categoria '" + pegacategoria + "' ===");
        boolean encontrouTarefas = false;
        for (Tarefa tarefa : this.tarefasPendentes) {
            if (tarefa.getCategoria().equalsIgnoreCase(pegacategoria)) {
                encontrouTarefas = true;
                exibirTarefaComSubtarefas(tarefa, 0);
            }
        }
        for (Tarefa tarefa : this.tarefasConcluidas) {
            if (tarefa.getCategoria().equalsIgnoreCase(pegacategoria)) {
                encontrouTarefas = true;
                exibirTarefaComSubtarefas(tarefa, 0);
            }
        }
        if (!encontrouTarefas) {
            System.out.println("Não há tarefas com a categoria '" + pegacategoria + "'.");
        }
    }

    /* métodos para editar uma tarefa pendente */
    private void editarTarefa() {
        exibirTarefasPendentes();
        System.out.println("Digite o ID da tarefa que deseja editar: ");
        int id = verificaNumeroInteiro();
        boolean tarefaEncontrada = false;
        for (Tarefa tarefa : this.tarefasPendentes) {
            if (tarefa.getId() == id) {
                exibirMenuEdicaoTarefa();
                int opcao = ScannerGlobal.nextInt();

                switch (opcao) {
                    case 1:
                        editarTituloTarefa(tarefa);
                        break;
                    case 2:
                        editarDescricaoTarefa(tarefa);
                        break;
                    case 3:
                        editarCategoriaTarefa(tarefa);
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

    private void exibirMenuEdicaoTarefa() {
        System.out.println("=== Editar Tarefa ===");
        System.out.println("1. Editar título");
        System.out.println("2. Editar descrição");
        System.out.println("3. Editar categoria");
        System.out.print("Escolha uma opção: \n");
    }

    private void editarTituloTarefa(Tarefa tarefa) {
        System.out.println("Digite o novo título da tarefa: ");
        ScannerGlobal.nextLine(); // Consumir a quebra de linha pendente
        String novoTitulo = ScannerGlobal.nextLine();
        tarefa.setTitulo(novoTitulo);
        System.out.println("Título atualizado com sucesso!");
    }

    private void editarDescricaoTarefa(Tarefa tarefa) {
        System.out.println("Digite a nova descrição da tarefa: ");
        ScannerGlobal.nextLine(); // Consumir a quebra de linha pendente
        String novaDescricao = ScannerGlobal.nextLine();
        tarefa.setDescricao(novaDescricao);
        System.out.println("Descrição atualizada com sucesso!");
    }

    private void editarCategoriaTarefa(Tarefa tarefa) {
        System.out.println("Digite a nova categoria da tarefa: ");
        ScannerGlobal.nextLine(); // Consumir a quebra de linha pendente
        String novaCategoria = ScannerGlobal.nextLine();
        tarefa.setCategoria(novaCategoria);
        System.out.println("Categoria atualizada com sucesso!");
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

    // método para que o scanner aceite apenas numeros inteiros e não quebre o
    // código ao digitar uma letra
    private int verificaNumeroInteiro() {
        int numero;
        while (true) {
            if (ScannerGlobal.hasNextInt()) {
                numero = ScannerGlobal.nextInt();
                break;
            } else {
                System.out.println("Apenas números são aceitos. Por favor, digite novamente: ");
                ScannerGlobal.nextLine(); // Limpar o valor inválido digitado
            }
        }
        ScannerGlobal.nextLine(); // Consumir a nova linha pendente
        return numero;
    }

    // métodos que criam as pastas usuário e senha para salvar as tarefas de cada
    // usuário
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
