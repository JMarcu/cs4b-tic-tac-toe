package models;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

public enum Color {
    BLACK (1, -1, 1, -0.45), 
    BLUE (1, 0, 1, -0.7), 
    GREEN (1, 0, 1, 0.66), 
    ORANGE (1, 0, 1, 0.17), 
    PINK (1, 0, 1, -0.22), 
    PURPLE (1, 0, 1, -0.45), 
    RED (1, 0, 1, 0), 
    TEAL (1, 0, 1, 1), 
    YELLOW (1, 0, 1, 0.33);

    final double brightness;
    final double contrast;
    final double hue;
    final double saturation;

    Color(double contrast, double brightness, double saturation, double hue){
        this.brightness = brightness;
        this.contrast = contrast;
        this.hue = hue;
        this.saturation = saturation;
    }

    public static void adjustImageColor(ImageView iv, Color color){
        ColorAdjust adjuster = new ColorAdjust();
        adjuster.setBrightness(color.brightness);
        adjuster.setContrast(color.contrast);
        adjuster.setSaturation(color.saturation);
        adjuster.setHue(color.hue);
        iv.setEffect(adjuster);
    }
}
