import java.util.Scanner;

public class ScannerGlobal {
    private static Scanner scanner = new Scanner(System.in);

    private ScannerGlobal() {
        // Construtor privado para impedir a instanciação da classe
    }

    public static String nextLine() {
        return scanner.nextLine();
    }

    public static int nextInt() {
        return scanner.nextInt();
    }

    // Outros métodos para ler diferentes tipos de dados, se necessário

    public static void close() {
        scanner.close();
    }
}
