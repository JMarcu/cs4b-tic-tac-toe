package models.ServerMessage;

public class CreateLobbyMessageBody {
    private String name;

    public CreateLobbyMessageBody(){
        this("Lobby");
    }

    public CreateLobbyMessageBody(String name){
        this.name = name;
    }

    public String getName(){ return name; }
}
