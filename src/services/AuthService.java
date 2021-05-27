package services;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import models.Lobby;
import models.Player;
import models.ServerMessage.AuthenticationResultMessageBody;
import models.ServerMessage.LoginMessageBody;
import models.ServerMessage.LoginSuccessMessageBody;
import models.ServerMessage.LogoutMessageBody;
import models.ServerMessage.Message;
import models.ServerMessage.MessageType;
import models.ServerMessage.RegisterMessageBody;
import models.ServerMessage.RegistrationResultMessageBody;
import models.ServerMessage.RequestPlayerMessageBody;
import models.ServerMessage.RequestedPlayerMessageBody;

/**
 * Facilitates communication between the client and the AuthService on the server.
 * This service is responsible for user authentication and authorization, as well  as player state.
 * The service follows a singleton pattern, and only one instance is allowed to exist per client. 
 * To get this instance, call {@link getInstance}.
 * @author James Marcu
 */
@ClientEndpoint
public class AuthService extends AbstractWebsocketService {
    /*==========================================================================================================
     * NESTED OBJECTS
     *==========================================================================================================*/

    /** 
     * Defines the result of a registration attempt. If the registration fails, the code hopefully indicates
     * the reason why it has failed so that a helpful message can be displayed to the user, allowing them
     * to fix the issue.
     */
    public enum RegistrationResult {
        /** Indicates the user's provided password does not meet the server's defined requirements for a valid password. */
        PASSWORD_FAILS_REQUIREMENTS,

        /** Indicates registration has failed but no other code defines the reason why. */
        UNKNOWN_ERROR,

        /** Indicates the user has selected a username which has already been selected by another user. Usernames must be unique. */
        USERNAME_ALREADY_EXISTS,

        /** Indicates the user has successfully registered with their provided credentials. */
        SUCCESS
    }

    /*==========================================================================================================
     * CLASS VARIABLES
     *==========================================================================================================*/

    /** JWT which authenticates the user and provides proof of their claims. */
    private String jwt = null;

    /** A local instance representing the currently logged in player. */
    private Player player = null;

    /** Refresh token used to authorize JWT refresh requests to the auth service. */
    private String refreshToken = null;

    /** A callback that is set when the user attempts to log in and invoked when the attempt completes or fails. */
    private Consumer<Player> onLoginCallback = null;

    /** A callback that is set when the user attempts to log out and invoked when the attempt completes or fails. */
    private Consumer<Boolean> onLogoutCallback = null;

    private HashMap<UUID, Consumer<Player.Patch>> onRequestPlayerCallbackMap;

    /** A callback that is set when the user attempts to register and invoked when registration completes or fails. */
    private BiConsumer<RegistrationResult, Player> onRegistrationCallback = null;

    /** The singleton */
    private static AuthService instance = null;

    /*==========================================================================================================
     * LIFECYCLE
     *==========================================================================================================*/

    /** Default constructor; player is uninitialized and connects to the domain 'herokuapp.com'. */
    protected AuthService(){
        super("cs4b-tic-tac-toe-auth-service");
    }

    /**
     * Player is uninitialized, and connects to the specified domain.
     * @param domain The remote server's domain.
     */
    protected AuthService(String domain){
        super("cs4b-tic-tac-toe-auth-service", domain);
    }

    /**
     * Initializes the internal player model and connects to the default domain 'herokuapp.com'.
     * @param player The player model to initialze with.
     */
    protected AuthService(Player player){
        super("cs4b-tic-tac-toe-auth-service");
        this.player = player;
    }

    /**
     * Sets the internal player model to the provided value and connects to the specified domain.
     * @param player The player model to use internally.
     * @param domain The remote server's domain.
     */
    protected AuthService(Player player, String domain){
        super("cs4b-tic-tac-toe-auth-service", domain);
        this.player = player;
    }

    /** 
     * Returns the client's instance of AuthService. If none yet exists, one will be instantiated using
     * default values. The internal player will be set to null and the service will attempt to connect
     * to a server at the domain 'herokuapp.com'.
     * @return Returns the client's instance of AuthService.
     */
    public static AuthService getInstance(){
        if(AuthService.instance == null){
            AuthService.instance = new AuthService();
        }
        return instance;
    }

