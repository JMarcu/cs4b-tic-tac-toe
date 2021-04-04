package models.ServerMessage;

public enum MessageType {
    AUTHENTICATION_SUCCESS, // Defines the player who is logging in. 
    AUTHENTICATION_REQUEST, // Request from the client to authenticate.
    AUTHENTICATION_ACKNOWLEDGED, // Final step in handshake, client is ready for messages.
    CHAT, 
    CONNECTION,
    CREATE_LOBBY,
    LOBBY_LIST,
    MOVE,
    PLAYER_PROPERTIES,
    REQUEST_PLAYER,
}
