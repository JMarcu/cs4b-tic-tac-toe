package models.ServerMessage;

import java.util.ArrayList;

import models.Lobby;

public class LobbyListMessageBody {
    private ArrayList<Lobby> lobbies;

    public LobbyListMessageBody(){
        this(new ArrayList<Lobby>());
    }

    public LobbyListMessageBody(ArrayList<Lobby> lobbies){
        this.lobbies = lobbies;
    }

    public ArrayList<Lobby> getLobbies(){
        return lobbies;
    }
}
