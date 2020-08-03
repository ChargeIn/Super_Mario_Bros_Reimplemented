package Model;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Fische sind einfache Gegner, die Mario bezwingen kann.
 */
public class Fish extends EnemyCharacter {

    /**
     * Einem Fish wird die Startposition und das Verhalten in Form einer KI übergeben.
     * @param x Start-X.
     * @param y Start-Y.
     * @param ki Verhalten.
     */
    Fish(int x, int y, KI ki){
        super(x, y, 1, ki);
    }

    /**
     * Gibt die zur Kolission notwendige Hitbox eines Fishes rück.
     * @return Shape der Hitbox.
     */
    @Override
    public Shape getHitbox() {
        //Union aus unterem grösseren und oberem kleineren Rechteck
        return Shape.union(new Rectangle(x, y + 10, BLOCK_SIZE, 22), new Rectangle(x + 2, y, BLOCK_SIZE - 4, 10));
    }
    @Override
    public void setX(int x) {
        oldX = this.x;
        this.x = x;
        forward = !((x - oldX) >= 0);  //Falls das neu X grösser oder gleich dem alten X ist, so bewegt er sich nach vorn
    }
}
