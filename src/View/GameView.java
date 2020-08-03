package View;

import Controller.Input;
import Model.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class GameView extends Application {
    private static CountDownLatch latch = new CountDownLatch(1);
    private static GameView gameView;
    private static boolean isOpen = false; //needs to be false initially in case the gui dösn't start. Then the gameloop won't be executed.
    private static GraphicsContext gc;
    private static Canvas display;
    private static HashMap<Input, Boolean> keysPressed = new HashMap<>();
    private static int sceneWidth = 960;
    private static int sceneHeight = 640;
    private static List<int[]> floatText = new ArrayList<>();
    private static List<Image> menuSprites = new LinkedList<>();
    private static List<Image> startMenuSprites = new LinkedList<>();
    private static List<Image> uiSprites = new LinkedList<>();
    private static int atCoinImage = 0;
    private static int frameCount = 0;
    private static final int coinImages = 2;

    private static Event currentEvent = Event.NO_EVENT;
    private static int eventStep = 0;

    public GameView() {
        setGameView(this);
    }

    //Use singleton for the graphical userinterface.
    public static GameView initialize() {
        try {
            latch.await();

            //Latch somehow is kind of too fast for the gui thread, so we give it one more second to start
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }

        for (Input input : Input.values()) {
            keysPressed.put(input, false);
        }
        return gameView;
    }

    //Make other thread for gui to be initialized by countdownlatch.
    private static void setGameView(GameView view) {
        gameView = view;
        isOpen = true;
        latch.countDown();

        //load MenuSprites
        menuSprites.add(SpriteHandler.initMenuSprite(2));
        menuSprites.add(SpriteHandler.initMenuSprite(1));
        //load startMenuSprites
        startMenuSprites.add(SpriteHandler.initMenuSprite(0));
        startMenuSprites.add(SpriteHandler.initMenuSprite(1));
        //load ui sprites
        uiSprites = SpriteHandler.initUISprite(0);
    }

    public Event getCurrentEvent(){
        return currentEvent;
    }

    //Condition for gameloop to be true.
    public boolean isOpen() {
        return isOpen;
    }

    //Update the view by the given gamemodel.
    public void update(GameModel gameModel, Event event, MenuOption option, List<Event> nonInterruptingEvents) {
        Platform.runLater(() -> {
            //Submodels will have there own "sub"-update-function.

            PlayerCharacter player = gameModel.getPlayerCharacter();

            //Background
            gc.setFill(Color.rgb(146, 144, 255));
            gc.fillRect(0, 0, display.getWidth(), display.getHeight());

            //Blocks & Enimies & Corpses
            updateEntities(gameModel.getCloseEntities(), gameModel.getAllEnemies(), gameModel.getDeadEntities());

            //draw Interface
            drawInterface(gameModel);

            //draw floatTexts
            drawFloatText();


            //Das übergebene Event wird gesetzt falls noch kein anderes Event behandelt wird
            if (currentEvent.equals(Event.NO_EVENT))
                currentEvent = event;
            switch (currentEvent) {  //Je nach aktüllem Event wird eine andere update Methode aufgerufen
                case PLAYER_DEATH:
                    updatePlayerDeath(gameModel);
                    break;
                case LEVEL_FINISHED:
                    updateLevelFinished(gameModel);
                    break;
                case FORMCHANGE:
                    updateFormChange(gameModel);
                    break;
                case MENU_EVENT:
                    //System.out.println("Spielmenü wurde aufgerufen.");
                    drawPlayer(player);
                    drawMenu(option,gameModel);
                    break;
                case START_MENU://
                    //PlayerCharacter
                    drawPlayer(player);
                    //Menu
                    drawStartMenu(option,gameModel);
                    break;
                default:    //entspricht case No_EVENT:, es passiert nichts
                    //PlayerCharacter
                    drawPlayer(player);

                    Iterator<Event> iterator = nonInterruptingEvents.iterator();
                    while(iterator.hasNext()){
                        switch (iterator.next()){
                            case COINSOUND:
                                SoundHandler.playCoinSound();
                                iterator.remove();
                                break;
                            case JUMPSOUND:
                                SoundHandler.playJumpSound();
                                iterator.remove();
                                break;
                            case BACKGROUNDMUSIC_START:
                                SoundHandler.startBackgroundMusic();
                                iterator.remove();
                                break;
                            case ENEMYKILL:
                                SoundHandler.playStompSound();
                                iterator.remove();
                                break;
                            case POWERUPAPPEARS:
                                SoundHandler.playPowerUpAppearsSound();
                                iterator.remove();
                                break;
                            case BLOCKINTERACTIONSOUND:
                                SoundHandler.playBumpSound();
                                iterator.remove();
                                break;
                            default:
                                break;
                        }
                    }
                    break;
            }
        });
    }

    private void drawPlayer(PlayerCharacter player){
        int y = player.getY();
        if (player.isTall())y -= 32;
        if (player.runsForward()) {
            if (player.isFalling()) gc.drawImage(player.getImage(), player.getX(), y);
            else gc.drawImage(player.getJumpImage(), player.getX(), y);
        }else{
            if (player.isFalling()) {
                gc.drawImage(player.getImage(), player.getX() + player.getImage().getWidth(), y
                        , -player.getImage().getWidth(), player.getImage().getHeight());
            }else {
                gc.drawImage(player.getJumpImage(), player.getX() + player.getImage().getWidth(), y
                        , -player.getImage().getWidth(), player.getImage().getHeight());
            }
        }
    }

    public HashMap<Input, Boolean> getInput() {
        return keysPressed;
    }

    private void handleKeys(KeyCode keyCode, boolean pressed) {
        switch (keyCode) {
            case LEFT:
            case A:
                keysPressed.put(Input.LEFT, pressed);
                break;
            case RIGHT:
            case D:
                keysPressed.put(Input.RIGHT, pressed);
                break;
            case BACK_SPACE:
            case R:
                keysPressed.put(Input.RESET, pressed);
                break;
            case SPACE:
                keysPressed.put(Input.JUMP, pressed);
                break;
            case UP:
            case W:
                keysPressed.put(Input.UP, pressed);
                break;
            case DOWN:
            case S:
                keysPressed.put(Input.DOWN, pressed);
                break;
            case ENTER:
                keysPressed.put(Input.ENTER, pressed);
                break;
            case ESCAPE:
                if (currentEvent.equals(Event.MENU_EVENT) && pressed) {
                    keysPressed.put(Input.MENU, true);
                    //currentEvent = Event.NO_EVENT;
                    //System.out.println("Menü geschlossen.");
                } else if (pressed) {
                    keysPressed.put(Input.MENU, true);
                    //System.out.println("Menü geöffnet.");
                }
        }
    }

    //A subupdatemethod to update blocks.
    private void updateEntities(List<Entity> entities,List<Entity> enemies, List<DeadEntity> deadEntities) {
        for (Entity entity : entities) {
            if (entity instanceof Block) {   //Nur die Blöcke sollen gemalt werden
                gc.drawImage(((Block) entity).getImage(), entity.getX(), entity.getY());
            }
        }

        for (Entity entity : enemies){
            if (entity instanceof EnemyCharacter){
                //Enemies
                if (((EnemyCharacter)entity).runsForward()) {
                    gc.drawImage(((EnemyCharacter) entity).getImage(), entity.getX(), entity.getY());
                }else{
                    gc.drawImage(((EnemyCharacter) entity).getImage(), entity.getX()+((EnemyCharacter) entity).getImage().getWidth(), entity.getY(),
                            - ((EnemyCharacter) entity).getImage().getWidth(),((EnemyCharacter) entity).getImage().getHeight());
                }
            } else if (entity instanceof Collectable){
                Collectable cEntity = (Collectable) entity;
                gc.drawImage(cEntity.getImage(), entity.getX(), entity.getY());
            }
        }

        //draw Corpses (upside down)
        for (DeadEntity e: deadEntities){
            if (e.getType().equals(DeadEntityType.GOOMBA))
                gc.drawImage(e.getImage(), e.getX(),e.getY() + e.getHeight(), e.getWidth(), -e.getHeight());
            else
                gc.drawImage(e.getImage(), e.getX(), e.getY(), 16, 16);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, sceneWidth,  sceneHeight, Color.WHITE);
        display = new Canvas(root.getWidth(), root.getHeight());
        gc = display.getGraphicsContext2D();
        gc.setStroke(Paint.valueOf("white"));
        gc.setFont(Font.font("Arial",30));
        root.getChildren().add(display);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> isOpen = false);
        primaryStage.show();

        //It will prevent from stopping after pressing and releasing  a key while holding down another one.
        scene.setOnKeyPressed(e -> handleKeys(e.getCode(), true));
        scene.setOnKeyReleased(e -> handleKeys(e.getCode(), false));
    }

    public boolean noEventGoingOn() {
        return currentEvent.equals(Event.NO_EVENT); //Falls ein anderes Event als NO_EVENT, so wird true zurückgegeben
    }

    public void setCurrentEvent(Event e){currentEvent = e;}

    private void updatePlayerDeath(GameModel gameModel) {
        if (eventStep == 0) {
            SoundHandler.stopBackgroundMusic();
            SoundHandler.playDeathSound();
        }
        if (eventStep < 150) {
            gc.drawImage(gameModel.getPlayerCharacter().getDeathImage(), gameModel.getPlayerCharacter().getOldX(), gameModel.getPlayerCharacter().getOldY() - 200 * Math.sin(2 * Math.PI * eventStep / 200));
            eventStep++;
        } else {
            currentEvent = Event.NO_EVENT;  //Event fertig behandelt -> reset auf NO_EVENT, bereit ein neüs Event zu behandeln
            eventStep = 0;  //EventSteps wieder auf 0 setzen
        }
    }

    private void updateLevelFinished(GameModel gameModel){
        if (eventStep == 0){
            SoundHandler.stopBackgroundMusic();
            SoundHandler.playFlagpoleSound();
        }
        int playerY = (gameModel.getPlayerCharacter().isTall() ? gameModel.getPlayerCharacter().getY() + 26 : gameModel.getPlayerCharacter().getY());
        if (playerY + eventStep < 14 * Block.BLOCK_SIZE) {
            gc.drawImage(gameModel.getPlayerCharacter().getFinishImage(), gameModel.getPlayerCharacter().getOldX(), gameModel.getPlayerCharacter().getOldY() + eventStep);
            eventStep++;
        } else if (eventStep < 200) {
            if (gameModel.getPlayerCharacter().isTall())
                gc.drawImage(gameModel.getPlayerCharacter().getFinishImage(), gameModel.getPlayerCharacter().getOldX(), 640 - 7 * Block.BLOCK_SIZE + 6);
            else
                gc.drawImage(gameModel.getPlayerCharacter().getFinishImage(), gameModel.getPlayerCharacter().getOldX(), 640 - 6 * Block.BLOCK_SIZE);
            eventStep++;
        } else if (eventStep < 420) {
            if (eventStep == 200)
                SoundHandler.playStageClearSound();

            if (gameModel.getPlayerCharacter().isTall())
                gc.drawImage(gameModel.getPlayerCharacter().getFinishImage(), gameModel.getPlayerCharacter().getOldX(), 640 - 7 * Block.BLOCK_SIZE + 6);
            else
                gc.drawImage(gameModel.getPlayerCharacter().getFinishImage(), gameModel.getPlayerCharacter().getOldX(), 640 - 6 * Block.BLOCK_SIZE);

            int rectGrowth = eventStep - 200;
            gc.setFill(Color.BLACK);
            gc.fillRect(-10, -10, rectGrowth * 2, 650);
            gc.fillRect(-10, -10, 970, rectGrowth * 2);
            gc.fillRect(-10, 650 - (rectGrowth * 2), 970, 650);
            gc.fillRect(970 - (rectGrowth * 2), -10, 970, 650);
            eventStep++;
        }else if (eventStep < 540){
            gc.setFill(Color.BLACK);
            gc.fillRect(-10, -10, 1100, 700);
            gc.setFill(Color.WHITE);
            //World display
            String world = "World  ";
            world += gameModel.getlvl();
            gc.fillText(world, 435, 335);
            eventStep++;
        } else {
            currentEvent = Event.NO_EVENT;
            eventStep = 0;
        }
    }

    private void updateFormChange(GameModel gameModel){
        if (eventStep == 0){
            SoundHandler.playPowerUpSound();
        }
        if ((eventStep % 10 < 5) && eventStep < 60) {
            if (gameModel.getPlayerCharacter().isTall())
                gc.drawImage(gameModel.getPlayerCharacter().getSmallStandingImage(), gameModel.getPlayerCharacter().getX(), gameModel.getPlayerCharacter().getY());
            else
                gc.drawImage(gameModel.getPlayerCharacter().getTallStandingImage(), gameModel.getPlayerCharacter().getX(), gameModel.getPlayerCharacter().getY() - 32);
            eventStep++;
        } else if (eventStep < 90){
            if (gameModel.getPlayerCharacter().isTall())
                gc.drawImage(gameModel.getPlayerCharacter().getTallStandingImage(), gameModel.getPlayerCharacter().getX(), gameModel.getPlayerCharacter().getY() - 32);
            else
                gc.drawImage(gameModel.getPlayerCharacter().getSmallStandingImage(), gameModel.getPlayerCharacter().getX(), gameModel.getPlayerCharacter().getY());
            eventStep++;
        } else {
            currentEvent = Event.NO_EVENT;
            eventStep = 0;
        }
    }

    public double getSceneWidth(){
        return sceneWidth;
    }

    public double getSceneHeight(){
        return sceneHeight;
    }


    private void drawStartMenu(MenuOption option, GameModel gameModel){
        gc.drawImage(startMenuSprites.get(0),250,50,450,200);
        gc.setFill(Color.WHITE);
        if (!GameModel.inOptions()) {
            gc.fillText("START GAME", 350, 320);
            gc.fillText("OPTIONS", 350, 370);
            gc.fillText("CLOSE", 350, 420);
            gc.drawImage(startMenuSprites.get(1), 320, 300 + option.returnvalue() * 50, 20, 20);
        } else {
            //in options
            gc.fillText("Character", 350, 320);
            gc.fillPolygon(new double[]{470,475,475},new double[] {314,319,309}, 3);
            gc.fillPolygon(new double[]{575,575,580},new double[] {319,309,314}, 3);
            gc.fillText("BACK", 350, 370);
            if (gameModel.getPlayerCharacter().isLuigi()){
                gc.fillText("Luigi",500,320);
            }else{
                gc.fillText("Mario",500,320);
            }
            gc.drawImage(menuSprites.get(1),300,300 + option.returnvalue() * 50,20,20);
        }
    }

    private void drawMenu(MenuOption opt, GameModel gameModel){
        gc.drawImage(menuSprites.get(0),300,50,400,400);
        gc.setFill(Color.WHITE);
        if (!GameModel.inOptions()) {
            gc.fillText("RESUME", 450, 120);
            gc.fillText("OPTIONS", 450, 170);
            gc.fillText("EXIT GAME", 450, 220);
            gc.drawImage(menuSprites.get(1),400,100 + opt.returnvalue() * 50,20,20);
        }else {
            //in options
            gc.fillText("Character", 400, 120);
            gc.fillPolygon(new double[]{520,525,525},new double[] {114,119,109}, 3);
            gc.fillPolygon(new double[]{625,625,630},new double[] {119,109,114}, 3);
            gc.fillText("BACK", 400, 170);
            if (gameModel.getPlayerCharacter().isLuigi()){
                gc.fillText("Luigi",550,120);
            }else{
                gc.fillText("Mario",550,120);
            }
            gc.drawImage(menuSprites.get(1),350,100 + opt.returnvalue() * 50,20,20);
        }

    }

    //necessary to get put out of menu loop
    public static void updateEvent(Event event){
        currentEvent = event;
    }

    private void drawInterface(GameModel gameModel){
        //Highscore
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("test", 20));
        String score = Integer.toString(gameModel.getHighscore());
        StringBuilder scoreBuilder = new StringBuilder("Score: ");

        for (int i = 0 ; i < 9 - score.length(); i++){
            scoreBuilder.append(0);
        }
        scoreBuilder.append(score);
        gc.fillText(scoreBuilder.toString(),15,30);

        //Coin Counter
        scoreBuilder = new StringBuilder();
        if (gameModel.getCoinCounter() < 10){
            scoreBuilder.append("x0");
        } else {
            scoreBuilder.append("x");
        }
        scoreBuilder.append(gameModel.getCoinCounter());
        gc.fillText(scoreBuilder.toString(),225,30);
        gc.drawImage(uiSprites.get(atCoinImage),210,15);
        updateUICoin();

        //World display
        String world = "World  ";
        world+= gameModel.getlvl();
        gc.fillText(world, 435, 30);

        //Time display
        scoreBuilder = new StringBuilder("Time: ");
        int time = gameModel.getTime();
        if (time < 0) time = 0;
        if (time < 100) scoreBuilder.append(0);
        if (time < 10) scoreBuilder.append(0);
        scoreBuilder.append(time);
        gc.fillText(scoreBuilder.toString(), 860,30);

    }

    public static void addFloatText(int x, int y, int score){
        int[] newFloat = new int[4];
        newFloat[0] = x;
        newFloat[1] = y;
        newFloat[2] = score;
        newFloat[3] = 0;
        floatText.add(newFloat);

    }

    private void drawFloatText(){
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("test2",20));
        StringBuilder sb;
        Iterator<int[]> iterator = floatText.iterator();
        int[] next;

        while (iterator.hasNext()){
            next = iterator.next();
            sb = new StringBuilder();
            sb.append(next[2]);
            gc.fillText(sb.toString(),next[0], next[1]);
            next[3]++;
            next[1]--;

            if (next[3]> 39){
                iterator.remove();
            }
        }
    }

    public static void moveFloatTexts(int shift){
        for (int[] next:floatText) {
            next[0] += shift;
        }
    }

    private static void updateUICoin(){
        // Animation for the coin symbol
        frameCount++;
        if (frameCount > 30) {
            frameCount = 0;
            atCoinImage++;
            atCoinImage %= coinImages;
        }
    }
}