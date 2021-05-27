package services;

import interfaces.CallBackable;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.UUID;
import models.GameState;
import models.Lobby;
import models.Player;
import models.ServerMessage.AuthenticationRequestMessageBody;
import models.ServerMessage.AuthenticationResultMessageBody;
import models.ServerMessage.ConnectionMessageBody;
import models.ServerMessage.ConnectionSuccessMessageBody;
import models.ServerMessage.CreateLobbyMessageBody;
import models.ServerMessage.LobbyListMessageBody;
import models.ServerMessage.Message;
import models.ServerMessage.MessageType;
import models.ServerMessage.MoveMessageBody;
import models.ServerMessage.NewGameMessageBody;
import models.ServerMessage.PlayAgainMessageBody;
import models.ServerMessage.PlayerJoinedMessageBody;
import models.ServerMessage.PlayerLeaveMessageBody;
import models.ServerMessage.SpectateJoinMessageBody;
import models.ServerMessage.SpectateLeaveMessageBody;
import models.ServerMessage.SpectateSuccessMessageBody;

import org.javatuples.Pair;

@ClientEndpoint
public class LobbyService extends AbstractWebsocketService {

    /*==========================================================================================================
     * CLASS VARIABLES
     *==========================================================================================================*/

    private boolean authenticated;

    /** JWT which authenticates the user and provides proof of their claims. */
    private String jwt = null;

    private UUID lobbyId = null;

    private Consumer<ArrayList<Lobby>> onConnectCallback;
    private CallBackable onLobbyCallback;
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

