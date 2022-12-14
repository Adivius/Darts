import sum.kern.*;

import java.awt.*;
import java.util.Random;

/**
 * @Author: Adivius
 * @Version: 2.1
 * <p>
 * School projekt in Java with SuM
 */

public class Main {
    /**
     * Defining all necessary vars and consts
     */
    private final int SCREEN_HEIGHT = 600, SCREEN_WIGHT = 800, FALL_SPEED = 6, SHOOT_SPEED = 9, ROTATION_SPEED = 2, GAME_DELAY = 3;
    public int targetX = 0, targetY = 0, lastPoint = 0;
    public Bildschirm screen = new Bildschirm(SCREEN_WIGHT, SCREEN_HEIGHT);
    public Buntstift pen = new Buntstift();
    public Maus mouse = new Maus();
    public Tastatur key = new Tastatur();

    /**
     * Main function for starting to programm to create a Main object
     */
    public static void main(String[] args) {
        new Main();
    }

    /**
     * Constructor to run the game code
     */
    public Main() {
        while (true) {
            screen.setResizable(false);
            sleep(5);
            drawTarget();
            pen.bewegeBis(20, 10);
            boolean target = false;
            while (!mouse.istGedrueckt()) {
                pen.bewegeBis(pen.hPosition(), (pen.vPosition() + FALL_SPEED) % SCREEN_HEIGHT);
                drawDart(true);
                if (key.wurdeGedrueckt() && key.zeichen() == 'x') target = true;
            }
            int rotation = 0;
            while (mouse.istGedrueckt()) {
                if (target) {
                    pen.dreheZu(targetX, targetY);
                    break;
                }
                drawDart(true);
                rotation = rotation <= -80 ? 80 : rotation - ROTATION_SPEED;
                    pen.dreheBis(rotation);
            }
            while (pen.hPosition() + 30 <= targetX && pen.hPosition() >= 0) {
                drawDart(true);
                pen.bewegeUm(SHOOT_SPEED);
                if (key.wurdeGedrueckt()) {
                    switch (key.zeichen()) {
                        case 'w' -> pen.dreheUm(ROTATION_SPEED / 1.5);
                        case 's' -> pen.dreheUm(ROTATION_SPEED / -1.5);
                    }
                    key.weiter();
                }
            }
            pen.bewegeUm(30);
            boolean win = pen.vPosition() <= targetY + 35 && pen.vPosition() >= targetY - 35;
            int points = win ? 35 - Math.abs((int) (targetY - pen.vPosition())) : 0;
            int percentage = (points * 10) / 35;
            pen.bewegeUm(-30);
            drawTarget();
            writeMid(win ? "Win" : "Fail", percentage, lastPoint, win);
            lastPoint = percentage;
            drawDart(false);
            end();
        }
    }

    /**
     * Function for drawing dart every tick
     */
    public void drawDart(boolean delete) {
        pen.runter();
        pen.bewegeUm(30);
        pen.zeichneKreis(3);
        pen.hoch();
        if (delete) {
            sleep(20);
            pen.radiere();
            pen.runter();
            pen.zeichneKreis(3);
            pen.bewegeUm(-30);
            pen.hoch();
            pen.normal();
        }
    }

    /**
     * Function for drawing target at start and end of the game
     */
    public void drawTarget() {
        Buntstift targetPen = new Buntstift();
        if (targetX == 0) {
            targetX = SCREEN_WIGHT - 100;
            targetY = new Random().nextInt(SCREEN_HEIGHT - 150) + 100;
        }
        targetPen.bewegeBis(targetX, targetY);
        targetPen.zeichneKreis(35);
        targetPen.zeichneKreis(25);
        targetPen.zeichneKreis(15);
        targetPen.setzeFarbe(new Color(255, 0, 0));
        targetPen.zeichneKreis(5);
    }

    /**
     * Function for resetting the game
     */
    public void end() {
        targetX = 0;
        targetY = 0;
        sleep(GAME_DELAY * 1000);
        screen.loescheAlles();
    }

    /**
     * Writes the game status at the end of the game
     */
    public void writeMid(String text, int points, int lastPoint, boolean win) {
        text = text + ": " + points + "$, last game: " + lastPoint + "$";
        Buntstift writePen = new Buntstift();
        writePen.setzeFarbe(win ? Farbe.GRUEN : Farbe.ROT);
        writePen.bewegeBis((SCREEN_WIGHT / 2.0) - 80, SCREEN_HEIGHT / 2.0);
        writePen.setzeSchriftGroesse(20);
        writePen.schreibeText(text);
    }

    /**
     * Function for delaying the Thread
     */
    public void sleep(int t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}