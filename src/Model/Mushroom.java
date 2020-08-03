package Model;

public class Mushroom extends Collectable {

    public Mushroom(int x, int y) {
        super(x, y, 1, new Patrouille(1000, 2), 1000);
    }
}
