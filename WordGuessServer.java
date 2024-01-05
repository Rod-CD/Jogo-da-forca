import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class WordGuessServer extends UnicastRemoteObject implements WordGuessService {
    private static final long serialVersionUID = 1L;

    private String palavraChave;
    private char[] acertos;
    private String letrasUsadas;
    private int vidas;

    public WordGuessServer() throws RemoteException {
        super();
        initializeGame();
    }

    private void initializeGame() {
        String[] palavras = {"SERVIDOR", "CLIENTE", "ADMINISTRADOR", "CLUSTER", "JAVA", "LINUX",
                "MICROPROCESSADOR", "PROCESSO", "COMPUTADOR", "INTERFACE", "INTEROPERABILIDADE", "PORTABILIDADE",
                "ESCALABILIDADE", "DESEMPENHO", "VULNERABILIDADE", "CONECTIVIDADE", "PERVASIVO", "MIDDLEWARE"};

        Random random = new Random();
        int indiceSorteado = random.nextInt(palavras.length);
        palavraChave = palavras[indiceSorteado];
        acertos = new char[palavraChave.length()];
        letrasUsadas = "";
        vidas = 10;

        for (int i = 0; i < acertos.length; i++) {
            acertos[i] = 0;
        }
    }

    public String startGame() throws RemoteException {
        return "O jogo começou! Adivinhe a palavra: " + getWordWithGuesses();
    }

    public String guessLetter(char letter) throws RemoteException {
        letter = Character.toUpperCase(letter);
        if (letrasUsadas.contains(String.valueOf(letter))) {
            return "Você já tentou a letra " + letter + ".";
        } else {
            letrasUsadas += " " + letter;

            boolean perdeVida = true;
            for (int i = 0; i < palavraChave.length(); i++) {
                if (letter == palavraChave.charAt(i)) {
                    acertos[i] = 1;
                    perdeVida = false;
                }
            }

            if (perdeVida) {
                vidas--;
            }

            if (isGameOver()) {
                if (hasWon()) {
                	return "Fim de jogo, você ganhou! A palavra era '" + palavraChave + "'";
                }
                
                else {
                    return "Fim de jogo, você perdeu... A palavra era '" + palavraChave + "'";
                 
                }
            } else {
                return "\nPalavra: " + getWordWithGuesses() + "\nVocê tem " + vidas + " vidas restantes.";
            }
        }
    } 
    
    

    private boolean isGameOver() {
        return vidas <= 0 || hasWon();
    }
    


    private boolean hasWon() {
        for (int i = 0; i < acertos.length; i++) {
            if (acertos[i] == 0) {
           
                return false;
            }
        }
        return true;
    }
    private String getWordWithGuesses() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < palavraChave.length(); i++) {
            if (acertos[i] == 0) {
                result.append(" _ ");
            } else {
                result.append(" ").append(palavraChave.charAt(i)).append(" ");
            }
        }
        return result.toString();
    }
    
    public static void main(String[] args) {
        try {
            WordGuessServer server = new WordGuessServer();
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("WordGuessService", server);
            System.out.println("Servidor pronto para receber chamadas remotas...");
        } catch (Exception e) {
            System.err.println("Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}