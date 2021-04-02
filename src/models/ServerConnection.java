package models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import models.ServerMessage.ChatMessageBody;
import models.ServerMessage.Message;
import models.ServerMessage.MessageType;
import models.ServerMessage.MoveMessageBody;

public class ServerConnection extends Thread{
    private GameState gameState;
    private Subscription gameStateSubscription;
    private String host;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int port;
    private UUID ownPlayerId;
    private Socket socket;

    public ServerConnection(){
        this("localhost", 4210);
    }

    public ServerConnection(String host, int port){
        this.host = host;
        this.in = null;
        this.out = null;
        this.port = port;
        this.socket = null;

        try {
            this.socket = new Socket(host, port);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread clientThread = new Thread(this);
        clientThread.start();
    }

    public void setGameState(GameState gameState, UUID ownPlayerId){
        this.gameState = gameState;
        this.ownPlayerId = ownPlayerId;

        if(this.gameStateSubscription != null){
            this.gameStateSubscription.cancel();
        }

        this.gameState.subscribe(new Subscriber<GameState.Patch>(){
			@Override public void onSubscribe(Subscription subscription) { 
                gameStateSubscription = subscription; 
                gameStateSubscription.request(1);
            }
			@Override public void onNext(GameState.Patch item) { 
                onGameStatePatch(item); 
                gameStateSubscription.request(1);
            }
			@Override public void onError(Throwable throwable) { }
			@Override public void onComplete() { }
        });
    }

    @Override
    public void run() {
        while(true) {
            try {
                Object data = in.readObject();
                System.out.println("data" + data);
                if(data instanceof Message){
                    final Message message = (Message)data;
                    this.onMessage(message);
                } else{
                    System.err.println("Unrecognised message format ::" + data);

                }
            } catch(IOException e){
                e.printStackTrace();
            } catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    private void onGameStatePatch(GameState.Patch patch){
        if(patch.getMove() != null && !patch.getMove().getValue0().getUuid().equals(this.ownPlayerId)){
            try {
                this.out.writeObject(
                    new Message(
                        new MoveMessageBody(
                            patch.getMove().removeFrom0(), 
                            patch.getMove().getValue0().getUuid()
                        ),
                        MessageType.MOVE
                    )
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onMessage(Message message){
        System.out.println(message);
        switch(message.getType()){
            case CHAT:
                break;
            case CONNECTION:
                break;
            case LOBBY_LIST:
                try {
                    this.out.writeObject(new Message(new ChatMessageBody(UUID.randomUUID(), "some chat message"), MessageType.CHAT));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case MOVE:
                final MoveMessageBody body = (MoveMessageBody)message.getBody();
                if(!body.getPlayerId().equals(ownPlayerId)){
                    this.gameState.setCell(body.getCell().getValue0(), body.getCell().getValue1());
                }
                break;
            case PLAYER_PROPERTIES:
                break;
            default:
                System.err.println("Unknown message type received from server :: " + message.getType());
                break;
        }
    }
}
