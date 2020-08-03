package Model;

public class Patrouille implements KI {

    private int length;
    private int speed;
    private int pos = 0;
    private boolean forward = true;

    Patrouille(int length, int speed){
        this.length = length;
        this.speed = speed;
    }

    @Override
    public int nextTurnX() {
        if (forward){
            pos += speed;
            if (pos > length){
                forward = false;
            }
            return -speed;
        } else {
            pos -= speed;
            if (pos < 0){
                forward = true;
            }
            return speed;
        }
    }

    @Override
    public int nextTurnY() {
        return 0;
    }
}
