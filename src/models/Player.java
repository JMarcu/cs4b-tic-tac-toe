package models;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

import javax.lang.model.type.NullType;

import javafx.scene.paint.Color;

public class Player implements Publisher<NullType> {
    private Color color;
    private UUID id;
    private String name;
    private MarkerShape shape;
    private ArrayList<Subscriber<? super NullType>> subscribers;

    public Player(Color color, UUID id, String name, MarkerShape shape) {
        this.color = color;
        this.id = id;
        this.name = name;
        this.shape = shape;
        this.subscribers = new ArrayList<Subscriber<? super NullType>>();
    }

    public Color getColor() {return this.color;}
    public UUID getUuid() {return this.id;}
    public String getName() {return this.name;}
    public MarkerShape getShape() {return this.shape;}
    public boolean getIsAI() {return false;}

    public void setColor(Color color){
        this.color = color;
        this.notifySubscribers();
    }

    public void setName(String name){
        this.name = name;
        this.notifySubscribers();
    }

    public void setShape(MarkerShape shape){
        this.shape = shape;
        this.notifySubscribers();
    }

    @Override
    public void subscribe(Subscriber<? super NullType> subscriber) {
        this.subscribers.add(subscriber);
    }

    private void notifySubscribers(){
        final ArrayList<Subscriber<? super NullType>> toRemove = new ArrayList<>();
        this.subscribers.forEach((Subscriber<? super NullType> subscriber) -> {
            if(subscriber != null){
                subscriber.onNext(null);;
            } else{
                toRemove.add(subscriber);
            }
        });
        toRemove.forEach((Subscriber<? super NullType> subscriber) -> {
            this.subscribers.remove(subscriber);
        });
    }
}
