package models.ServerMessage;

public class RefreshSuccessMessageBody {
    private String jwt;

    public RefreshSuccessMessageBody(String jwt){
        this.jwt = jwt;
    }

    public String getJWT(){ return jwt; }
}
