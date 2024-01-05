import java.rmi.Naming;
import java.util.Scanner;

public class WordGuessClient {
    public static void main(String[] args) {
        try {
            WordGuessService gameService = (WordGuessService) Naming.lookup("rmi://localhost/WordGuessService");
            Scanner in = new Scanner(System.in);
            String mensagem = gameService.startGame();

            while (!mensagem.contains("Fim de jogo")) {
                System.out.println(mensagem);
                System.out.print("Insira sua próxima letra: ");
                char letra = in.next().toUpperCase().charAt(0);
                mensagem = gameService.guessLetter(letra);
            }            

            System.out.println(mensagem);
        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
