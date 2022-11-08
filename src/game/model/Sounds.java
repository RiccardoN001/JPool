package game.model;

import game.Main;

import javax.sound.sampled.*;

import java.io.IOException;
import java.net.URL;

public class Sounds {

    private static Clip ballClip;
    private static Clip pocketClip;
    private static Clip cueClip;
    private static Clip cueSplitClip;
    private static Clip splitClip;


    public static void ballSound() {
        try {
            URL url = Main.class.getResource("resources/Sounds/BallSound.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            ballClip = AudioSystem.getClip ();
            ballClip.open (audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        } catch (LineUnavailableException e) {
            e.printStackTrace ();
        }

        ballClip.setFramePosition (0);
        ballClip.start ();
    }
    public static void pocketSound() {
        try {
            URL url = Main.class.getResource("resources/Sounds/PocketSound.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream (url);
            pocketClip = AudioSystem.getClip ();
            pocketClip.open (audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        } catch (LineUnavailableException e) {
            e.printStackTrace ();
        }

        pocketClip.setFramePosition (0);
        pocketClip.start ();
    }
    public static void cueSound() {
        try {
            URL url = Main.class.getResource("resources/Sounds/CueSound.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream (url);
            cueClip = AudioSystem.getClip ();
            cueClip.open (audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        } catch (LineUnavailableException e) {
            e.printStackTrace ();
        }

        cueClip.setFramePosition (0);
        cueClip.start ();
    }
    public static void cueSplitSound() {
        try {
            URL url = Main.class.getResource("resources/Sounds/CueSplitSound.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream (url);
            cueSplitClip = AudioSystem.getClip ();
            cueSplitClip.open (audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        } catch (LineUnavailableException e) {
            e.printStackTrace ();
        }

        cueSplitClip.setFramePosition (0);
        cueSplitClip.start ();
    }
    public static void splitSound() {
        try {
            URL url = Main.class.getResource("resources/Sounds/SplitSound.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream (url);
            splitClip = AudioSystem.getClip ();
            splitClip.open (audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        } catch (LineUnavailableException e) {
            e.printStackTrace ();
        }

        splitClip.setFramePosition (0);
        splitClip.start ();
    }

}