    /** 
     * Returns the client's instance of AuthService. 
     * If none yet exists, one will be instantiated. The interal player will be null, and the service will 
     * attempt to connect to the provided domain.
     * If an instance already exists, the provided domain parameter is ignored.
     * @param domain The remote server's domain.
     * @return Returns the client's instance of AuthService.
     */
    public static AuthService getInstance(String domain){
        if(AuthService.instance == null){
            AuthService.instance = new AuthService(domain);
        }
        return instance;
    }

    /** 
     * Returns the client's instance of AuthService. 
     * If none yet exists, one will be instantiated. The interal player will be set to the provided value, 
     * and the service will attempt to connect to the the default domain 'herokuapp.com'.
     * If an instance already exists, the provided player parameter is ignored.
     * @param player The player model to initialze with.
     * @return Returns the client's instance of AuthService.
     */
    public static AuthService getInstance(Player player){
        if(AuthService.instance == null){
            AuthService.instance = new AuthService(player);
        }
        return instance;
    }

    /** 
     * Returns the client's instance of AuthService. 
     * If none yet exists, one will be instantiated. The interal player will be set to the provided value, 
     * and the service will attempt to connect to the provided domain.
     * If an instance already exists, the provided parameters are ignored.
     * @param player The player model to initialze with.
     * @return Returns the client's instance of AuthService.
     */
    public static AuthService getInstance(Player player, String domain){
        if(AuthService.instance == null){
            AuthService.instance = new AuthService(player, domain);
        }
        return instance;
    }

    /*==========================================================================================================
     * STATE
     *==========================================================================================================*/

    public Player getPlayer(){
        return this.player;
    }

    /*==========================================================================================================
     * SESSION MANAGEMENT
     *==========================================================================================================*/

    /**
     * Attempts to log the user in with the provided credentials.
     * @param username The user's username.
     * @param password The user's password.
     * @param onLoginCallback This consumer's 'accept' method will be invoked once the login attempt completes
     * (or fails). The provided argument will be true if the login was successful, false otherwise.
     * @throws Exception Only one attempt to log in may be active at a time. If this method is invoked before
     * the callback supplied to a previous invocation has been invoked then an exception will be thrown.
     */
    public void login(String username, String password, Consumer<Player> onLoginCallback) throws Exception {
        if(this.onLoginCallback != null){
            /* If we are already attempting to log the user in, then throw an exception. Only one attempt to log
            in can be active at a time. */
            throw new Exception("Only one attempt to log in may be active at a time.");
        } else{
            //Otherwise, store the callback so we can invoke it when the server responds to our login attempt.
            this.onLoginCallback = onLoginCallback;
        }

        //Construct the login request message.
        final Message loginMessage = new Message(
            new LoginMessageBody(username, password),
            MessageType.LOGIN
        );
        
        try {
            //Send the request to the server.
            this.send(loginMessage);
        } catch (IOException e) {
            //If we fail to send the message for some reason, invoke the callback with failed status.
            e.printStackTrace();
            invokeLoginCallback(null);
        }
    }

    /** 
     * Logs the user out of their session with the auth service. Does not close the websocket session. 
     * @param onLogoutCallback This consumer's 'accept' method will be invoked once the log out attempt 
     * completes (or fails). The provided argument will be true if the logout was successful, false otherwise.
     * @throws Exception Only one attempt to log out may be active at a time. If this method is invoked before
     * the callback supplied to a previous invocation has been invoked then an exception will be thrown.
     */
    public void logout(UUID playerId, Consumer<Boolean> onLogoutCallback) throws Exception {
        //If the refresh token is null then we are not logged in and this method does nothing.
        if(refreshToken != null){
            if(this.onLogoutCallback != null){
                /* If we are already attempting to log the user out, then throw an exception. Only one attempt to log
                out can be active at a time. */
                throw new Exception("Only one attempt to log out may be active at a time.");
            } else {
                //Otherwise, store the callback so we can invoke it when the server responds to our login attempt.
                this.onLogoutCallback = onLogoutCallback;
            }

            //Construct the logout request message.
            final Message logoutMessage = new Message(new LogoutMessageBody(refreshToken, playerId), MessageType.LOGOUT);
            
            try {
                //Send the request to the server.
                this.send(logoutMessage);
            } catch (IOException e) {
                //If we fail to send the message for some reason, invoke the callback with failed status.
                e.printStackTrace();
                invokeLogoutCallback(false);
            }
        }
    }

