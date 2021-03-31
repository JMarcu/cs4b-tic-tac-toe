package models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class HandleLobbies implements Runnable{

    private Socket playerOne;
    private ObjectInputStream player1In;
    private ObjectOutputStream player1Out;

    private Socket playerTwo;
    private ObjectInputStream player2In;
    private ObjectOutputStream player2Out;

    private Lobby lobby;
    
    public HandleLobbies(Socket playerOne, Socket playerTwo){

        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        
        lobby = new Lobby("");
    }

    @Override
    public void run(){




        
    }  
}
// try {
      

//     Thread clientThread = new Thread(this);
// } catch (UnknownHostException e) {
//     e.printStackTrace();
// } catch (IOException e) {
//     e.printStackTrace();
// }