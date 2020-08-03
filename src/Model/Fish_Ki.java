package Model;

public class Fish_Ki implements KI {
    private int length;
    private int speed;
    private int pos = 0;
    private boolean forwarded = true;
    private int jumpHeight = 400;
    private int vel = jumpHeight;
    private boolean firstJump = true;

    Fish_Ki(int length, int speed){
        this.length = length;
        this.speed = speed;
    }

    @Override
    public int nextTurnX() {
        if (forwarded){
            pos += speed;
            if (pos > length){
                forwarded = false;
                vel = jumpHeight;
            }
            return -speed;
        } else {
            pos -= speed;
            if (pos < 0){
                forwarded = true;
                vel = jumpHeight;
            }
            return speed;
        }
    }

    @Override
    public int nextTurnY() {
        int result  = -8;
        result -= vel;
        vel-= 2;
        if (firstJump){
            firstJump = false;
            jumpHeight -= 100;
            return result;
        }
        return result/25;
    }
}

