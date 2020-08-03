package Model;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Repräsentiert ungefähr alles, was es in dem Spiel geben kann.
 */
//Entity makes updating less complicated and collision detection more easy because it prevents a lot of special cases which have to be implemented.
public abstract class Entity {
    /**
     * X- und Y-Koordinate der Entität.
     */
    int x, y;
    /**
     * Gibt an, ob auf die Entität Gravitation angewendet werden kann.
     */
    boolean canDrop;

    /**
     * Grösse der Visüllen Darstellung.
     */
    public static final int BLOCK_SIZE = 32;

    Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Zur Abfrage der X-Koordinate.
     * @return X-Koordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Zur Abfrage der Y-Koordinate.
     * @return Y-Koordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Zur Festlegung der X-Koordinate.
     * @param x Neü X-Koordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Zur Festlegung der X-Koordinate.
     * @param x Neü X-Koordinate.
     */
    public void set2X(int x) {
        this.x = x;
    }

    /**
     * Zur Festlegung der Y-Koordinate.
     * @param y Neü Y-Koordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Zur Festlegung der Position
     * @param x Neü X-Koordinate.
     * @param y Neü Y-Koordinate.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Zur Abfrage ob man mit dem Block kollidieren kann.
     *
     * @return true falls man nicht kollidieren kann. False sonst.
     */
    public boolean isPassable() {
        return false;
    }

    /**
     * Zur Abfrage der Hitbox der Entität.
     * @return Rechteck der Hitbox.
     */
    public Shape getHitbox() {
        return new Rectangle(x, y, BLOCK_SIZE, BLOCK_SIZE);
    }
}
