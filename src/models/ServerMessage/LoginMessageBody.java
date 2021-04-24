package models.ServerMessage;

public class LoginMessageBody {
    private String password;
    private String username;

    public LoginMessageBody(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getPassword(){ return password; }
    public String getUsername(){ return username; }
}