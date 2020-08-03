package Controller;

import Model.*;
import View.GameView;
import javafx.application.Application;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hauptklasse welche den Controller des MVC-Patterns repräsentiert.
 */
public class GameController {

    /**
     * Anzahl der Frames pro Sekunde.
     */
    private static final int FPS = 60;

    /**
     * Maximale Schleifendauer in Abhängigkeit von den FPS.
     */
    private static final long maxLoopTime = 1000 / FPS;

    /**
     * Ein GameModel-Objekt welches das Model des MVC-Patterns repräsentiert.
     */
    private static GameModel gameModel = new GameModel();

    /**
     * Ein GameView-Objekt welches die View des MVC-Patterns repräsentiert.
     */
    private static GameView gameView;

    /**
     * Das Event, welches als nächstes von der View behandelt wird.
     */
    private static Event currentEvent = Event.START_MENU;
    private static MenuOption option = MenuOption.START;
    private static boolean inStartMenu = true;
    private static boolean inIngameMenu = false;

    /**
     * Liste von nicht Spielschleifenunterbrechenden Events für die GameView.
     */
    private static List<Event> nonInterruptingEvents = new ArrayList<>();

    /**
     * Hilfsvariable diff um den Spieler nach Sprüngen wieder genau auf Bodenhöhe zu bringen.
     * Und Hilfsvariablen um zu checken, ob ein Level-Reset (Neuladen oder Laden des nächsten Levels) nötig ist.
     */
    private static int diff = 0;
    private static int reset = 0;
    private static boolean waitingForReset = false;

    /**
     * Behandelt die in den Menüs ausgewählte Option.
     */
    private static void acceptOption(){
        if (inStartMenu){
            switch (option) {
                case START:
                    inStartMenu = false;
                    gameModel.loadNewLevel(1);
                    nonInterruptingEvents.add(Event.BACKGROUNDMUSIC_START);
                    GameView.updateEvent(Event.NO_EVENT);
                    break;
                case OPTION:
                    GameModel.setOption(true);
                    option = MenuOption.CHARACTER;
                    break;
                case CLOSE:
                    System.exit(0);
                    break;
                case BACK:
                    option = MenuOption.START;
                    GameModel.setOption(false);
                    break;
                case CHARACTER:
                    break;
                default:
            }

        } else if (inIngameMenu) {
            switch (option) {
                case START:
                    GameView.updateEvent(Event.NO_EVENT);
                    inIngameMenu = false;
                    break;
                case OPTION:
                    GameModel.setOption(true);
                    option = MenuOption.CHARACTER;
                    break;
                case BACK:
                    option = MenuOption.START;
                    GameModel.setOption(false);
                    break;
                case CHARACTER:
                    break;
                default:
                    System.exit(0);
                    break;
            }
        }

    }

    /**
     * Wendet die Schwerkraft auf eine entity an.
     *
     * @param entity LivingEntity.
     */
    private static void applyGravity(LivingEntity entity) {
        entity.setY(entity.getY() + 8);  //Zweiter Teil in der Klammer ist die Gravity
        if (checkCollisions(gameModel.getSurroundingEntities(entity), gameModel.getAllEnemies(), entity)) {
            if (diff > 0) entity.setY(entity.getY()-diff);
            else entity.setY(entity.getOldY());
        }
    }

