package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import models.GameState;
import models.Lobby;
import models.Player;
import models.ServerMessage.AuthenticationRequestMessageBody;
import models.ServerMessage.AuthenticationResultMessageBody;
import models.ServerMessage.ConnectionMessageBody;
import models.ServerMessage.CreateLobbyMessageBody;
import models.ServerMessage.LobbyListMessageBody;
import models.ServerMessage.Message;
import models.ServerMessage.MessageType;

public class LobbyService extends AbstractWebsocketService {

    /*==========================================================================================================
     * CLASS VARIABLES
     *==========================================================================================================*/

    private boolean authenticated;

    private GameState gameState;

    /** JWT which authenticates the user and provides proof of their claims. */
    private String jwt = null;

    private Consumer<ArrayList<Lobby>> onConnectCallback;
    private Consumer<Boolean> onLeaveCallback;
    private Consumer<GameState> onLobbyCallback;
    private Consumer<ArrayList<Lobby>> onLobbyListCallback;

    private Player player;
    
    /** The singleton */
    private static LobbyService instance = null;

    /*==========================================================================================================
     * LIFECYCLE
     *==========================================================================================================*/

    /** Default constructor; player is uninitialized and connects to the domain 'herokuapp.com'. */
    protected LobbyService(){
        super("cs4b-tic-tac-toe-lobby-service");
        this.authenticated = false;
    }

    /**
     * Player is uninitialized, and connects to the specified domain.
     * @param domain The remote server's domain.
     */
    protected LobbyService(String domain){
        super("cs4b-tic-tac-toe-lobby-service", domain);
        this.authenticated = false;
    }

    /** 
     * Returns the client's instance of LobbyService. If none yet exists, one will be instantiated using
     * default values. The service will attempt to connect to a server at the domain 'herokuapp.com'.
     * @return Returns the client's instance of LobbyService.
     */
    public static LobbyService getInstance(){
        if(LobbyService.instance == null){
            LobbyService.instance = new LobbyService();
        }
        return instance;
    }

    /** 
     * Returns the client's instance of LobbyService. 
     * If none yet exists, one will be instantiated. The service will attempt to connect to the 
     * provided domain. If an instance already exists, the provided domain parameter is ignored.
     * @param domain The remote server's domain.
     * @return Returns the client's instance of LobbyService.
     */
    public static LobbyService getInstance(String domain){
        if(LobbyService.instance == null){
            LobbyService.instance = new LobbyService(domain);
        }
        return instance;
    }

    public void connect(String jwt, Player player, Consumer<ArrayList<Lobby>> onConnectCallback){
        this.jwt = jwt;
        this.onConnectCallback = onConnectCallback;
        this.player = player;
        AuthenticationRequestMessageBody body = new AuthenticationRequestMessageBody(this.jwt);
        try {
            send(new Message(body, MessageType.AUTHENTICATION_REQUEST));
        } catch (IOException e) {
            e.printStackTrace();
            final Consumer<ArrayList<Lobby>> temp = this.onConnectCallback;
            this.onConnectCallback = null;
            temp.accept(null);
        }
    }

    /*==========================================================================================================
     * LOBBY MANAGEMENT
     *==========================================================================================================*/

    public void createLobby(String name, Consumer<GameState> onLobbyCallback) throws Exception{
        if(!this.authenticated){
            this.onLobbyCallback = onLobbyCallback;
            CreateLobbyMessageBody body = new CreateLobbyMessageBody(name);
            send(new Message(body, MessageType.CREATE_LOBBY));
        } else{
            throw new Exception("Not Authenticated");
        }
    }

    public void joinLobby(UUID lobbyId, Consumer<GameState> onLobbyCallback) throws Exception{
        if(!this.authenticated){
            this.onLobbyCallback = onLobbyCallback;
            ConnectionMessageBody body = new ConnectionMessageBody(
                lobbyId, 
                player.getUuid(), 
                ConnectionMessageBody.Type.JOIN
            );
            send(new Message(body, MessageType.CONNECTION));
        } else{
            throw new Exception("Not Authenticated");
        }
    }

    public void leaveLobby(UUID lobbyId, Consumer<Boolean> onLeaveCallback) throws Exception{
        if(!this.authenticated){
            this.onLeaveCallback = onLeaveCallback;
            ConnectionMessageBody body = new ConnectionMessageBody(
                lobbyId, 
                player.getUuid(), 
                ConnectionMessageBody.Type.LEAVE
            );
            send(new Message(body, MessageType.CONNECTION));
        } else{
            throw new Exception("Not Authenticated");
        }
    }

    public void listLobbies(Consumer<ArrayList<Lobby>> onLobbyListCallback) throws Exception{
        if(!this.authenticated){
            this.onLobbyListCallback = onLobbyListCallback;
            send(new Message("", MessageType.LOBBY_LIST));
        } else{
            throw new Exception("Not Authenticated");
        }
    }

    /*==========================================================================================================
     * CLIENT ENDPOINT
     *==========================================================================================================*/

     /** No closure logic required. */
     @Override
     protected void handleClose(Session session, CloseReason closeReason) { }
 
     /** 
      * Deserializes message bodies and routes handling to the appropriate internal methods.
      * @param session A reference to the websocket session between the client and auth service.
      * @param message The message object received across the websocket connect.
      */
    @Override
    protected void handleMessage(Session session, Message message) {
        try{
            switch(message.getType()){
                case AUTHENTICATION_RESULT:
                    //Deserialize the body.
                    AuthenticationResultMessageBody authResultBody = GSON.fromJson(message.getBody(), AuthenticationResultMessageBody.class);
                    if(authResultBody.getSuccess()){
                        this.authenticated = true;
                        send(new Message("", MessageType.AUTHENTICATION_ACKNOWLEDGED));
                    } else {
                        this.authenticated = false;
                    }
                    break;
                case CONNECTION:
                    ConnectionMessageBody connBody = GSON.fromJson(message.getBody(), ConnectionMessageBody.class);

                    // AuthService.getInstance().get

                    // if(connBody.getType() == ConnectionMessageBody.Type.JOIN){
                    //     if(gameState.getPlayers().getValue0() == null){
                    //         gameState.setPlayerOne(connBody.);
                    //     }
                    // } else{

                    // }
                    // if(this.onLeaveCallback != null){
                    //     final Consumer<Boolean> temp = this.onLeaveCallback;
                    //     this.onLeaveCallback = null;
                    //     temp.accept(true);
                    // }

                    break;
                case CONNECTION_FAILURE:
                    break;
                case CONNECTION_SUCCESS:
                    break;
                case LOBBY_LIST:
                    LobbyListMessageBody lobbyListBody = GSON.fromJson(message.getBody(), LobbyListMessageBody.class);

                    if(this.onConnectCallback != null){
                        final Consumer<ArrayList<Lobby>> temp = this.onConnectCallback;
                        this.onConnectCallback = null;
                        temp.accept(lobbyListBody.getLobbies());
                    }

                    if(this.onLobbyListCallback != null){
                        final Consumer<ArrayList<Lobby>> temp = this.onLobbyListCallback;
                        this.onLobbyListCallback = null;
                        temp.accept(lobbyListBody.getLobbies());
                    }
                    break;
                case MOVE:
                    break;
                default:
                    break;
            }
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
 
     /** No opening logic required. */
     @Override
     protected void handleOpen(Session session) { }
}
