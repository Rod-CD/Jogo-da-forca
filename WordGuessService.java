import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WordGuessService extends Remote {
    String startGame() throws RemoteException;
    String guessLetter(char letter) throws RemoteException;
}
