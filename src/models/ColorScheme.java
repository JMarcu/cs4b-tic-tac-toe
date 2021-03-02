package models;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public enum ColorScheme {
    PRIMARY (Color.web("#455a64", 1)),
    PRIMARY_DARK (Color.web("#718792", 1)),
    PRIMARY_LIGHT (Color.web("#1c313a", 1)),
    SECONDARY (Color.web("#303f9f", 1)),
    SECONDARY_DARK (Color.web("#666ad1", 1)),
    SECONDARY_LIGHT (Color.web("#001970", 1)),
    TEXT_ON_PRIMARY (Color.web("#eeeeee", 1)),
    TEXT_ON_SECONDARY (Color.web("#ffffff", 1));

    private final Color color;

    ColorScheme(Color color){
        this.color = color;
    }
    
    public Color getColor(){
        return this.color;
    }

    public static void adjustImageColor(ImageView iv, Color color){
        ColorAdjust adjuster = new ColorAdjust();
        adjuster.setBrightness(color.getBrightness());
        adjuster.setSaturation(color.getSaturation());
        adjuster.setHue(color.getHue());
        iv.setEffect(adjuster);
    }
}
