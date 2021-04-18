package models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import com.google.gson.Gson;

import models.ServerMessage.AuthenticationRequestMessage;
import models.ServerMessage.AuthenticationResultMessage;
import models.ServerMessage.ConnectionMessageBody;
import models.ServerMessage.Message;
import models.ServerMessage.MessageType;
import models.ServerMessage.MoveMessageBody;
import models.ServerMessage.PlayerPropertiesMessageBody;

import org.apache.tomcat.util.json.ParseException;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

@ClientEndpoint
public class ServerConnection extends Thread{
    private WebSocketContainer container;
    private GameState gameState;
    private Subscription gameStateSubscription;
    private String host;
    private int port;
    private Player player;
    private Session session;

    public ServerConnection(){
        // this("ws://cs4b-tic-tac-toe-lobby-service.herokuapp.com/ws", 443);
        this("ws://localhost:4205/ws", 4210);
    }

    public ServerConnection(String host, int port){
        this.host = host;
        this.port = port;

        Thread clientThread = new Thread(this);
        clientThread.start();
    }

    public void send(Message message) throws IOException{
        session.getBasicRemote().sendText(new Gson().toJson(message));
    }

    @OnOpen
    public void onOpen(Session session){
        System.out.println("Connected: " + session);
    }
    
    @OnClose
    public void onClose(Session sesion, CloseReason closeReason){
        System.out.println("Disconnected: " + closeReason);
    }

    @OnMessage
    public void onMessage(Session session, String messageString){
        Message message = new Gson().fromJson(messageString, Message.class);
        switch(message.getType()){
            case AUTHENTICATION_ACKNOWLEDGED:
                break;
            case AUTHENTICATION_REQUEST:
                break;
            case AUTHENTICATION_RESULT:
                AuthenticationResultMessage authResultMsg = (AuthenticationResultMessage) message;
                System.out.println("Success: " + authResultMsg.getSuccess());
                break;
            case CHAT:
                break;
            case CONNECTION:
                break;
            case LOBBY_LIST:
                System.out.println("Type: LOBBY_LIST");
                break;
            case MOVE:
                System.out.println("Type: MOVE");
                break;
            case PLAYER_PROPERTIES:
                break;
            default:
                System.err.println("Unknown message type received from server :: " + message.getType());
                break;
        }
    }

    public void setGameState(GameState gameState){
        this.gameState = gameState;

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
        container = ContainerProvider.getWebSocketContainer();

        try {
            session = container.connectToServer(this, new URI(host));

            //HARDCODE
            AuthenticationRequestMessage authRequest = new AuthenticationRequestMessage("token");
            send(authRequest);
        } catch (DeploymentException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void onGameStatePatch(GameState.Patch patch){
        if(patch.getMove() != null && !patch.getMove().getValue0().getUuid().equals(this.player.getUuid())){
            // try {
            //     this.out.writeObject(
            //         new Message(
            //             new MoveMessageBody(
            //                 patch.getMove().removeFrom0(), 
            //                 patch.getMove().getValue0().getUuid()
            //             ),
            //             MessageType.MOVE
            //         )
            //     );
            // } catch (IOException e) {
            //     e.printStackTrace();
            // }
        }
    }
}
