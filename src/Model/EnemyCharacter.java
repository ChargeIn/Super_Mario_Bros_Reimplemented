package Model;

import View.SpriteHandler;
import javafx.scene.image.Image;

/**
 * Repräsentiert einen Gegner im Spiel.
 */
public class EnemyCharacter extends LivingEntity {

    private int frameCount = -1;
    private static final int deathImage = 2;

    EnemyCharacter(int x, int y, int sprite, KI ki){
        super(x, y);
        velocity = 2;
        image = SpriteHandler.initEnemySprite(sprite);
        setKI(ki);
    }

    /**
     * Zur Abfrage der zum Gegner zugehörigen visüllen Darstellung.
     *
     * @return Zum Gegner zugehöriges Image.
     */
    @Override
    public Image getImage() {
        frameCount++;

        if (frameCount > 20){
            frameCount = 0;
            atImage++;
            if (atImage >= deathImage){
                atImage = 0;
            }
        }
        return super.getImage();
    }

}
