package com.aytel;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;

public class GameLoop extends AnimationTimer {
    private final GraphicsContext landGC, aimsGC, cannonGC;
    private final Land land;
    private final Aims aims;
    private final Cannon cannon;

    private long time = System.nanoTime();

    GameLoop(GraphicsContext landGC, GraphicsContext aimsGC, GraphicsContext cannonGC) {
        this.landGC = landGC;
        this.aimsGC = aimsGC;
        this.cannonGC = cannonGC;

        land = new Land(landGC.getCanvas().getWidth(), landGC.getCanvas().getHeight(), Land.Mode.DEFAULT);
        cannon = new Cannon(land);
        aims = new Aims(land, Aims.Mode.DEFAULT);
    }

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
        }
    }

    void releaseKey(KeyEvent event) {
        cannon.doNothing();
    }
}
