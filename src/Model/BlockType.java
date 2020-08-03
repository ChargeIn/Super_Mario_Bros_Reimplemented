package Model;

import View.SpriteHandler;
import javafx.scene.image.Image;

/**
 * Sammlung der implementierten Blockarten. Jeder Block besitzt sein eigenes Sprite. Namen sind selbsterklärend.
 */
public enum BlockType {
    PLATFORM(0, false, SpriteHandler.initBlockSprite(0), true),
    AIR(1, true, SpriteHandler.initBlockSprite(1), false),
    PLATFORM_NO_TOP(2, false, SpriteHandler.initBlockSprite(2), false),
    GROUND(3, false, SpriteHandler.initBlockSprite(3), false),
    BG_HILL1(4, true, SpriteHandler.initBlockSprite(4), false),
    BG_HILL2(5, true, SpriteHandler.initBlockSprite(5), false),
    BG_HILL3(6, true, SpriteHandler.initBlockSprite(6), false),
    BG_HILL4(7, true, SpriteHandler.initBlockSprite(7), false),
    BG_HILL5(8, true, SpriteHandler.initBlockSprite(8), false),
    BG_HILL6(9, true, SpriteHandler.initBlockSprite(9), false),
    BG_BUSH1(10, true, SpriteHandler.initBlockSprite(10), false),
    BG_BUSH2(11, true, SpriteHandler.initBlockSprite(11), false),
    BG_BUSH3(12, true, SpriteHandler.initBlockSprite(12), false),
    QuestionMARK_BLOCK_COIN(13, false, SpriteHandler.initBlockSprite(13), true),
    PIPE1(14, false, SpriteHandler.initBlockSprite(14), false),
    PIPE2(15, false, SpriteHandler.initBlockSprite(15), false),
    PIPE3(16, false, SpriteHandler.initBlockSprite(16), false),
    PIPE4(17, false, SpriteHandler.initBlockSprite(17), false),
    BG_CLOUD1(18, true, SpriteHandler.initBlockSprite(18), false),
    BG_CLOUD2(19, true, SpriteHandler.initBlockSprite(19), false),
    BG_CLOUD3(20, true, SpriteHandler.initBlockSprite(20), false),
    BG_CLOUD4(21, true, SpriteHandler.initBlockSprite(21), false),
    BG_CLOUD5(22, true, SpriteHandler.initBlockSprite(22), false),
    BG_CLOUD6(23, true, SpriteHandler.initBlockSprite(23), false),
    STAIR_BLOCK(24, false, SpriteHandler.initBlockSprite(24), false),
    CASTLE_BLOCK1(25, true, SpriteHandler.initBlockSprite(25), false),
    CASTLE_BLOCK2(26, true, SpriteHandler.initBlockSprite(26), false),
    CASTLE_BLOCK3(27, true, SpriteHandler.initBlockSprite(27), false),
    CASTLE_BLOCK4(28, true, SpriteHandler.initBlockSprite(28), false),
    CASTLE_BLOCK5(29, true, SpriteHandler.initBlockSprite(29), false),
    CASTLE_BLOCK6(30, true, SpriteHandler.initBlockSprite(30), false),
    BG_FLAGTOP(31, true, SpriteHandler.initBlockSprite(31), false),
    BG_FLAGPOLE(32, true, SpriteHandler.initBlockSprite(32), false),
    CASTLE_BLOCK7(33, true, SpriteHandler.initBlockSprite(33), false),
    USED_QuestionMARK_BLOCK(34, false, SpriteHandler.initBlockSprite(34), false),
    QuestionMARK_BLOCK_MUSHROOM(35, false, SpriteHandler.initBlockSprite(13), true),
    BG_GROUND(36, false, SpriteHandler.initBlockSprite(3), false),
    BRIDGE(37,false,SpriteHandler.initBlockSprite(35),false),
    WATER(38,true,SpriteHandler.initBlockSprite(36),true),
    WAVE(39,true,SpriteHandler.initBlockSprite(37),true);

    private int id;
    private final boolean passable;
    private final Image image;
    private boolean interactable;

    BlockType(int id, boolean passable, Image image, boolean interactable) {
        this.id = id;
        this.passable = passable;
        this.image = image;
        this.interactable = interactable;
    }


    /**
     * Zur Abfrage ob man mit dem Block kollidieren kann.
     *
     * @return true falls man nicht kollidieren kann. False sonst.
     */
    public boolean isPassable() {
        return passable;
    }

    /**
     * Zur Abfrage ob man mit dem Block interagieren kann.
     *
     * @return true falls interaktiver Block. False sonst.
     */
    public boolean isInteractable(){
        return interactable;
    }

    /**
     * Zur Abfrage der zum Block zugehörigen visüllen Darstellung.
     *
     * @return Zum Block zugehöriges Image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Generiert einen Blocktyp anhand der übergebenen id
     * @param id Blocktypid
     * @return Blocktype.
     */
    public static BlockType createFromID(int id){
        for (BlockType blockType : BlockType.values())
            if (id == blockType.id)
                return blockType;
        throw new IllegalStateException("Unknown BlockType!");
    }
}
