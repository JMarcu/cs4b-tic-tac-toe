package models;

import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
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

    public static void adjustImageColor(Node node, Color color){
        double hue;
        double brightness = 0;
        double saturation = 1;

        switch(color.toString()){
            case "0x0000ffff": //BLUE
                hue = -.7;
                break;
            case "0xffff00ff": //YELLOW
                hue = .33;
                brightness = -.1;
                break;
            case "0x008000ff": //GREEN
                brightness = -.1;
                hue = .66;
                break;
            case "0x008080ff": //TEAL
                brightness = -.1;
                hue = 1.0;
                break;
            case "0x800080ff": //PURPLE
                hue = -.45;
                break;
            case "0xff0000ff": //RED
                hue = 0.0;
                break;
            case "0xffa500ff": //ORANGE
                hue = .17;
                break;
            case "0xffc0cbff": //PINK
                hue = -.22;
                break;
            default: //BLACK
                hue = 0;
                brightness = -1;
        }

        ColorAdjust adjuster = new ColorAdjust();

        adjuster.setBrightness(brightness);
        adjuster.setSaturation(saturation);
        adjuster.setHue(hue);
        node.setEffect(adjuster);
    }
}
