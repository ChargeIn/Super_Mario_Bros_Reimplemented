package Model;

/**
 * Erweitert Collectable und repräsentiert einsammelbare Münzen.
 */
public class Coin extends Collectable {

    /**
     * Erstellt eine neü Münze.
     * @param x X-Koordinate der Münze.
     * @param y Y-Koordinate der Münze.
     */
    public Coin(int x, int y) {
        super(x, y,0, new Patrouille(0, 0), 100);
    }
}
