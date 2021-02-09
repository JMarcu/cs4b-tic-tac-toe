package models;

public enum TTTScene {
    MAIN_MENU ("main-menu"),
    OPTIONS_MENU ("options-menu"),
    SHAPE_PICKER ("shape-picker");

    private final String name;
    
    TTTScene(String name){
        this.name = name;
    }
    
    public String getName(){return this.name;}
}
