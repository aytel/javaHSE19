package com.aytel;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;

/** Main gameloop. Works until all aims are detroyed. */
public class GameLoop extends AnimationTimer {
    private final GraphicsContext landGC, aimsGC, cannonGC;
    private final Land land;
    private final Aims aims;
    private final Cannon cannon;
    private final Main main;

    private long time = System.nanoTime();

    /** Creates all game objects. */
    GameLoop(Main main, GraphicsContext landGC, GraphicsContext aimsGC, GraphicsContext cannonGC) {
        this.main = main;
        this.landGC = landGC;
        this.aimsGC = aimsGC;
        this.cannonGC = cannonGC;

        land = new Land(landGC.getCanvas().getWidth(), landGC.getCanvas().getHeight(), Land.Mode.DEFAULT);
        cannon = new Cannon(land);
        aims = new Aims(land, Aims.Mode.DEFAULT);
    }

    /** Draws all still existing game objects and finishes game if there's no aims left. */
    @Override
    public void handle(long now) {
        landGC.clearRect(0, 0, landGC.getCanvas().getWidth(), landGC.getCanvas().getHeight());
        aimsGC.clearRect(0, 0, aimsGC.getCanvas().getWidth(), aimsGC.getCanvas().getHeight());
        cannonGC.clearRect(0, 0, cannonGC.getCanvas().getWidth(), cannonGC.getCanvas().getHeight());

        long delta = now - time;

        cannon.checkBulletsWithAims(aims.aims);
        cannon.checkBulletsWithLand();

        aims.update(delta);
        cannon.update(delta);

        land.draw(landGC);
        aims.draw(aimsGC);
        cannon.draw(cannonGC);

        time = now;

        if (aims.finished()) {
            main.finish();
            stop();
        }
    }

    void pressKey(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER:
                cannon.fire();
                break;
            case LEFT:
                cannon.moveLeft();
                break;
            case RIGHT:
                cannon.moveRight();
                break;
            case UP:
                cannon.upTrunk();
                break;
            case DOWN:
                cannon.downTrunk();
                break;
            case DIGIT1:
                cannon.bulletMode = Bullet.Mode.SMALL;
                break;
            case DIGIT2:
                cannon.bulletMode = Bullet.Mode.DEFAULT;
                break;
            case DIGIT3:
                cannon.bulletMode = Bullet.Mode.LARGE;
                break;
        }
    }

    void releaseKey(KeyEvent event) {
        cannon.doNothing();
    }
}
