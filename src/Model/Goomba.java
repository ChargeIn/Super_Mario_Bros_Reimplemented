package Model;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Goombas sind einfache Gegner, die Mario bezwingen kann.
 */
class Goomba extends EnemyCharacter {

    /**
     * Einem Goomba wird die Startposition und das Verhalten in Form einer KI übergeben.
     * @param x Start-X.
     * @param y Start-Y.
     * @param ki Verhalten.
     */
    Goomba(int x, int y, KI ki){
        super(x, y, 0, ki);
    }

    /**
     * Gibt die zur Kolission notwendige Hitbox eines Goombas rück.
     * @return Shape der Hitbox.
     */
    @Override
    public Shape getHitbox() {
        //Union aus unterem grösseren und oberem kleineren Rechteck
        return Shape.union(new Rectangle(x, y + 10, BLOCK_SIZE, 22), new Rectangle(x + 2, y, BLOCK_SIZE - 4, 10));
    }
}
