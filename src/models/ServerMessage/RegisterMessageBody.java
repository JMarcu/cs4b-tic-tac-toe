package models.ServerMessage;

public class RegisterMessageBody {
    private String password;
    private String username;

    public RegisterMessageBody(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getPassword(){ return password; }
    public String getUsername(){ return username; }
}
