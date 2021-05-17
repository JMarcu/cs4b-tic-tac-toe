package models.ServerMessage;

import java.util.UUID;
import models.MarkerShape;
import models.SerializeableColor;

public class PlayerPropertiesMessageBody {
    private SerializeableColor color;
    private UUID id;
    private String name;
    private MarkerShape shape;

    public PlayerPropertiesMessageBody(SerializeableColor color, UUID id, String name, MarkerShape shape){
        this.color = color;
        this.id = id;
        this.name = name;
        this.shape = shape;
    }

    public SerializeableColor getColor(){ 
        return color;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MarkerShape getShape() {
        return shape;
    }
}
