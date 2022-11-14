package game.utils;

import game.Main;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.*;

public class Sounds {

    private static Clip clip;

    public static void playSound(String soundName) {
        try {
            URL url = Main.class.getResource("resources/Sounds/" + soundName + ".wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip ();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        clip.setFramePosition (0);
        clip.start();
    }

    public static Clip getClip() {
        return clip;
    }

}
