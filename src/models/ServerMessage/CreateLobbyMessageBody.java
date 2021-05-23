package models.ServerMessage;

public class CreateLobbyMessageBody {
    private boolean ai;
    private String jwt;
    private String name;

    public CreateLobbyMessageBody(String jwt, String name, boolean ai){
        this.ai = ai;
        this.jwt = jwt;
        this.name = name;
    }

    public String getJWT(){ return jwt; }
    public String getName(){ return name; }
    public boolean isAi() { return ai; }
}