    /**
     * Attempts to register a new user account with the AuthService.
     * @param username The user's chosen username. Registration will fail if the username is not unique.
     * @param password The users's chosen password. If the server is configured with password requirements,
     * the password must satisfy them or else the registration will fail.
     * @param onRegisterCallback This consumer's 'accept' method will be invoked once registration completes
     * (or fails). The provided argument will be a {@link RegistrationResult} value indicating the status
     * of the user's registration.
     * @throws Exception Only one attempt to register may be active at a time. If this method is invoked before
     * the callback supplied to a previous invocation has been invoked then an exception will be thrown.
     */
    public void register(String username, String password, BiConsumer<RegistrationResult, Player> onRegisterCallback) throws Exception {
        if(this.onRegistrationCallback != null){
            /* If we are already attempting to register the user, then throw an exception. Only one attempt to register
            can be active at a time. */
            throw new Exception("Only one attempt to register may be active at a time.");
        } else{
            //Otherwise, store the callback so we can invoke it when the server responds to our registration request.
            this.onRegistrationCallback = onRegisterCallback;
        }

        //Construct the registration request message.
        final Message registerMessage = new Message(
            new RegisterMessageBody(username, password),
            MessageType.REGISTER
        );
        
        try {
            //Send the request to the server.
            this.send(registerMessage);
        } catch (IOException e) {
            //If we fail to send the message for some reason, invoke the callback with an UNKOWN_ERROR status.
            e.printStackTrace();
            invokeRegisterCallback(RegistrationResult.UNKNOWN_ERROR, null);
        }
    }

    /** 
     * Invokes the callback set when the user attempts to log in and handles the associated cleanup.
     * @param loginResult Whether or not the login succeeded.
     */
    private void invokeLoginCallback(Player player){
        if(this.onLoginCallback != null){
            /* Store a local-scoped copy of the callback, then set the instance-scoped reference to null.
            The callback might invoke the 'login' method, which will throw an exception if our 
            instance-scoped copy is not null, so it's important that it be set to null before invoking
            the callback */
            Consumer<Player> tempLoginCB = this.onLoginCallback;
            this.onLoginCallback = null;

            //Invoke the callback.
            tempLoginCB.accept(player);
        }
    }

    /** 
     * Invokes the callback set when the user attempts to log out and handles the associated cleanup.
     * @param loginResult Whether or not the logout succeeded.
     */
    private void invokeLogoutCallback(boolean logoutResult){
        if(this.onLogoutCallback != null){
            /* Store a local-scoped copy of the callback, then set the instance-scoped reference to null.
            The callback might invoke the 'logout' method, which will throw an exception if our 
            instance-scoped copy is not null, so it's important that it be set to null before invoking
            the callback */
            Consumer<Boolean> tempLogoutCB = onLogoutCallback;
            this.onLogoutCallback = null;

            //Invoke the callback.
            tempLogoutCB.accept(logoutResult);
        }
    }
    
    /**
     * Invokes the callback set when the user attempts to register and handles the associated cleanup.
     * @param registerResult A result code defining whether the registration attempt succeeded and, if not, why
     * it failed.
     */
    private void invokeRegisterCallback(RegistrationResult registerResult, Player player){
        if(this.onRegistrationCallback != null){
            /* Store a local-scoped copy of the callback, then set the instance-scoped reference to null.
            The callback might invoke the 'register' method, which will throw an exception if our 
            instance-scoped copy is not null, so it's important that it be set to null before invoking
            the callback */
            BiConsumer<RegistrationResult, Player> tempRegResultCB = this.onRegistrationCallback;
            this.onRegistrationCallback = null;

            //Invoke the callback.
            tempRegResultCB.accept(registerResult, player);
        }
    }

