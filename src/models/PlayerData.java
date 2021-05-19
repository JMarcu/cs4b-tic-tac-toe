package models;

import javafx.scene.paint.Color;

public class PlayerData {

    /** The color of the player's marker. */
    protected SerializeableColor color;

    /** The player's name. */
    protected String name;

    /** The shape of the player's marker. */
    protected MarkerShape shape;

    public PlayerData(SerializeableColor color, String name, MarkerShape shape){
        this.color = color;
        this.name = name;
        this.shape = shape;
    }
        
    /**
     * Returns the color of the player's marker.
     * @return The color of the player's marker, or null if the color has not changed since the last patch.
     */
    public Color getColor() { return color.getColor(); }

    /**
     * Returns the player's name.
     * @return The player's name, or null if the player's name has not changed since the last patch.
     */
    public String getName() { return name; }

    /**
     * Returns the shape of the player's marker.
     * @return The shape of the player's marker, or null if the shape has not changed since the last patch.
     */
    public MarkerShape getShape() { return shape; }
    
}