    public void disconnect(){
        this.jwt = null;
        this.lobbyId = null;
        this.player = null;

        try {
            this.session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setJwt(String jwt){
        this.jwt = jwt;
    }

    /*==========================================================================================================
     * LOBBY MANAGEMENT
     *==========================================================================================================*/

    public void createLobby(String name, boolean ai, CallBackable onLobbyCallback) throws Exception{
        if(this.authenticated){
            this.onLobbyCallback = onLobbyCallback;
            CreateLobbyMessageBody body = new CreateLobbyMessageBody(jwt, name, ai);
            send(new Message(body, MessageType.CREATE_LOBBY));
        } else{
            throw new Exception("Not Authenticated");
        }
    }

    public void joinLobby(UUID lobbyId, CallBackable onLobbyCallback) throws Exception{
        if(this.authenticated){
            this.onLobbyCallback = onLobbyCallback;
            ConnectionMessageBody body = new ConnectionMessageBody(
                jwt,
                lobbyId, 
                player.getUuid(), 
                ConnectionMessageBody.Type.JOIN
            );
            send(new Message(body, MessageType.CONNECTION));
        } else{
            throw new Exception("Not Authenticated");
        }
    }

    public void leaveLobby() throws Exception{
        if(this.authenticated && lobbyId != null){
            ConnectionMessageBody body = new ConnectionMessageBody(
                jwt,
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
        if(this.authenticated){
            this.onLobbyListCallback = onLobbyListCallback;
            send(new Message("", MessageType.LOBBY_LIST));
        } else{
            throw new Exception("Not Authenticated");
        }
    }

    public void move(int x, int y) throws Exception{
        if(this.authenticated){
            MoveMessageBody body = new MoveMessageBody(jwt, lobbyId, new Pair<Integer, Integer>(x, y));
            send(new Message(body, MessageType.MOVE));
        } else{
            throw new Exception("Not Authenticated");
        }
    }

    public void playAgain() throws Exception{
        if(this.authenticated){
            PlayAgainMessageBody body = new PlayAgainMessageBody(lobbyId, jwt);
            send(new Message(body, MessageType.PLAY_AGAIN));
        } else{
            throw new Exception("Not Authenticated");
        }
    }

    public void spectate(UUID lobbyId, CallBackable onLobbyCallback) throws Exception{
        if(this.authenticated){
            this.onLobbyCallback = onLobbyCallback;
            SpectateJoinMessageBody body = new SpectateJoinMessageBody(
                lobbyId, 
                player.getUuid()
            );
            send(new Message(body, MessageType.SPECTATE_JOIN));
        } else{
            throw new Exception("Not Authenticated");
        }
    }

    public void stopSpectating() throws Exception{
        if(this.authenticated && lobbyId != null){
            SpectateLeaveMessageBody body = new SpectateLeaveMessageBody(
                lobbyId, 
                player.getUuid()
            );
            send(new Message(body, MessageType.SPECTATE_LEAVE));
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
                    AuthenticationResultMessageBody authResultBody = gson.fromJson(message.getBody(), AuthenticationResultMessageBody.class);
                    if(authResultBody.getSuccess()){
                        this.authenticated = true;
                        send(new Message("", MessageType.AUTHENTICATION_ACKNOWLEDGED));
                    } else {
                        this.authenticated = false;
                    }
                    break;
                case CONNECTION_FAILURE:
                    break;
                case CONNECTION_SUCCESS:
                    ConnectionSuccessMessageBody connSuccessBody = gson.fromJson(message.getBody(), ConnectionSuccessMessageBody.class);


                    if(connSuccessBody.getType() == ConnectionMessageBody.Type.JOIN){
                        if(connSuccessBody.getGameState() != null && connSuccessBody.getGameState().getPlayers() != null){
                            Player[] connSuccessPlayers = gson.fromJson(connSuccessBody.getGameState().getPlayers().toString(), Player[].class);
                            connSuccessBody.getGameState().setPlayerOne(connSuccessPlayers[0]);
                            connSuccessBody.getGameState().setPlayerTwo(connSuccessPlayers[1]);
    
                            GameStateService.getInstance().setGameState(connSuccessBody.getGameState());
                            this.onLobbyCallback.callback();
                        }
                        
                        this.lobbyId = connSuccessBody.getLobbyId();
                    } else{
                        this.lobbyId = null;
                    }

                    break;
                case LOBBY_LIST:
                    LobbyListMessageBody lobbyListBody = gson.fromJson(message.getBody(), LobbyListMessageBody.class);

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
                case PLAYER_JOINED:
                    PlayerJoinedMessageBody joinBody = gson.fromJson(message.getBody(), PlayerJoinedMessageBody.class);
                    GameState joinGameState = GameStateService.getInstance().getGameState();
                    
                    if(joinBody.getPosition() == 0){
                        joinGameState.setPlayerOne(joinBody.getPlayer());
                    } else{
                        joinGameState.setPlayerTwo(joinBody.getPlayer());
                    }
                    break;
                case PLAYER_LEFT:
                    PlayerLeaveMessageBody leaveBody = gson.fromJson(message.getBody(), PlayerLeaveMessageBody.class);
                    GameState leaveGameState = GameStateService.getInstance().getGameState();

                    if(leaveBody.getPosition() == 0){
                        leaveGameState.setPlayerOne(null);
                    } else{
                        leaveGameState.setPlayerTwo(null);
                    }
                    break;
                case MOVE:
                    MoveMessageBody moveBody = gson.fromJson(message.getBody(), MoveMessageBody.class);
                    GameState moveGameState = GameStateService.getInstance().getGameState();
                    moveGameState.setCell(moveBody.getMove().getValue0(), moveBody.getMove().getValue1());
                    break;
                case NEW_GAME:
                    NewGameMessageBody newGameBody = gson.fromJson(message.getBody(), NewGameMessageBody.class);
                    Player[] newGamePlayers = gson.fromJson(newGameBody.getGameState().getPlayers().toString(), Player[].class);
                    newGameBody.getGameState().setPlayerOne(newGamePlayers[0]);
                    newGameBody.getGameState().setPlayerTwo(newGamePlayers[1]);
                    GameStateService.getInstance().setGameState(newGameBody.getGameState());
                    break;
                case SPECTATE_SUCCESS:
                    SpectateSuccessMessageBody specSuccessBody = gson.fromJson(message.getBody(), SpectateSuccessMessageBody.class);
                    Player[] specSuccessPlayers = gson.fromJson(specSuccessBody.getGameState().getPlayers().toString(), Player[].class);
                    specSuccessBody.getGameState().setPlayerOne(specSuccessPlayers[0]);
                    specSuccessBody.getGameState().setPlayerTwo(specSuccessPlayers[1]);
                    this.lobbyId = specSuccessBody.getLobbyId();

                    GameStateService.getInstance().setGameState(specSuccessBody.getGameState());
                    this.onLobbyCallback.callback();
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
