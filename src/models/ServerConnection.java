package models;

import com.google.gson.Gson;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import models.ServerMessage.AuthenticationRequestMessageBody;
import models.ServerMessage.AuthenticationResultMessageBody;
import models.ServerMessage.ConnectionMessageBody;
import models.ServerMessage.Message;
import models.ServerMessage.MessageType;
import org.apache.tomcat.util.json.ParseException;

@ClientEndpoint
public class ServerConnection extends Thread{
    private WebSocketContainer container;
    private GameState gameState;
    private Subscription gameStateSubscription;
    private String host;
    private Player player;
    private Session session;

    public ServerConnection(){
        this("ws://cs4b-tic-tac-toe-lobby-service.herokuapp.com/ws");
        // this("ws://localhost:4205/ws");
    }

    public ServerConnection(String host){
        this.host = host;

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
        Gson gson = new Gson();
        Message message = gson.fromJson(messageString, Message.class);
        switch(message.getType()){
            case AUTHENTICATION_ACKNOWLEDGED:
                break;
            case AUTHENTICATION_REQUEST:
                break;
            case AUTHENTICATION_RESULT:
                AuthenticationResultMessageBody authResultMsg = new Gson().fromJson(message.getBody(), AuthenticationResultMessageBody.class);
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
            send(new Message(new AuthenticationRequestMessageBody("token"), MessageType.AUTHENTICATION_REQUEST));
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
