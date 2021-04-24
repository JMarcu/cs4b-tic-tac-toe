package models;

public enum Environment {
    HEROKU,
    LOCALHOST;
    
    public static Environment fromString(String str){
        switch(str){
            case "heroku":
                return HEROKU;
            case "localhost":
                return LOCALHOST;
            default:
                return null;
        }
    }
    

}
