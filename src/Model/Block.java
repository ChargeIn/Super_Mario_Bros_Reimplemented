package Model;

import javafx.scene.image.Image;

/**
 * Repräsentiert ein starres Levelelement.
 */
public class Block extends Entity implements Comparable<Block> {

    /**
     * Typ des Blocks.
     */
    private BlockType type;

    /**
     * Konstruktor des Blocks.
     *
     * @param x Vertikale Position der linken oberen Ecke des Blocks.
     * @param y Horizontale Position der linken oberen Ecke des Blocks.
     * @param type Typ des Blocks.
     */
    Block(int x, int y, BlockType type) {
        super(x, y);
        this.type = type;
    }

    /**
     * Zur Abfrage des Blocktyps.
     *
     * @return Typ des Blocks.
     */
    public BlockType getType() {
        return type;
    }

    /**
     * Zum setzen des Blocktyps.
     *
     * @param type Neür Typ des Blocks.
     */
    public void setType(BlockType type) {
        this.type = type;
    }

    /**
     * Zur Abfrage ob man mit dem Block kollidieren kann.
     *
     * @return true falls man nicht kollidieren kann. False sonst.
     */
    @Override
    public boolean isPassable() {
        return type.isPassable();
    }

    /**
     * Zur Abfrage ob man mit dem Block interagieren kann.
     *
     * @return true falls interaktiver Block. False sonst.
     */
    public boolean isInteractable(){
        return type.isInteractable();
    }

    /**
     * Zur Abfrage der zum Block zugehörigen visüllen Darstellung.
     *
     * @return Zum Block zugehöriges Image.
     */
    public Image getImage() {
        return type.getImage();
    }

    /**
     * Vergleicht zwei Blöcke miteinander.
     *
     * @param o Anderer Block
     * @return -1 falls o vertikal vor Block, 0 falls gleiche x-Position, 1 falls o vertikal hinter Block.
     */
    @Override
    public int compareTo(Block o) {
        return Integer.compare(this.x, o.getX());
    }
}
