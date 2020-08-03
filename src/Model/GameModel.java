package Model;

import View.SpriteHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Repräsentiert das Model des MVC-Patterns.
 */
public class GameModel {

    private PlayerCharacter playerCharacter;
    private Level level = new Level(0);
    private int highscore;
    private int coinCounter;
    private int oldX;
    private List<Entity> allEntities;
    private List<Entity> allEnemies;
    private List<Entity> oldEntities;
    private List<DeadEntity> deadEntities = new LinkedList<>();
    private int position;
    private boolean lvlChanged;
    private long time;
    private static boolean inOption = false;

    /**
     * Initialisiert das Gamemodel mit seinen Standardwerten.
     */
    public GameModel() {
        playerCharacter = new PlayerCharacter(Block.BLOCK_SIZE, 15 * Block.BLOCK_SIZE);
        highscore = 0;
        coinCounter = 0;
        //start position need to be the middle of the screen
        position = 480;
        oldX = -1;
        allEntities = level.getLevelEntities();
        allEnemies = level.getLevelEnemies();
        time = System.currentTimeMillis();
    }

    /**
     * Zzur Abfrage der Spielfigur.
     * @return Das Objekt zur Spielfigur.
     */
    public PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    //for the view

    /**
     * Zur Abfrage der Entities im Sichtfenster.
     * @return Eine Liste der Entities die im sichtbaren Bereich des Spiels sind.
     */
    public List<Entity> getCloseEntities() {
        int sceneWidth = 30;
        int currentPos = position / 32;
        if (lvlChanged) {
            lvlChanged = false;
        } else if (currentPos <= oldX) {
            return oldEntities;
        }
        oldX = currentPos;
        //the lvl is 20 blocks high the 3 lowest blocks are redundant
        //the lvl is 30 blocks long
        //all entities are save in a linked list (the order in which the entities are added is saved)
        //the entities are added row after row
        //the index where the first relevant block starts is currentPos - sceneWidth/2;
        // -1 is is for safety
        int lvlSize = this.level.getLvlSize();
        currentPos = currentPos -sceneWidth/2;
        if (currentPos <= 0) currentPos = 0;
        else if (currentPos > lvlSize-30) currentPos = lvlSize -30;
        //remove all old Entities
        List<Entity> closeEntities = new ArrayList<>();

        int row = 0;
        int width = sceneWidth;
        if (currentPos <= lvlSize-31) width++;

        while(row < 20){
            for (int i = 0 ; i < width; i++ ) {
                Block next = (Block) allEntities.get(currentPos + i);
                if (next.getType() != BlockType.AIR) {
                    closeEntities.add(next);
                }
            }
            currentPos += lvlSize;
            row++;
        }
        oldEntities = closeEntities;
        return closeEntities;
    }

    //for collision check

