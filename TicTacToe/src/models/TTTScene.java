package models;

public enum TTTScene {
    GAME_BOARD ("game-board"),
    MAIN_MENU ("main-menu"),
    OPTIONS_MENU ("OptionsMenu"),
    SHAPE_PICKER ("ShapeColorPicker"),
    TIC_TAC_TOE_BOARD ("Board");

    private final String name;
    
    TTTScene(String name){
        this.name = name;
    }
    
    public String getName(){return this.name;}
}
