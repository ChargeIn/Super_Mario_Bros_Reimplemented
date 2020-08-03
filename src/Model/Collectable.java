package Model;

import View.SpriteHandler;
import javafx.scene.image.Image;

/**
 * Repräsentiert aufsammelbare Objekte.
 * Da aufsammelbare Objekte auch lebende sein können,
 * wird LivingEntity erweitert.
 */
public class Collectable extends LivingEntity {

    private int frameCount = -1;
    private int itemNumber; //Evtl neün Enum erstellen -> übersichtlichkeit!

    /**
     * Erstellt ein neüs Collectable
     * @param x X-Koordinate.
     * @param y Y-Koordinate
     * @param sprite Zahl des Sprites.
     * @param ki KI die auf das Collectable angewendet werden soll.
     * @param reward Zahl der Punkte beim Einsammeln.
     */
    Collectable(int x, int y, int sprite, KI ki, int reward){
        super(x, y);
        velocity = 2;
        image = SpriteHandler.initObjectSprite(sprite);
        setKI(ki);
        this.reward = reward;
        itemNumber = sprite;
    }

    /**
     * Zur Abfrage der Höhe.
     * @return gibt -999 zurück.
     */
    @Override
    public double getHeight(){ return -999; }

    /**
     * Zur Abfrage des zugehörigen Bildes.
     * @return zugehperiges Image.
     */
    @Override
    public Image getImage() {
        //Falls das Item eine Münze ist, dann tu das:
        //(Die Münzen Sprites sind 0 und 1, ab 2 ists Mushroom)
        if (itemNumber == 0) {
            frameCount++;

            if (frameCount > 20) {
                frameCount = 0;
                atImage++;
                if (atImage >= 2) {
                    atImage = 0;
                }
            }
        }
        return super.getImage();
    }
}
