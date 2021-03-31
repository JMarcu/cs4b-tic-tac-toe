package models.ServerMessage;

import java.io.Serializable;

public class CreateLobby implements Serializable {
    private static final long serialVersionUID = 1L;

    String name;

    public CreateLobby(String name){
        this.name = name;
    }

    public String getName(){return this.name;}
}