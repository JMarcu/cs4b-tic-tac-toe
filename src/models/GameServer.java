package models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import models.ServerMessage.Message;
import models.ServerMessage.MessageType;
import models.ServerMessage.MoveMessageBody;

public class GameServer implements Runnable{
  private ServerSocket serverSocket;
  private ArrayList<HandleClientConnection> clientConnectionList;
  private ArrayList<HandleLobbies> lobbyList;
  private int totalClientConnections;
  
  /******************************/
  private GameState gameState;
  private Message messages;
  /******************************/

 
    GameServer(){
        serverSocket = null; 
        gameState = null;
        clientConnectionList = null;
        lobbyList = null;
        messages = null;
        totalClientConnections = 0;
    }

    @Override
    public void run(){
        clientConnectionList = new ArrayList<HandleClientConnection>();
        lobbyList = new ArrayList<HandleLobbies>();

        try {
         serverSocket = new ServerSocket(8080);

         System.out.println("Server started and listening for new connections...");

         while(true){
            Socket playerOneSocket= serverSocket.accept();
            totalClientConnections++;

            HandleClientConnection clientConnection1 = new HandleClientConnection(playerOneSocket);
            clientConnectionList.add(clientConnection1);


            Socket playerTwoSocket = serverSocket.accept();
            totalClientConnections++;

            HandleClientConnection clientConnection2 = new HandleClientConnection(playerTwoSocket);
            clientConnectionList.add(clientConnection2);


            lobbyList.add(new HandleLobbies(playerOneSocket, playerTwoSocket));
         }



        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                serverSocket.close();            
            }            
            catch (Exception ex){}        
        }
    }
    
    public void checkDeadLobbies(){
        for(int i = 0; i < lobbyList.size(); i++){
            if(lobbyList.get(i) != null && lobbyList.get(i).getPlayers().size() == 0){
                System.out.println("Removing Lobby with id of: " + lobbyList.get(i).getId());
                lobbyList.remove(i);
            }
        }
    }
}
