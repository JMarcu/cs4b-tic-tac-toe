package models.ServerMessage;

import models.MarkerShape;
import models.SerializeableColor;

public class UpdatePlayerMessageBody {
    SerializeableColor color;
    String jwt;
    String name;
    MarkerShape shape;

    public UpdatePlayerMessageBody(SerializeableColor color, String jwt, String name, MarkerShape shape){
        this.color = color;
        this.jwt = jwt;
        this.name = name;
        this.shape = shape;
    }

    public SerializeableColor getColor() {
        return color;
    }

    public String getJwt() {
        return jwt;
    }

    public String getName() {
        return name;
    }

    public MarkerShape getShape() {
        return shape;
    }
}
