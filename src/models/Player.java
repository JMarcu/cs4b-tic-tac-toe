package models;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.SubmissionPublisher;
import java.util.UUID;
import javafx.scene.paint.Color;

/** 
 * Models a player in the game.
 * @author James Marcu
 */
public class Player {
    /************************************************************************************************************
     * NESTED OBJECTS
     ************************************************************************************************************/

    /** Describes a differential and atomic update to the player's state. Objects of this type are dispatched to subscribers */
    public class Patch {
        /** The color of the player's marker. */
        protected Color color;

        /** The player's name. */
        protected String name;

        /** The shape of the player's marker. */
        protected MarkerShape shape;
        
        /**
         * Returns the color of the player's marker.
         * @return The color of the player's marker, or null if the color has not changed since the last patch.
         */
        public Color getColor() { return color; }

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

    /************************************************************************************************************
     * CLASS VARIABLES
     ************************************************************************************************************/

    /** Whether or not the player is an AI. */
    protected boolean isAi;

    /** The color of the player's marker. */
    private Color color;

    /** A uuid for identifying the player. */
    private UUID id;

    /** The player's name. */
    private String name;

    /** A {@link java.util.concurrent.Flow.Publisher} implementation that handles our subscriptions. */
    private SubmissionPublisher<Player.Patch> publisher;

    /** The shape of the player's marker. */
    private MarkerShape shape;

    /** Constructs a default player object. */
    public Player() {
        this(Color.BLACK, UUID.randomUUID(), "Player", MarkerShape.X);
    }

    /** Constructs a new player object. */
    public Player(Color color, UUID id, String name, MarkerShape shape) {
        this.color = color == null ? Color.BLACK : color;
        this.id = id;
        this.isAi = false;
        this.name = name;
        this.publisher = new SubmissionPublisher<Player.Patch>(Runnable::run, Flow.defaultBufferSize());
        this.shape = shape == null ? MarkerShape.X : shape;
    }

    /*==========================================================================================================
     * ACCESSORS & MUTATORS
     *==========================================================================================================*/

    /**
     * Returns the color of the player's marker.
     * @return The color of the player's marker.
     */
    public Color getColor() {return this.color;}

    /**
     * Returns player's UUID.
     * @return The player's UUID.
     */
    public UUID getUuid() {return this.id;}
    
    /**
     * Returns player's name.
     * @return The player's name.
     */
    public String getName() {return this.name;}
    
    /**
     * Returns the shape of the player's marker.
     * @return The shape of the player's marker.
     */
    public MarkerShape getShape() {return this.shape;}
    
    /**
     * Returns whether the player is an AI.
     * @return True if the player is an AI, false if they are not.
     */
    public boolean getIsAI() {return isAi;}

    /**
     * Sets the color of the player's marker. Subscribers are notified.
     * @param color The new color of the player's marker.
     */
    public void setColor(Color color){
        this.color = color;
        this.notifySubscribers(new Patch(){
            {
                color = Player.this.color;
            }
        });
    }

    /**
     * Sets the player's name. Subscribers are notified.
     * @param name The player's new name.
     */
    public void setName(String name){
        this.name = name;
        this.notifySubscribers(new Patch(){
            {
                name = Player.this.name;
            }
        });
    }

    /**
     * Sets the shape of the player's marker. Subscribers are notified.
     * @param shape The new shape of the player's marker.
     */
    public void setShape(MarkerShape shape){
        this.shape = shape;
        this.notifySubscribers(new Patch(){
            {
                shape = Player.this.shape;
            }
        });
    }

    /*==========================================================================================================
     * PUBLISHER INTERFACE - the publisher interface is satisfied by wrapping a 
     * {@link java.util.concurrent.SubmissionPublisher} behind the interface, not through direct implementation.
     * This publisher is functionally synchronous since all tasks are run on the current thread.
     *==========================================================================================================*/


    /** Subscribe to receive updates whenever the game's state updates. */
    public void subscribe(Subscriber<? super Player.Patch> subscriber) {
        this.publisher.subscribe(subscriber);
    }

    /**  Offers the patch to all subscribers. */
    private <U> void notifySubscribers(Patch patch){
        this.publisher.offer(patch, null);
    }

    /*==========================================================================================================
     * COMPARISON INTERFACES
     *==========================================================================================================*/
    
    /**
     * Player equality is defined by UUIDs. Player objects are considered equal if they share a UUID. This does not
     * guarantee that the rest of their fields are the same. Two instances of the same player might have discrepencies
     * due to dirty reads or concurrency issues, but nonetheless be 'equal'.
     * @param otherPlayer The player to compare equality to.
     * @return Returns true if the two objects represent the same player (i.e. share a UUID), returns false otherwise.
     */
    public boolean equals(Player otherPlayer){
        return id == otherPlayer.id;
    }
}
