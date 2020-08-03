package Model;

import View.SpriteHandler;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class PlayerCharacter extends LivingEntity {

    private boolean isMoving = false;
    private boolean isTall = false;
    private int count = 1;
    private int step = 0;
    private static final int deathImage = 4;
    private static final int finishImage = 5;
    private static final int jumpImage = 6;
    private static final int standingSmall = 0;
    private static final int standingTall = 7;
    private static final int tallImagesStartingIndex = 7;
    private static final int luigi = 14;
    private final int jumpHeight = 33;
    private boolean isLuigi = false;


    PlayerCharacter(int x, int y) {
        super(x, y);
        velocity = 4;
        jumpingVelocity = jumpHeight;
        setJumpingHeight(0);
        image = SpriteHandler.initCharacterSprite(0);
    }

    public int getJumpMovement() {

        if (getJumpingHeight() == 0) {
            return 0;
        }

        setJumpingHeight(getJumpingHeight() + 1);
        if (getJumpingHeight() > 15) {
            jumpingVelocity = jumpHeight;
            setJumpingHeight(0);
            return 0;
        }
        jumpingVelocity -= 2;

        return jumpingVelocity;
    }

    public void resetImage() {
        if (isFalling) {
            atImage = 0;
            isMoving = false;
        }
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving() {
        isMoving = true;
    }

    public void startJump() {
        jumpingVelocity = jumpHeight;
        setJumpingHeight(1);
    }

    public void startSmallJump() {
        jumpingVelocity = jumpHeight / 2 + 8;
        setJumpingHeight(1);
    }

    public void setTall(boolean isTall) {
        this.isTall = isTall;
    }

    /**
     * Wählt situativ das richtige Sprite aus, mit dem der Charakter gezeichnet wird.
     */
    public void animate() {
        if (step > 4) {
                /*
                Erklärung zu unten drunter:
                Die Bilder an der image Position 1, 2, 3 werden zum laufen / der Lauf-Animation genutzt
                Das atImage wird in Abhängigkeit des counts gesetzt
                Ob der count anschliessen erhöht oder verringert wird hängt davon ab welcher Schritt vorher
                (gespeichert in temp) stattfand
                Vorher 1 jetzt 2 -> weiter hoch zählen zu 3
                Vorher 3 jetzt 2 -> weiter runter zählen zu 1
                Von 1 immer zu 2 zählen
                Von 3 immer zu 2 zählen
                Dies liefert uns die Reihenfolge: 1 - 2 - 3 - 2 - 1 - 2 - 3 - 2 - 1 - 2 - 3 - 2 ...
                Im Fall 0 immer erhöhen
                Am Ende wird der Step (Anzahl der Frames die dieses Bild schon angezeigt wurde) auf 0 gesetzt
                 */
            int temp = atImage;
            if (count == 1) atImage = 1;
            else if (count == 2) atImage = 2;
            else if (count == 3) atImage = 3;
            else atImage = 0;
            //Count gibt an was die Animation / das Image im nächsten Schritt sein muss
            if (temp == 1 && atImage == 2)
                count++;
            else if (temp == 3 && atImage == 2)
                count--;
            else if (atImage == 1)
                count++;
            else if (atImage == 3)
                count--;
            else    //Fall atImage == 0
                count++;
            step = 0;
        } else {
            step++;
        }
    }

    public Image getDeathImage() {
        int l = 0;
        if (isLuigi) {
            l+= luigi;
        }
        if (isTall) return image.get(tallImagesStartingIndex + deathImage +l);
        return image.get(deathImage + l);
    }

    public Image getFinishImage() {
        int l = 0;
        if (isLuigi) {
            l+= luigi;
        }
        if (isTall) return image.get(finishImage + tallImagesStartingIndex +l);
        return image.get(finishImage+l);
    }

    public Image getJumpImage() {
        int l = 0;
        if (isLuigi) {
            l+= luigi;
        }
        if (isTall) return image.get(jumpImage + tallImagesStartingIndex +l);
        return image.get(jumpImage +l);
    }

    public Image getSmallStandingImage() {
        int l = 0;
        if (isLuigi) {
            l+= luigi;
        }
        return image.get(standingSmall + l);
    }

    public Image getTallStandingImage() {
        int l = 0;
        if (isLuigi) {
            l+= luigi;
        }
        return image.get(standingTall + l);
    }

    public boolean isTall(){return isTall;}

    @Override
    public Image getImage() {
        int l = 0;
        if (isLuigi) {
            l+= luigi;
        }
        if (isTall) return image.get(atImage + tallImagesStartingIndex +l);
        return image.get(atImage + l);
    }

    public void setX(int x) {
        super.setX(x);
        animate();
    }

    public void setFalling(boolean falling){
        this.isFalling = falling;
    }

    public Shape getHitbox() {
        if (isTall)  return new Rectangle(x + 2, y-32, Block.BLOCK_SIZE - 4, 2*Block.BLOCK_SIZE);
        return new Rectangle(x + 2, y, Block.BLOCK_SIZE - 4, Block.BLOCK_SIZE);
    }

    public boolean isLuigi() { return isLuigi;}

    public void changeCharacter(){isLuigi = !isLuigi;}
}
