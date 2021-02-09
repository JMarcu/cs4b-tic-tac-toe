package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum GameMode {
    BEST_OF_X ("Best of X"),
    BULLET ("Bullet"),
    FREE_PLAY ("Free Play"),
    MULTI ("Multi"),
    RECURSIVE ("Recursive");

    private final String stringRep;

    GameMode(String stringRep){
        this.stringRep = stringRep;
    }

    @Override
    public String toString() {
        return this.stringRep;
    }

    public static GameMode fromString(String str){
        switch(str){
            case "Best of X": return GameMode.BEST_OF_X;
            case "Bullet": return GameMode.BULLET;
            case "Free Play": return GameMode.FREE_PLAY;
            case "Multi": return GameMode.MULTI;
            case "Recursive": return GameMode.RECURSIVE;
            default: return null;
        }
    }

    public static ObservableList<String> toObservableArray(){
        return FXCollections.observableArrayList("Best of X", "Bullet", "Free Play", "Multi", "Recursive");
    }
}
