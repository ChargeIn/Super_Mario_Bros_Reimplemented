package Model;

import javafx.scene.image.Image;

import java.util.LinkedList;
import java.util.List;

public abstract class LivingEntity extends Entity {

    boolean isFalling = true;

    //We need oldX and oldY to be able to reset the character in case of colliding
    int velocity, jumpingVelocity;
    int oldX, oldY;
    private int jumpingHeight = 0;
    List<Image> image = new LinkedList<>();
    boolean forward = true;
    int reward = 100;
    int atImage = 0;
    private KI ki;

    LivingEntity(int x, int y) {
        super(x, y);
        oldX = x;
        oldY = y;
        velocity = 0;
        canDrop = true;
    }

    public int getVelocity() {
        return velocity;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public void setFalling(boolean falling) {
        this.isFalling = falling;
    }

    public void setJumpingHeight(int jumpingHeight) {
        this.jumpingHeight = jumpingHeight;
    }

    public int getJumpingHeight() {
        return jumpingHeight;
    }

    @Override
    public void setX(int x) {
        oldX = this.x;
        this.x = x;
        forward = (x - oldX) >= 0;  //Falls das neu X grösser oder gleich dem alten X ist, so bewegt er sich nach vorn
    }

    @Override
    public void setY(int y) {
        oldY = this.y;
        this.y = y;

        //If Player hit highest jumpingpoint, set him falling so he cannot get up until having hit the ground once.
        if (this.y == jumpingHeight) {
            setFalling(true);
        }
    }

    @Override
    public void setPosition(int x, int y) {
        setX(x);
        setY(y);

        //Nach einem Position Reset schaut er immer nach vorne!
        forward = true;
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public Image getImage(){ return image.get(atImage); }

    public void moveRight(){
        setX(getX() + velocity);
    }

    public void moveLeft(){
        setX(getX() - velocity);
    }

    public void resetLastVerticalMove(){
        resetX(getOldX());
    }

    /*
    public void setImage(Image image){this.image.add(image);}
    */

    public boolean runsForward(){return forward;}

    private void resetX(int x){this.x = x;}

    public int getReward(){ return reward; }

    public double getHeight(){ return image.get(atImage).getHeight(); }

    /*
    public double getWidth(){ return image.get(atImage).getWidth(); }
    */

    void setKI(KI ki){
        this.ki = ki;
    }

    public void nextTurn(){
        setX(this.x + ki.nextTurnX());
        setY(this.y + ki.nextTurnY());
    }
}