    /**
     * Zur Abfrage der Entities in unmittelbarer Nähe zu entity.
     * @param entity Beliebige Entity.
     * @return Liste der Objekte in direkter Umgebung zu entity.
     */
    public List<Entity> getSurroundingEntities(Entity entity) {
        int sceneHeight = 20;
        //since block size is 16 we set our position to the next lower block
        int currentPosX = entity.getX() +(position-480);
        currentPosX = (currentPosX - currentPosX%32)/ 32 -1;
        int currentPosY = (entity.getY() - entity.getY()%32)/ 32 -1;
        int lvlSize = this.level.getLvlSize();

        if (currentPosY < 0) currentPosY = 0;
        if (currentPosX < 0) currentPosX = 0;

        //remove all old Entities
        List<Entity> surroundingEntities = new ArrayList<>();
        if(currentPosY >= sceneHeight-2) currentPosY = sceneHeight-3;
        if(currentPosX >= lvlSize-2) currentPosX =  lvlSize-3;
        currentPosY = currentPosY*lvlSize;
        currentPosX += currentPosY;


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                surroundingEntities.add(allEntities.get(currentPosX+j));
            }
            currentPosX += lvlSize;
        }
        return surroundingEntities;
    }

    /**
     * Zur Abfrage aller Entities.
     * @return Liste von Entities im Spiel.
     */
    public List<Entity> getAllEntities() {
        return allEntities;
    }

    /**
     * Zur Abfrage aller Gegner.
     * @return Liste von Gegnern im Spiel.
     */
    public List<Entity> getAllEnemies(){ return allEnemies;}

    /**
     * Initialisiert ein neüs Level und setzt die Modelwerte auf die entsprechenden Werte.
     * @param level ID des zu ladenden Levels.
     */
    public void loadNewLevel(int level){
        this.level = new Level(level);
        playerCharacter.setPosition(this.level.getStartingX(), this.level.getStartingY());
        highscore = 0;
        coinCounter = 0;
        oldX = -1;
        position = 480;
        time = System.currentTimeMillis();
        allEntities = this.level.getLevelEntities();
        allEnemies = this.level.getLevelEnemies();
        playerCharacter.setTall(false);
    }

    /**
     * Gibt das aktülle Level zurück.
     * @return Level.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Gibt den Highscore des aktüllen Spiels zurück.
     * @return Highscore als Integer.
     */
    public int getHighscore(){ return highscore; }

    /**
     * Erhöht den Highscore des aktüllen Spiels.
     * @param scoreToAdd Summand der auf den Highscore kumuliert wird.
     */
    public void addHighscore(int scoreToAdd){ highscore += scoreToAdd; }

    /**
     * Löscht eine Entity aus dem Spiel.
     * @param entity Zu löschende Entity.
     */
    public void removeEntity(Entity entity) {
        allEntities.set(allEntities.indexOf(entity), new Block(entity.getX(), entity.getY(), BlockType.AIR));
        lvlChanged = true;
    }

    /**
     * Inkrementiert die Anzahl an Münzen.
     */
    public void addCoin(){
        coinCounter += 1;
    }

    /**
     * Zur Abfrage der gesammelten Münzen.
     * @return Anzahl der Münzen des Spielers.
     */
    public int getCoinCounter(){
        return coinCounter;
    }

    /**
     * Fügt der Liste deadEntities eine tote Entity hinzu.
     * @param entity Hinzuzufügende Entity.
     */
    public void addDead(Entity entity){
        if (entity instanceof EnemyCharacter)
            deadEntities.add(new DeadEntity(((LivingEntity)entity).getImage(), entity.getX(), entity.getY(),70, DeadEntityType.GOOMBA));
        else if (entity instanceof Block) {
            deadEntities.add(new DeadEntity(SpriteHandler.initObjectSprite(4).get(0), entity.getX(),  entity.getY(), 20, DeadEntityType.BLOCK_LEFT));
            deadEntities.add(new DeadEntity(SpriteHandler.initObjectSprite(5).get(0), entity.getX(), entity.getY() + Block.BLOCK_SIZE, 20, DeadEntityType.BLOCK_LEFT));
            deadEntities.add(new DeadEntity(SpriteHandler.initObjectSprite(6).get(0), entity.getX() + Block.BLOCK_SIZE, entity.getY(), 20, DeadEntityType.BLOCK_RIGHT));
            deadEntities.add(new DeadEntity(SpriteHandler.initObjectSprite(7).get(0), entity.getX() + Block.BLOCK_SIZE, entity.getY() + Block.BLOCK_SIZE, 20, DeadEntityType.BLOCK_RIGHT));
        }
    }

    /**
     * Gibt die Liste der toten Entities zurück.
     * @return Liste der toten Entities.
     */
    public List<DeadEntity> getDeadEntities(){

        //let the dead ascend to heaven
        deadEntities.removeIf(DeadEntity::timeToRemoveDeadEntity);
        return deadEntities;
    }

    /**
     * Bewegt einen Block.
     * @param i Index des zu bewegenden Blocks.
     */
    public void movePos(int i){this.position += i;}

    /**
     * Zur Anzeige des aktuellen Levels.
     * @return Stringrepräsentation des Levels.
     */
    public String getlvl(){return level.toString();}

    /**
     * Zur Abfrage der übriggebliebenen Zeit.
     * @return Übrige zeit.
     */
    public int getTime() {
        if (level.getLevelNumber() == 0) return 999;
        return (int)(300-((System.currentTimeMillis()-time)/1000));
    }

    public static boolean inOptions(){return inOption;}

    public static void setOption(boolean b) {inOption = b;}

}
