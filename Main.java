
import java.util.Scanner;

   public class Main{
   
      public static void main(String[] args){
      GerenciadorTarefas gerenciador = new GerenciadorTarefas();
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;
        
        while (opcao != 5) {
            System.out.println("=== Menu ===");
            System.out.println("1. Criar nova tarefa");
            System.out.println("2. Concluir tarefa");
            System.out.println("3. Exibir tarefas pendentes");
            System.out.println("4. Exibir tarefas concluídas");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            //scanner.nextLine();
            
         
            switch (opcao) {
                case 1:
                   gerenciador.adicionarTarefa();
                   break;
                case 2:
                   gerenciador.concluirTarefa();
                   break;
                case 3:
                   gerenciador.exibirTarefasPendentes();
                   break;
                case 4:
                   gerenciador.exibirTarefasConcluidas();
                   break;
                case 5:
                   System.out.println("Saindo...");
                   break;
                 default:
                  System.out.println("Opção inválida.");
                  break;
                   }
              }
             scanner.close();                 
           }
         }
      
                     
                     