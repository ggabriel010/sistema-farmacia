package farmacia.view;

import java.util.Scanner;

/**
 * Utilitário compartilhado entre todas as Views.
 * Centraliza o Scanner e métodos de exibição padronizados.
 */
public class Tela {

    private static final Scanner scanner = new Scanner(System.in);

    private static final String LINHA  = "══════════════════════════════════════════════════";
    private static final String TRACOS = "--------------------------------------------------";

    private Tela() {}

    public static Scanner getScanner() {
        return scanner;
    }

    public static void cabecalho(String titulo) {
        System.out.println("\n" + LINHA);
        System.out.println("  " + titulo);
        System.out.println(LINHA);
    }

    public static void separador() {
        System.out.println(TRACOS);
    }

    public static void sucesso(String msg) {
        System.out.println("\n  ✔  " + msg);
    }

    public static void erro(String msg) {
        System.out.println("\n  ✘  ERRO: " + msg);
    }

    public static void aviso(String msg) {
        System.out.println("\n  ⚠  " + msg);
    }

    public static void info(String msg) {
        System.out.println("     " + msg);
    }

    public static String lerLinha(String prompt) {
        System.out.print("  > " + prompt + ": ");
        return scanner.nextLine().trim();
    }

    /**
     * Lê uma linha obrigatória — repete até o usuário digitar algo.
     */
    public static String lerLinhaObrigatoria(String prompt) {
        String valor;
        do {
            valor = lerLinha(prompt);
            if (valor.isBlank()) erro("Este campo é obrigatório.");
        } while (valor.isBlank());
        return valor;
    }

    /**
     * Lê um inteiro; repete se inválido.
     */
    public static int lerOpcao(int min, int max) {
        while (true) {
            String entrada = lerLinha("Opção [" + min + "-" + max + "]");
            try {
                int opcao = Integer.parseInt(entrada);
                if (opcao >= min && opcao <= max) return opcao;
                erro("Digite um número entre " + min + " e " + max + ".");
            } catch (NumberFormatException e) {
                erro("Entrada inválida. Digite um número.");
            }
        }
    }

    /**
     * Exibe mensagem e aguarda Enter para continuar.
     */
    public static void pausar() {
        System.out.print("\n  Pressione Enter para continuar...");
        scanner.nextLine();
    }

    /**
     * Pergunta confirmação S/N.
     */
    public static boolean confirmar(String pergunta) {
        while (true) {
            String resp = lerLinha(pergunta + " [S/N]").toUpperCase();
            if (resp.equals("S")) return true;
            if (resp.equals("N")) return false;
            erro("Digite S para Sim ou N para Não.");
        }
    }
}