    /** 
     * 
     * @param 
     */
    private void invokeRequestPlayerCallback(Player player){
        // if(this.onRequestPlayerCallbackMap != null){
        //     /* Store a local-scoped copy of the callback, then set the instance-scoped reference to null.
        //     The callback might invoke the 'login' method, which will throw an exception if our 
        //     instance-scoped copy is not null, so it's important that it be set to null before invoking
        //     the callback */
         
        //     HashMap<UUID, Consumer<Player.Patch>> tempRequestPlayerCB =  this.onRequestPlayerCallbackMap;
        //     this.onRequestPlayerCallbackMap = null;

        //     //Invoke the callback.
        //     tempRequestPlayerCB.put(player.getUuid(), );
        // }
    }

    /*==========================================================================================================
     * PROFILES
     *==========================================================================================================*/


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
        switch(message.getType()){
            case AUTHENTICATION_RESULT:
                //Deserialize the body.
                AuthenticationResultMessageBody authResultBody = GSON.fromJson(message.getBody(), AuthenticationResultMessageBody.class);

                if(!authResultBody.getSuccess()){
                    /* A failed authentication result indicates a failure to change session status. If we have any pending
                    requests of that nature (i.e. login or logout), invoke their callbacks. Else, ignore the message. */
                    this.invokeLoginCallback(null);
                    this.invokeLogoutCallback(false);
                } else{
                    //Do nothing. There is currently no reason to receive a successful authentication result message 
                    //from the auth service. On a successfull login, instead expect a LOGIN_SUCCESS message.
                }
                break;
            case LOGIN_FAIL:
                invokeLoginCallback(null);
                break;
            case LOGIN_SUCCESS:
                //Deserialize the body.
                LoginSuccessMessageBody loginSuccessBody = GSON.fromJson(message.getBody(), LoginSuccessMessageBody.class);

                //Store the auth tokens.
                this.jwt = loginSuccessBody.getJWT();
                this.player = loginSuccessBody.getPlayer();
                this.refreshToken = loginSuccessBody.getRefreshToken();

                LobbyService.getInstance().connect(
                    jwt, 
                    loginSuccessBody.getPlayer(),
                    new Consumer<ArrayList<Lobby>>(){
                        @Override
                        public void accept(ArrayList<Lobby> t) {
                            //Invoke the callback.
                            invokeLoginCallback(loginSuccessBody.getPlayer());
                        }
                    }
                );

                break;
            case LOGOUT_SUCCESS:
                //If we haven't stored a callback for handling a logout, ignore the message.
                if(this.onLogoutCallback != null){
                    //Clear our auth tokens.
                    this.jwt = null;
                    this.player = null;
                    this.refreshToken = null;

                    //Invoke the callback.
                    invokeLogoutCallback(true);
                }
                break;
            case REFRESH_SUCCESS:
                //TODO Handle successful refresh.
                break;
            case REGISTRATION_RESULT:
                //Deserialize the message body.
                RegistrationResultMessageBody regResultBody = GSON.fromJson(message.getBody(), RegistrationResultMessageBody.class);

                //Invoke the callback.
                invokeRegisterCallback(regResultBody.getResult(), regResultBody.getPlayer());
                break;
            case REQUEST_PLAYER:
                RequestPlayerMessageBody requestPlayerBody = gson.fromJson(message.getBody(), RequestPlayerMessageBody.class);

                try {
                    this.send(new Message(requestPlayerBody.getPlayerId(), MessageType.REQUEST_PLAYER));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    
                    System.out.println("REQUEST PLAYER");
                    e.printStackTrace();
                }
                break;
            case REQUESTED_PLAYER:
                RequestedPlayerMessageBody requestedPlayerBody = gson.fromJson(message.getBody(), RequestedPlayerMessageBody.class);
                

                if(requestedPlayerBody.getPlayer() != null){
                    System.out.println("Player Received");
                    System.out.println(requestedPlayerBody.getPlayer().getName());
                    System.out.println(requestedPlayerBody.getPlayer().getUuid());
                }
                else{
                    System.out.println("The player is null");
                }
            
                break;
        }
    }

    /** No opening logic required. */
    @Override
    protected void handleOpen(Session session) { }
}
