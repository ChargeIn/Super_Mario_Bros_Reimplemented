package View;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public abstract class SpriteHandler {

    //This methode returns a Image depending on the given integer -> it is to initialize sprites / Images for game objects
    public static Image initBlockSprite(int blockType) {
        Image output = null;
        try {
            BufferedImage image = ImageIO.read(SpriteHandler.class.getResource("/WorldBlocks_TileSet.png"));
            switch (blockType) {
                case 0: //Oberfläche
                    output = transformSubimage(image.getSubimage(16, 0, 16, 16));
                    break;
                case 1: //Luft
                    output = transformSubimage(image.getSubimage(16 * 30, 0, 16, 16));
                    break;
                case 2: //Erde
                    output = transformSubimage(image.getSubimage(32, 0, 16, 16));
                    break;
                case 3: //brüchiger Stein
                    output = transformSubimage(image.getSubimage(0, 0, 16, 16));
                    break;
                case 4: //Hintergrund Berg
                    output = transformSubimage(image.getSubimage(16 * 8, 16 * 8, 16, 16));
                    break;
                case 5: //Hintergrund Berg
                    output = transformSubimage(image.getSubimage(16 * 9, 16 * 8, 16, 16));
                    break;
                case 6: //Hintergrund Berg
                    output = transformSubimage(image.getSubimage(16 * 10, 16 * 8, 16, 16));
                    break;
                case 7: //Hintergrund Berg
                    output = transformSubimage(image.getSubimage(16 * 8, 16 * 9, 16, 16));
                    break;
                case 8: //Hintergrund Berg
                    output = transformSubimage(image.getSubimage(16 * 9, 16 * 9, 16, 16));
                    break;
                case 9: //Hintergrund Berg
                    output = transformSubimage(image.getSubimage(16 * 10, 16 * 9, 16, 16));
                    break;
                case 10: //Hintergrund Busch
                    output = transformSubimage(image.getSubimage(16 * 11, 16 * 9, 16, 16));
                    break;
                case 11: //Hintergrund Busch
                    output = transformSubimage(image.getSubimage(16 * 12, 16 * 9, 16, 16));
                    break;
                case 12: //Hintergrund Busch
                    output = transformSubimage(image.getSubimage(16 * 13, 16 * 9, 16, 16));
                    break;
                case 13: //Fragezeichen Block
                    output = transformSubimage(image.getSubimage(16 * 24, 0, 16, 16));
                    break;
                case 14: //Rohr
                    output = transformSubimage(image.getSubimage(0, 16 * 8, 16, 16));
                    break;
                case 15: //Rohr
                    output = transformSubimage(image.getSubimage(16, 16 * 8, 16, 16));
                    break;
                case 16: //Rohr
                    output = transformSubimage(image.getSubimage(0, 16 * 9, 16, 16));
                    break;
                case 17: //Rohr
                    output = transformSubimage(image.getSubimage(16 , 16 * 9, 16, 16));
                    break;
                case 18: //Hintergrund Wolke
                    output = transformSubimage(image.getSubimage(0, 16 * 20, 16, 16));
                    break;
                case 19: //Hintergrund Wolke
                    output = transformSubimage(image.getSubimage(16, 16 * 20, 16, 16));
                    break;
                case 20: //Hintergrund Wolke
                    output = transformSubimage(image.getSubimage(16 * 2, 16 * 20, 16, 16));
                    break;
                case 21: //Hintergrund Wolke
                    output = transformSubimage(image.getSubimage(0, 16 * 21, 16, 16));
                    break;
                case 22: //Hintergrund Wolke
                    output = transformSubimage(image.getSubimage(16, 16 * 21, 16, 16));
                    break;
                case 23: //Hintergrund Wolke
                    output = transformSubimage(image.getSubimage(16 * 2, 16 * 21, 16, 16));
                    break;
                case 24: //Treppe
                    output = transformSubimage(image.getSubimage(0, 16, 16, 16));
                    break;
                case 25: //Schloss
                    output = transformSubimage(image.getSubimage(16 * 11, 0, 16, 16));
                    break;
                case 26: //Schloss
                    output = transformSubimage(image.getSubimage(16 * 12, 0, 16, 16));
                    break;
                case 27: //Schloss
                    output = transformSubimage(image.getSubimage(16 * 13, 0, 16, 16));
                    break;
                case 28: //Schloss
                    output = transformSubimage(image.getSubimage(16 * 11, 16, 16, 16));
                    break;
                case 29: //Schloss
                    output = transformSubimage(image.getSubimage(16 * 12, 16, 16, 16));
                    break;
                case 30: //Schloss
                    output = transformSubimage(image.getSubimage(16 * 13, 16, 16, 16));
                    break;
                case 31: //Flagge
                    output = transformSubimage(image.getSubimage(16 * 16, 16 * 8, 16, 16));
                    break;
                case 32: //Flagge
                    output = transformSubimage(image.getSubimage(16 * 16, 16 * 9, 16, 16));
                    break;
                case 33: //Schloss Block
                    output = transformSubimage(image.getSubimage(16 * 14, 0, 16, 16));
                    break;
                case 34: //Benutzter Fragezeichen Block
                    output = transformSubimage(image.getSubimage(16 * 27, 0, 16, 16));
                    break;
                case 35:
                    output = transformSubimage(image.getSubimage(16*3, 16, 16, 16));
                    break;
                case 36:
                    output = transformSubimage(image.getSubimage(16*3, 16*27, 16, 16));
                    break;
                case 37:
                    output = transformSubimage(image.getSubimage(16*3, 16*26, 16, 16));
                    break;
                default:
                    output = transformSubimage(image.getSubimage(16 * 24, 0, 16, 16));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static List<Image> initCharacterSprite(int characterNumber) {
        Image output;
        List<Image> outputList = new LinkedList<>();
        try {
            BufferedImage image = ImageIO.read(SpriteHandler.class.getResource("/MarioPlayer_TileSet.png"));
            switch (characterNumber) {
                case 0:
                    //small Mario
                    //Images for running
                    output = transformSubimage(image.getSubimage(16 * 5,(16 * 2) + 2, 16, 16));
                    outputList.add(output);

                    output = transformSubimage(image.getSubimage((16 * 6) + 1,(16 * 2) + 2, 16, 16));
                    outputList.add(output);

                    output = transformSubimage(image.getSubimage((16 * 7) + 2,(16 * 2) + 2, 16, 16));
                    outputList.add(output);

                    output = transformSubimage(image.getSubimage((16 * 8) + 3,(16 * 2) + 2, 16, 16));
                    outputList.add(output);

                    //Image for dying
                    output = transformSubimage(image.getSubimage((16 * 11) + 6, (16 *  2) + 2, 16, 16));
                    outputList.add(output);

                    //Image for finishing
                    output = transformSubimage(image.getSubimage((16 * 13) + 8, (16 *  2) + 2, 16, 16));
                    outputList.add(output);

                    //Image for jumping
                    output = transformSubimage(image.getSubimage((16 * 10) + 5, (16 *  2) + 2, 16, 16));
                    outputList.add(output);

                    //tall Mario
                    //Images for running
                    output = transformSubimage(image.getSubimage(16 * 5,1, 16, 32));
                    outputList.add(output);

                    output = transformSubimage(image.getSubimage((16 * 6) + 1,1, 16, 32));
                    outputList.add(output);

                    output = transformSubimage(image.getSubimage((16 * 7) + 2,1, 16, 32));
                    outputList.add(output);

                    output = transformSubimage(image.getSubimage((16 * 8) + 3,1, 16, 32));
                    outputList.add(output);

                    //Image for dying
                    output = transformSubimage(image.getSubimage((16 * 11) + 6,  1, 16, 32));
                    outputList.add(output);

                    //Image for finishing
                    output = transformSubimage(image.getSubimage((16 * 13) + 8, 1, 16, 32));
                    outputList.add(output);

                    //Image for jumping
                    output = transformSubimage(image.getSubimage((16 * 10) + 5, 1, 16, 32));
                    outputList.add(output);

                    //Luigi
                    //small Lugig
                    //Images for running
                    output = transformSubimage(image.getSubimage((16 * 5),(16 * 6) + 3, 16, 16));
                    outputList.add(output);

                    output = transformSubimage(image.getSubimage((16 * 6) + 1,(16 * 6) + 3, 16, 16));
                    outputList.add(output);

                    output = transformSubimage(image.getSubimage((16 * 7) + 2,(16 * 6) + 3, 16, 16));
                    outputList.add(output);

                    output = transformSubimage(image.getSubimage((16 * 8) + 3,(16 * 6) + 3, 16, 16));
                    outputList.add(output);

                    //Image for dying
                    output = transformSubimage(image.getSubimage((16 * 11) + 6, (16 *  6) + 3, 16, 16));
                    outputList.add(output);

                    //Image for finishing
                    output = transformSubimage(image.getSubimage((16 * 13) + 8, (16 *  6) + 3, 16, 16));
                    outputList.add(output);

                    //Image for jumping
                    output = transformSubimage(image.getSubimage((16 * 10) + 5, (16 *  6) + 3, 16, 16));
                    outputList.add(output);

                    //tall Lugig
                    //Images for running
                    output = transformSubimage(image.getSubimage((16 * 5),(16 * 4) + 2, 16, 32));
                    outputList.add(output);

                    output = transformSubimage(image.getSubimage((16 * 6) + 1,(16 * 4) + 2, 16, 32));
                    outputList.add(output);

                    output = transformSubimage(image.getSubimage((16 * 7) + 2,(16 * 4) + 2, 16, 32));
                    outputList.add(output);

                    output = transformSubimage(image.getSubimage((16 * 8) + 3,(16 * 4) + 2, 16, 32));
                    outputList.add(output);

                    //Image for dying
                    output = transformSubimage(image.getSubimage((16 * 11) + 6,  (16 * 4) + 2, 16, 32));
                    outputList.add(output);

                    //Image for finishing
                    output = transformSubimage(image.getSubimage((16 * 13) + 8, (16 * 4) + 2, 16, 32));
                    outputList.add(output);

                    //Image for jumping
                    output = transformSubimage(image.getSubimage((16 * 10) + 5, (16 * 4) + 2, 16, 32));
                    outputList.add(output);
                    break;
                default:
                    output = transformSubimage(image.getSubimage(16 * 16, 0, 16, 16));
                    outputList.add(output);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputList;
    }

    public static List<Image> initEnemySprite(int characterNumber) {
        Image output;
        List<Image> outputList = new LinkedList<>();
        try {
            BufferedImage image = ImageIO.read(SpriteHandler.class.getResource("/Enemies_TileSet.png"));
            switch (characterNumber) {
                case 0:    //Goomba
                    //Laufsprite 1
                    output = transformSubimage(image.getSubimage(0, 16, 16, 16));
                    outputList.add(output);

                    //Laufsprite 2
                    output = transformSubimage(image.getSubimage(16, 16, 16, 16));
                    outputList.add(output);

                    //Sterbender Sprite
                    output = transformSubimage(image.getSubimage(16 * 2, 16, 16, 16));
                    outputList.add(output);
                    break;

                case 1:
                    //Laufsprite 1
                    output = transformSubimage(image.getSubimage(16 * 39, 16, 16, 16));
                    outputList.add(output);

                    //Laufsprite 2
                    output = transformSubimage(image.getSubimage(16 * 40, 16, 16, 16));
                    outputList.add(output);

                    //Sterbender Sprite
                    output = transformSubimage(image.getSubimage(16 * 40, 16, 16, 16));
                    outputList.add(output);
                    break;
                default:    //Goomba (for now)
                    output = transformSubimage(image.getSubimage(0, 16, 16, 16));
                    outputList.add(output);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputList;
    }

    public static List<Image> initObjectSprite(int characterNumber) {
        Image output;
        List<Image> outputList = new LinkedList<>();
        try {
            BufferedImage image = ImageIO.read(SpriteHandler.class.getResource("/Object_Tiles.png"));
            switch (characterNumber) {
                case 0: //Coin
                    output = transformSubimage(image.getSubimage(0, 16 * 6, 16, 16));
                    outputList.add(output);
                    output = transformSubimage(image.getSubimage(16, 16 * 6, 16, 16));
                    outputList.add(output);
                    break;
                case 1: //Mushroom
                    output = transformSubimage(image.getSubimage(0, 0, 16, 16));
                    outputList.add(output);
                    break;
                case 4: //zerstörter Block Links oben
                    output = transformSubimage(image.getSubimage(16 * 4, 0, 8, 8));
                    outputList.add(output);
                    break;
                case 5: //zerstörter Block Links unten
                    output = transformSubimage(image.getSubimage(16 * 4, 8, 8, 8));
                    outputList.add(output);
                    break;
                case 6: //zerstörter Block Rechts oben
                    output = transformSubimage(image.getSubimage(16 * 4 + 8, 0, 8, 8));
                    outputList.add(output);
                    break;
                case 7: //zerstörter Block Rechts unten
                    output = transformSubimage(image.getSubimage(16 * 4 + 8, 8, 8, 8));
                    outputList.add(output);
                    break;
                default:
                    output = transformSubimage(image.getSubimage(0, 16, 16, 16));
                    outputList.add(output);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputList;
    }

    public static List<Image> initUISprite(int type) {
        Image output;
        List<Image> outputList = new LinkedList<>();
        try {
            BufferedImage image = ImageIO.read(SpriteHandler.class.getResource("/Object_Tiles.png"));
            switch (type) {
                case 0: //Coin symbol
                    output = transformSubimage(image.getSubimage(0, 16 * 10 , 8, 8));
                    outputList.add(output);
                    output = transformSubimage(image.getSubimage(8, 16 * 10 , 8, 8));
                    outputList.add(output);
                default:
                    output = transformSubimage(image.getSubimage(0, 16, 16, 16));
                    outputList.add(output);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputList;
    }

    public static Image initMenuSprite(int type){
        Image output = null;
        try {
            BufferedImage image = ImageIO.read(SpriteHandler.class.getResource("/Object_Tiles.png"));
            switch(type){
                case 0: //Startmenu
                    output = new Image("file:resources/TitleScreen.png");
                    break;
                case 1: //Starmenu Mushroom
                    output = transformSubimage(image.getSubimage(23,160,9,9));
                    break;
                case 2: //MenuBG
                    output = new Image("file:resources/MenuBG.png");
                    break;
                default:
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return output;
    }

    //This methode just scales the image to double the size
    private static Image transformSubimage(BufferedImage toTransform) {
        int w = toTransform.getWidth() * 2;
        int h = toTransform.getHeight() * 2;
        BufferedImage preOutput = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(2.0, 2.0);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return SwingFXUtils.toFXImage(scaleOp.filter(toTransform, preOutput), null);
    }
}
