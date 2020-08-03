package View;

import javafx.scene.media.AudioClip;

public abstract class SoundHandler {

    private static final AudioClip backgroundMusic = new AudioClip((SoundHandler.class.getResource("/Sounds/BGMusic_1.wav")).toString());
    private static final AudioClip jumpSound = new AudioClip((SoundHandler.class.getResource("/Sounds/jump.wav")).toString());
    private static final AudioClip deathSound = new AudioClip((SoundHandler.class.getResource("/Sounds/ded.wav")).toString());
    private static final AudioClip coinSound = new AudioClip((SoundHandler.class.getResource("/Sounds/coin.wav")).toString());
    private static final AudioClip flagpoleSound = new AudioClip((SoundHandler.class.getResource("/Sounds/flagpole.wav")).toString());
    private static final AudioClip stompSound = new AudioClip((SoundHandler.class.getResource("/Sounds/stomp.wav")).toString());
    private static final AudioClip powerUpSound = new AudioClip((SoundHandler.class.getResource("/Sounds/powerup.wav")).toString());
    private static final AudioClip powerUpAppearsSound = new AudioClip((SoundHandler.class.getResource("/Sounds/powerup_appears.wav")).toString());
    private static final AudioClip bumpSound = new AudioClip((SoundHandler.class.getResource("/Sounds/bump.wav")).toString());
    private static final AudioClip stageClearSound = new AudioClip((SoundHandler.class.getResource("/Sounds/stage_clear.wav")).toString());

    static void startBackgroundMusic(){
        if (backgroundMusic.isPlaying())
            backgroundMusic.stop();
        backgroundMusic.setCycleCount(Integer.MAX_VALUE);
        backgroundMusic.play();
    }

    static void stopBackgroundMusic(){
        if(backgroundMusic.isPlaying()) backgroundMusic.stop();
    }

    static void playJumpSound(){ jumpSound.play();}

    static void playDeathSound(){ deathSound.play();}

    static void playCoinSound(){ coinSound.play();}

    static void playFlagpoleSound(){ flagpoleSound.play();}

    static void playStompSound(){ stompSound.play();}

    static void playPowerUpSound(){ powerUpSound.play();}

    static void playPowerUpAppearsSound(){ powerUpAppearsSound.play();}

    static void playBumpSound(){ bumpSound.play(); }

    static void playStageClearSound(){ stageClearSound.play();}
}
