package services;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import interfaces.Sender;
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
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import models.ServerMessage.Message;

/**
 * Base class for services which connect to some backend service over a websocket.
 * @author James Marcu
 */
@ClientEndpoint
public abstract class AbstractWebsocketService extends Thread implements Sender {

    /** The client's websocket container, provided by Tomcat. */
    protected WebSocketContainer container;

    protected Gson gson;

    /** The websocket session with the remote AuthService. */
    protected Session session;
    
    /** The domain of the remote host. */
    protected final String DOMAIN;

    /** A Gson instance for serialization and deserialation. */
    protected final Gson GSON = new Gson();

    /** The subdomain of the service on the remote. */
    protected final String SUBDOMAIN;
    
    /** The protocol to use when establishing a websocket handshake. */
    protected final String WEBSOCKET_PROTOCOL;

    /** The route which should be targetted when initiating a websocket handshake. */
    protected final String WEBSOCKET_ROUTE = "ws";

    /*==========================================================================================================
     * LIFECYCLE
     *==========================================================================================================*/

    /** Default constructor; attempts to connect to the domain "herokuapp.com/". */
    protected AbstractWebsocketService(String subdomain){
        this("herokuapp.com/", subdomain);
    }

    /** 
     * Instantiate a connection to a specific domain. Localhost domains will disable SSL.
     * @param domain The host domain to connect to.
     */
    protected AbstractWebsocketService(String domain, String subdomain){
        this.DOMAIN = domain;
        this.SUBDOMAIN = subdomain;
        this.WEBSOCKET_PROTOCOL = domain.contains("localhost") ? "ws://" : "wss://";
        
        this.gson = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .setExclusionStrategies(new ExclusionStrategy(){
                @Override
                public boolean shouldSkipField(FieldAttributes f){
                    boolean exclude = false;
                    try{
                        exclude = f.getName().equals("serialVersionUID");
                    } catch(Exception err){ }
                    return exclude;
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .create();

        Thread clientThread = new Thread(this);
        clientThread.start();
    }

    /*==========================================================================================================
     * CLIENT ENDPOINT
     *==========================================================================================================*/

    /**
     * This method is invoked when the websocket session is terminated. Inheriting classes should 
     * implement their closure logic here. 
     * @param session The session object representing the websocket conversation which was just closed.
     * @param closeReason Defines the manner in which the session has ended.
     */
    protected abstract void handleClose(Session session, CloseReason closeReason);

    /** 
     * This method is invoked upon receipt of a message across the websocket connection. Inheriting
     * classes should implement their message handling logic here. 
     * Messages are deserialized prior to invocation and will be sent in as fully instantiated 
     * {@link Message} objects. However, the 'body' property of the message will be raw and likely
     * represents the serialized version of some other object.
     * @param session Represents the websocket conversation which the message is a part of.
     * @param messageString The received message, serialized as a string.
     */
    protected abstract void handleMessage(Session session, Message message);

    /** 
     * This method is invoked when the websocket connection is opened. Inheriting classes should
     * implement their opening logic here. By the time this method is invoked, the service's session 
     * property has already been set to be equal to the Session argument provided to the method.
     * @param session The session object representing the websocket conversation.
     */
    protected abstract void handleOpen(Session session);

    /** 
     * Annotated method for handling the closing of a websocket session as a client endpoint.
     * Actual handling of the message is delegated to the {@link handleClose} method so that 
     * inheriting classes do not need to reimplement annotated handlers.
     * @param session The session object representing the websocket conversation which was just closed.
     * @param closeReason Defines the manner in which the session has ended.
     */
    @OnClose
    public void onClose(Session session, CloseReason closeReason){
        this.handleClose(session, closeReason);
    }
    
    /** 
     * Annotated method for handling the receipt of a message through a websocket session as a 
     * client endpoint. Actual handling of the message is delegated to the {@link handleMessage} 
     * method so that inheriting classes do not need to reimplement annotated handlers, but the
     * message *is* deserialized prior to handoff.
     * @param session Represents the websocket conversation which the message is a part of.
     * @param messageString The received message, serialized as a string.
     */
    @OnMessage
    public void onMessage(Session session, String messageString){
        System.out.println("Message Received: " + messageString);
        Message message = GSON.fromJson(messageString, Message.class);
        this.handleMessage(session, message);
    }

    /** 
     * Annotated method for handling the opening of a websocket session as a client endpoint. 
     * Stores the session object on the service instance, then passes handling off to the
     * {@link handleOpen} method.
     * @param session The session object representing the websocket conversation.
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        this.handleOpen(session);
    }

    /*==========================================================================================================
     * SENDER
     *==========================================================================================================*/

     /** 
      * Dispatches a method over the websocket connection to the server.
      * @param message The message to send.
      * @throws IOExecption Thrown if there is an error operating over the websocket channel.
      */
    public void send(Message message) throws IOException{
        System.out.println("Sending Message: " + gson.toJson(message));
        session.getBasicRemote().sendText(gson.toJson(message));
    }

    /*==========================================================================================================
     * THREAD
     *==========================================================================================================*/
    @Override
    public void run() {
        container = ContainerProvider.getWebSocketContainer();

        try {
            StringBuilder uriBuilder = new StringBuilder();
            uriBuilder.append(WEBSOCKET_PROTOCOL);
            uriBuilder.append(SUBDOMAIN);
            uriBuilder.append(".");
            uriBuilder.append(DOMAIN);
            uriBuilder.append(WEBSOCKET_ROUTE);

            session = container.connectToServer(this, new URI(uriBuilder.toString()));
        } catch (DeploymentException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
