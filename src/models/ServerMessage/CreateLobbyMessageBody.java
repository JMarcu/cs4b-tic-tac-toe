package models.ServerMessage;

public class CreateLobbyMessageBody {
    private String jwt;
    private String name;

    public CreateLobbyMessageBody(String jwt, String name){
        this.jwt = jwt;
        this.name = name;
    }

    public String getJWT(){ return jwt; }
    public String getName(){ return name; }
}
