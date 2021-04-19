package models;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class SerializeableColor implements Serializable{
    private static final long serialVersionUID = 1L;

    double red;
    double green;
    double blue;
    double opacity;

    public SerializeableColor(double red, double green, double blue, double opacity){
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.opacity = opacity;
    }

    public SerializeableColor(Color color){
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.opacity = color.getOpacity();
    }

    public Color getColor(){
        return new Color(red, green, blue, opacity);
    }

    public void setColor(Color color){
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.opacity = color.getOpacity();
    }
}