    /**
     * Prüft, ob entityToCheckFor mit einer Entity aus einer der gegebenen Listen kollidiert.
     *
     * @param entities Liste aller verfügbaren Entities.
     * @param enemies Liste bestehend aus EnemyEntities.
     * @param entityToCheckFor Spielercharakter oder andere LivingEntity.
     * @return true falls eine Kollision vorhanden ist, false sonst.
     */
    private static boolean checkCollisions(List<Entity> entities, List<Entity> enemies, Entity entityToCheckFor) {

        Shape overlap;
        //Check for collision with blocks
        for (Entity entity : entities) {
            if (!entity.isPassable()&& !entity.equals(entityToCheckFor)) {
                overlap = Shape.intersect(entity.getHitbox(), entityToCheckFor.getHitbox());
                diff = (int) Math.floor(overlap.getLayoutBounds().getHeight());
                if (((Path) overlap).getElements().size() > 0) {
                    //Zusätzlicher check nur falls es sich um den Spieler handelt
                    if (entityToCheckFor.equals(gameModel.getPlayerCharacter())) {
                        boolean output = !(entity instanceof Collectable);
                        //If Character collides at the top or at the bottom we have to reset the jumpingheight
                        if (overlap.getLayoutBounds().getWidth() > overlap.getLayoutBounds().getHeight()) {
                            if (entity instanceof Block && gameModel.getPlayerCharacter().isFalling()) {
                                gameModel.getPlayerCharacter().setJumpingHeight(0);
                            }
                            checkEntityForBlockAndInteract(entity);
                            gameModel.getPlayerCharacter().setFalling(true);

                        }
                        return output;
                    } else {
                        return true;
                    }
                }
            }
        }

        //check for collision with enemies
        for (Entity entity : enemies) {
            if (!entity.isPassable()&& !entity.equals(entityToCheckFor)) {
                overlap = Shape.intersect(entity.getHitbox(), entityToCheckFor.getHitbox());
                if (((Path) overlap).getElements().size() > 0) {
                    //Zusätzlicher check nur falls es sich um den Spieler handelt
                    if (entityToCheckFor.equals(gameModel.getPlayerCharacter())) {
                        boolean output = !(entity instanceof Collectable);
                        //check if entity is an enemy
                        //check which way they overlap
                        //+8 is tolerance
                        LivingEntity livingEntity = (LivingEntity) entity;
                        if (entityToCheckFor.getY() > (livingEntity.getY() - livingEntity.getHeight()) + 8) {
                            if (gameModel.getPlayerCharacter().isTall()){
                                currentEvent = Event.FORMCHANGE;
                                gameModel.getPlayerCharacter().setTall(false);
                                gameModel.getAllEnemies().remove(livingEntity);
                            } else
                                playerDeath();
                        } else {

                            /*
                            if (entity instanceof Collectable && ((Collectable) entity).getItemNumber() == 1){
                                gameModel.getPlayerCharacter().setTall(true);
                            }
                            */
                            if (livingEntity instanceof Mushroom){
                                currentEvent = Event.FORMCHANGE;
                                gameModel.getPlayerCharacter().setTall(true);
                            }
                            else if (livingEntity instanceof Coin) {
                                gameModel.addCoin();
                                nonInterruptingEvents.add(Event.COINSOUND);
                            } else if (livingEntity instanceof EnemyCharacter) {
                                nonInterruptingEvents.add(Event.ENEMYKILL);
                                gameModel.addDead(livingEntity);

                                //Kill Feedback via kleinem Sprung
                                gameModel.getPlayerCharacter().setFalling(false);
                                gameModel.getPlayerCharacter().startSmallJump();
                            }

                            gameModel.addHighscore(livingEntity.getReward());
                            gameModel.getAllEnemies().remove(livingEntity);
                            //add new floatText
                            GameView.addFloatText(livingEntity.getX(), livingEntity.getY(), livingEntity.getReward());
                        }
                        return output;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Prüft ob mit entity eine Interaktion nötig ist und ruft gegebenenfalls interactWithBlock auf.
     *
     * @param entity Eine Entity die geprüft wird.
     */
    private static void checkEntityForBlockAndInteract(Entity entity){
        if (interactionNessecary(entity)) {
            Block block = (Block)entity;
            interactWithBlock(block);
        }
    }

    /**
     * Prüft ob der Spielercharakter das Ziel des Levels erreicht hat und ruft levelFinished auf.
     * Hierzu werden die x Koordinate des Spielers und des Zielblocks miteinander vergleichen.
     */
    private static void checkForLevelFinished(){
        if (gameModel.getPlayerCharacter().getX() > gameModel.getLevel().getFinishBlock().getX() && gameModel.getPlayerCharacter().getX() < gameModel.getLevel().getFinishBlock().getX() + Block.BLOCK_SIZE
                || gameModel.getPlayerCharacter().getX() + Block.BLOCK_SIZE > gameModel.getLevel().getFinishBlock().getX() && gameModel.getPlayerCharacter().getX() + Block.BLOCK_SIZE < gameModel.getLevel().getFinishBlock().getX() + Block.BLOCK_SIZE
                && gameModel.getPlayerCharacter().getY() < gameModel.getLevel().getFinishBlock().getY())
            levelFinished();
    }

    /**
     * Setzt gegebenenfalls das Level auf seine Startwerte zurück.
     *
     * @param currentEvent Ein Event.
     */
    private static void checkForLevelReset(Event currentEvent){
        if (currentEvent.equals(Event.NO_EVENT) && gameView.noEventGoingOn() && waitingForReset) {
            gameModel.loadNewLevel(gameModel.getLevel().getLevelNumber());
            if(gameModel.getLevel().getLevelNumber() != 0)
                nonInterruptingEvents.add(Event.BACKGROUNDMUSIC_START);
            waitingForReset = false;
        }
    }

    /**
     * Stellt sicher, dass die Spielschleife nicht zu schnell läuft.
     *
     * @param timestamp Zeit bei Schleifenbeginn.
     * @param oldTimestamp Zeit vor Schleifenende.
     * @param maxLoopTime Maximale Schleifendaür.
     */
    private static void checkIfToFast(long timestamp, long oldTimestamp, long maxLoopTime){
        if (timestamp - oldTimestamp <= maxLoopTime) {
            try {
                Thread.sleep(maxLoopTime - (timestamp - oldTimestamp));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Verhindert, dass der Spielercharakter über die Fenstergrenzen hinausläuft.
     */
    private static void checkPlayerBounds() {
        //Right bound
        if (gameModel.getPlayerCharacter().getX() > gameView.getSceneWidth() - 32)
            gameModel.getPlayerCharacter().resetLastVerticalMove();

        //Left bound
        if (gameModel.getPlayerCharacter().getX() < 0)
            gameModel.getPlayerCharacter().resetLastVerticalMove();

        //Lower bound
        if (gameModel.getPlayerCharacter().getY() > gameView.getSceneHeight()) {
            playerDeath();
        }

        //Upper bound
        if (gameModel.getPlayerCharacter().getY() < Block.BLOCK_SIZE / 2)
            gameModel.getPlayerCharacter().setY(Block.BLOCK_SIZE / 2);
    }

    /**
     * Überprüft, ob der Spieler noch innerhalb des vorgegebenen Zeitlimits agiert.
     * Tötet den Spielercharakter andernfalls.
     */
    private static void checkTime(){
        if (gameModel.getTime() == 0) {
            GameView.updateEvent(Event.PLAYER_DEATH);
            waitingForReset = true;
        }
    }

    /**
     * Mit eine der Kernfunktionen im Controller, sie führt die Spiellogik (pro Tick) aus.
     */
    private static void gamelogicTick(){

        //-------------------------------- This is the part to make calculations. -------------------------------------
        updateEnemies(gameModel.getAllEnemies());

        applyGravity(gameModel.getPlayerCharacter());

        handleInput(gameView.getInput());

        handleJump();

        checkPlayerBounds();

        checkTime();

        //Testen ob Level geschafft
        checkForLevelFinished();
        //-------------------------------------------------------------------------------------------------------------
    }

    /**
     * Setzt den Spielercharakter in Bewegung.
     * @param inputs Eine HashMap aus Input, Boolean.
     */
    private static void handleInput(HashMap<Input, Boolean> inputs) {
        if (inputs.get(Input.LEFT)) {
            gameModel.getPlayerCharacter().moveLeft();

            if (checkCollisions(gameModel.getSurroundingEntities(gameModel.getPlayerCharacter()),gameModel.getAllEnemies(), gameModel.getPlayerCharacter()))
                gameModel.getPlayerCharacter().resetLastVerticalMove();
        }

        if (inputs.get(Input.RIGHT)) {
            //Scrollen nur, wenn der Spieler weiter als die Hälfte des Bildschirms weit ist und wenn der letzte Block des Levels nicht der letzte ist der angezeigt wird
            if (gameModel.getPlayerCharacter().getX() >= gameView.getSceneWidth() / 2 && gameModel.getLevel().getLastBlock().getX() > gameView.getSceneWidth() - Block.BLOCK_SIZE){
                moveEverything();
            } else {
                gameModel.getPlayerCharacter().moveRight();
            }
            if (checkCollisions(gameModel.getSurroundingEntities(gameModel.getPlayerCharacter()),gameModel.getAllEnemies(), gameModel.getPlayerCharacter()))
                gameModel.getPlayerCharacter().resetLastVerticalMove();
        }

        if (inputs.get(Input.RESET)) {
            gameModel.getPlayerCharacter().setPosition(0, 80);
        }

        if (inputs.get(Input.JUMP)) {
            if (gameModel.getPlayerCharacter().isFalling() && gameModel.getPlayerCharacter().getJumpingHeight() == 0) {
                nonInterruptingEvents.add(Event.JUMPSOUND);
                gameModel.getPlayerCharacter().setFalling(false);
                gameModel.getPlayerCharacter().startJump();
            }
        }

        //check for collision if nothing happens
        if (!inputs.get(Input.LEFT) && !inputs.get(Input.RIGHT)){
            if (gameModel.getPlayerCharacter().isMoving()) gameModel.getPlayerCharacter().resetImage();
            checkCollisions(gameModel.getSurroundingEntities(gameModel.getPlayerCharacter()),gameModel.getAllEnemies(),gameModel.getPlayerCharacter());
        }else{
            gameModel.getPlayerCharacter().setMoving();
        }
    }

    /**
     * Lässt den Spielercharakter springen.
     */
    private static void handleJump(){
        PlayerCharacter player = gameModel.getPlayerCharacter();
        if (!player.isFalling())player.setY(player.getY() - player.getJumpMovement());
        if(checkCollisions(gameModel.getSurroundingEntities(player),gameModel.getAllEnemies(), player)){
            player.setY(player.getOldY());
        }
    }

    /**
     * Reagiert auf die Tastatureingabe während ein Menü geöffnet ist.
     *
     * @param inputs Eine HashMap aus Input, Boolean
     */
    private static void handleMenuInput(Map<Input,Boolean> inputs){
        if (inputs.get(Input.MENU)) {
            if (GameModel.inOptions()){
                option = MenuOption.START;
                GameModel.setOption(false);
                System.out.println(GameModel.inOptions());
                inputs.put(Input.MENU, false);
            } else if (!gameView.getCurrentEvent().equals(Event.PLAYER_DEATH)){
                if (inIngameMenu){
                    gameView.setCurrentEvent(Event.NO_EVENT);
                    currentEvent = Event.NO_EVENT;
                } else {
                    currentEvent = Event.MENU_EVENT;
                }
                option = MenuOption.START;
                inputs.put(Input.MENU, false);
                inIngameMenu = !inIngameMenu;
            }
        }
        if (inIngameMenu || inStartMenu) {
            if (inputs.get(Input.UP)) {
                option = option.getByvalue(option.returnvalue() - 1, GameModel.inOptions());
                inputs.put(Input.UP, false);
            }
            if (inputs.get(Input.DOWN)) {
                option = option.getByvalue(option.returnvalue() + 1, GameModel.inOptions());
                inputs.put(Input.DOWN, false);
            }
            if (inputs.get(Input.ENTER)) {
                acceptOption();
                inputs.put(Input.ENTER, false);
            }

            if (inputs.get(Input.LEFT)) {
                inputs.put(Input.LEFT, false);
                if (GameModel.inOptions()&& option.equals(MenuOption.CHARACTER)) {
                    gameModel.getPlayerCharacter().changeCharacter();
                }
            }

            if (inputs.get(Input.RIGHT)) {
                inputs.put(Input.RIGHT, false);
                if (GameModel.inOptions() && option.equals(MenuOption.CHARACTER)) {
                    gameModel.getPlayerCharacter().changeCharacter();
                }
            }
        }

    }

    /**
     * Überprüft, ob mit gegebener Entity eine Interaktion möglich und nötig ist.
     * Zusätzlich wird ein Kollisionssound wiedergegeben.
     *
     * @param entity Eine beliebige Entity.
     * @return true, falls eine Interaktion nötig ist. False sonst.
     */
    private static boolean interactionNessecary(Entity entity){
        //Nur wenn die entity ein Block ist, interagierbar und die Interaktion vom Spieler von unten her stattfindet, wird true zurückgegeben
        if (entity instanceof Block && gameModel.getPlayerCharacter().getY() > entity.getY()) {
            nonInterruptingEvents.add(Event.BLOCKINTERACTIONSOUND);
            return ((Block)entity).isInteractable();
        }
        return false;
    }

    /**
     * Lässt die Spielfigur mit gegebenem Block interagieren.
     *
     * @param block Ein beliebiger, interagierbarer Block.
     */
    private static void interactWithBlock(Block block){
        switch (block.getType()){
            case QuestionMARK_BLOCK_COIN:
                block.setType(BlockType.USED_QuestionMARK_BLOCK);
                gameModel.getAllEnemies().add(new Coin(block.getX(), block.getY() - 40));
                break;
            case QuestionMARK_BLOCK_MUSHROOM:
                block.setType(BlockType.USED_QuestionMARK_BLOCK);
                gameModel.getAllEnemies().add(new Mushroom(block.getX(), block.getY() - Block.BLOCK_SIZE));
                nonInterruptingEvents.add(Event.POWERUPAPPEARS);
                break;
            case PLATFORM:
                gameModel.addDead(block);
                gameModel.removeEntity(block);
                break;
        }
    }

    /**
     * Startet Zielanimation und setzt das Level zurück
     *
     */
    private static void levelFinished(){
        currentEvent = Event.LEVEL_FINISHED;
        waitingForReset = true;
    }

    /** Die Mainfunktion. Es gibt keine Kommandozeilenparameter
     *  Startet die Graphische Benutzungsoberfläche in einem externen Thread.
     *  Enthält die Spielschleife, welche die nächsten Aktionen berechnet und Benutzereingaben abfängt.
     */
    public static void main(String... args) {
        new Thread(() -> Application.launch(GameView.class)).start();

        gameView = GameView.initialize();

        long timestamp;
        long oldTimestamp;

        while (gameView.isOpen()) {
            oldTimestamp = System.currentTimeMillis();

            checkForLevelReset(currentEvent);

            if (gameView.noEventGoingOn()) {
                gamelogicTick();
            }
            handleMenuInput(gameView.getInput());
            timestamp = System.currentTimeMillis();

            if (timestamp - oldTimestamp > maxLoopTime) {
                continue;   //continue -> Wir springen aus dem aktüllen Durchlauf wieder vor die While-Schleife, also überspringen wir das Zeichnen
            }
            gameView.update(gameModel, currentEvent, option, nonInterruptingEvents);
            if (currentEvent.equals(Event.LEVEL_FINISHED)){
                updateLevelFinished();
            }else {
                currentEvent = Event.NO_EVENT;  //Nach dem Update der GameView ist das Event auf jeden Fall behandelt worden
            }

            timestamp = System.currentTimeMillis();
            checkIfToFast(timestamp, oldTimestamp, maxLoopTime);    //If so, the method let's the thread sleep for the needed amount of time
        }
    }

    private static void updateLevelFinished() {
        if (reset < 440) {
            reset++;
        } else {
            currentEvent = Event.NO_EVENT;
            reset = 0;
            gameModel.loadNewLevel(gameModel.getLevel().getLevelNumber()+1);
        }
    }

    /**
     * Setzt die "Kamera" in Bewegung indem die Weltentities nach links verschoben werden.
     */
    private static void moveEverything(){

        int velocity = gameModel.getPlayerCharacter().getVelocity();

        //move intern position
        gameModel.movePos(velocity);
        GameView.moveFloatTexts(velocity);
        //trigger animation for playercharacter
        gameModel.getPlayerCharacter().animate();

        //moveBlocks
        for (Entity entity : gameModel.getAllEntities()){
            entity.setX(entity.getX() - velocity);
        }

        //move Enemies & Collectibles
        for (Entity entity : gameModel.getAllEnemies()){
            if (entity instanceof Fish) entity.set2X(entity.getX() - velocity);
            else entity.setX(entity.getX() - velocity);
        }

        //move Corpses
        for (DeadEntity e : gameModel.getDeadEntities()){
            e.setX(e.getOldX()- velocity);
        }
    }

    /**
     * Startet Todesanimation und setzt das Level zurück.
     */
    private static void playerDeath(){
        if (gameModel.getPlayerCharacter().isTall() && gameModel.getPlayerCharacter().isFalling()){
            gameModel.getPlayerCharacter().setTall(false);
        }else {
            currentEvent = Event.PLAYER_DEATH;
            waitingForReset = true;
        }
    }

    /**
     * Bewegt Gegner.
     * @param entityList Eine Liste bestehend aus Entities.
     */
    private static void updateEnemies(List<Entity> entityList){
        for (Entity entity : entityList) {
            if (entity instanceof EnemyCharacter){
                EnemyCharacter enemyCharacter = (EnemyCharacter)entity;
                enemyCharacter.nextTurn();

                applyGravity(enemyCharacter);
            }
            if (entity instanceof Collectable){
                Collectable collectable = (Collectable)entity;
                collectable.nextTurn();
                if (collectable instanceof Mushroom)
                    applyGravity(collectable);
            }
        }
    }
}