package Model;


import javafx.scene.image.Image;

/**
 * Repräsentiert eine Tote Entität.
 */

public class DeadEntity extends Entity{

    /**
     * Bild der toten Entität.
     */
    private Image image;

    /**
     * Zeit nach der die visülle Darstellung entfernt wird.
     */
    private int timeToLive; //frames visible before removed
    private int animationX = 0;
    private int animationY = 0;
    private DeadEntityType type;

    /**
     * Erstellt eine neü tote Entität
     * @param image Bild das für diese DeadEntity genutzt wird
     * @param x x Koordinate für das erste erscheinen der DeadEntity
     * @param y Y Koordinate für das erste erscheinen der DeadEntity
     * @param timeToLive Anzahl der Frames die diese DeadEntity angezeigt werden soll
     * @param type Gibt an, um welche Art DeadEntity es sich handelt. (Goomba oder Block)
     */
    DeadEntity(Image image, int x, int y, int timeToLive, DeadEntityType type){
        super(x,y);
        //roate image upside down
        this.image = image;
        this.timeToLive = timeToLive;
        this.type = type;
    }

    /**
     * Zur Abfrage der zum Block zugehörigen visüllen Darstellung.
     *
     * @return Zum Block zugehöriges Image.
     */
    public Image getImage() {
        switch (type){
            case GOOMBA:
                //the animation can be described by the function f(x) = -0.25*(x-15)^2 -15
                animationY = (int)(Math.pow(-0.25*(animationX - 15),2) - 15);
                animationX++;
                timeToLive--;
                break;
            case BLOCK_LEFT:
                animationY = (int)(Math.pow(animationX, 2) * 0.5);
                animationX--;
                timeToLive--;
                break;
            case BLOCK_RIGHT:
                animationY = (int)(Math.pow(animationX, 2) * 0.5);
                animationX++;
                timeToLive--;
                break;
        }
        return image;
    }

    /**
     * Gibt zurück, ob die Entität entfernt werden kann.
     * @return true, falls die Zeit abgelaufen ist. False sonst.
     */
    public boolean timeToRemoveDeadEntity(){return timeToLive <= 0;}

    /**
     * Zur Abfrage der Breite.
     * @return Breite.
     */
    public double getWidth(){return image.getWidth();}

    /**
     * Zur Abfrage der Höhe.
     * @return Höhe.
     */
    public double getHeight(){return image.getHeight();}

    /**
     * Zur Abfrage der letzten X-Position.
     * @return Alte X-Position.
     */
    public int getOldX(){return x;}


    /**
     * Zur Abfrage der aktüllen X-Position.
     * @return Aktülle X-Position.
     */
    @Override
    public int getX() { return x + animationX;}

    /**
     * Zur Abfrage der Aktülle Y-Position.
     * @return Aktülle Y-Position
     */
    public int getY() { return y + animationY;}

    /**
     * Zur Abfrage des Typs der Entität.
     * @return Typ der Entität.
     */
    public DeadEntityType getType() {
        return type;
    }
}
