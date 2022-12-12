import sum.kern.Bildschirm;
import sum.kern.Buntstift;
import sum.kern.Maus;
import sum.kern.Tastatur;
import java.util.Random;

public class Main {

    private final int SCREEN_HEIGHT = 400, SCREEN_WIGHT = 600, FALL_SPEED = 2, SHOOT_SPEED = 6, ROTATION_SPEED = 2;
    public int targetX, targetY;
    public Bildschirm screen = new Bildschirm(SCREEN_WIGHT, SCREEN_HEIGHT);
    public Buntstift pen = new Buntstift();
    public Maus mouse = new Maus();
    public Tastatur key = new Tastatur();

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        screen.setResizable(false);
        sleep(5);
        drawTarget();
        pen.bewegeBis(100, 10);

        while (!mouse.istGedrueckt()) {
            pen.bewegeBis(pen.hPosition(), (pen.vPosition() + FALL_SPEED) % SCREEN_HEIGHT);
            drawDart();
        }

        int rotation = 0;
        while (mouse.istGedrueckt()) {
            drawDart();
            rotation = rotation <= -80 ? 80 : rotation - ROTATION_SPEED;
            pen.dreheBis(rotation);
        }

        while (pen.hPosition() + 30 <= targetX && pen.hPosition() >= 0) {
            drawDart();
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
        int points = win ? 35 - Math.abs((int)(targetY - pen.vPosition())) : 0;
        int percentage = (points * 100) / 35;
        pen.bewegeUm(-30);
        drawTarget();
        end();
        writeMid(win ? "Win" : "Fail", percentage);
    }

    public void drawDart() {
        pen.runter();
        pen.bewegeUm(30);
        pen.zeichneKreis(3);
        pen.hoch();
        sleep(20);
        pen.radiere();
        pen.runter();
        pen.zeichneKreis(3);
        pen.bewegeUm(-30);
        pen.hoch();
        pen.normal();
    }

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
        targetPen.zeichneKreis(5);
    }

    public void end() {
        pen.normal();
        pen.runter();
        pen.bewegeUm(30);
        pen.zeichneKreis(3);
        pen.hoch();
    }

    public void writeMid(String text, int points) {
        text = text + ": " + points + " p.";
        Buntstift writePen = new Buntstift();
        writePen.bewegeBis(SCREEN_WIGHT / 2.0, SCREEN_HEIGHT / 2.0);
        writePen.setzeSchriftGroesse(20);
        writePen.schreibeText(text);
    }

    public void sleep(int t){
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }


}