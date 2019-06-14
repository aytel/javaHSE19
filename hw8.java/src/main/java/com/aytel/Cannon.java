package com.aytel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.*;

/** Cannon player should control. */
public class Cannon implements Sprite {
    private final int BASE_HEIGHT = 30;
    private final int BASE_WIDTH = 30;
    private final int TRUNK_LENGTH = 40;
    private final int TRUNK_GAUGE = 10;

    private final Land land;
    private final IntrusiveList<Bullet> bullets = new IntrusiveList<>();

    Bullet.Mode bulletMode = Bullet.Mode.DEFAULT;

    private double speedX = 0.0, speedTrunk = 0.0;

    /** Creates cannon and places it on the land. */
    Cannon(Land land) {
        this.land = land;
        x = land.width / 50.0;
    }

    private double x;
    private double angle = 0.0;

    /** Cannon is a base, which is square, and a trunk, which is line. */
    @Override
    public void draw(GraphicsContext gc) {
        double x = this.x, y = land.getY(x);
        drawBase(gc, x, y);
        drawTrunk(gc, x, y);
        drawBullets(gc);
    }

    private void drawBullets(GraphicsContext gc) {
        bullets.forEach(bullet -> bullet.draw(gc));
    }

    private void drawTrunk(GraphicsContext gc, double x, double y) {
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(TRUNK_GAUGE);
        gc.strokeLine(x, y, x + TRUNK_LENGTH * cos(angle), y + TRUNK_LENGTH * sin(angle));
    }

    private void drawBase(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.FORESTGREEN);
        gc.fillRect(x - (BASE_WIDTH / 2), y - (BASE_HEIGHT / 2), BASE_WIDTH, BASE_HEIGHT);
    }

    /** Cannon has speed of base and speed of trunk turning which depend on the pressed keys. */
    @Override
    public void update(long delta) {
        x += speedX * delta;
        angle += speedTrunk * delta;
        bullets.forEach(bullet -> bullet.update(delta));
    }

    /** Creates new bullet at the end of the trunk. */
    void fire() {
        bullets.add(new Bullet(x + TRUNK_LENGTH * cos(angle), land.getY(x) + TRUNK_LENGTH * sin(angle), angle, bulletMode));
    }

    /** Checks which aims must be destroyed due to bullets near them. */
    void checkBulletsWithAims(IntrusiveList<Aim> aims) {
        bullets.forEach(bullet -> aims.forEach(aim -> {
            double dx = abs(bullet.x - aim.x), dy = abs(bullet.y - aim.y);
            if (dx * dx + dy * dy <= pow(bullet.RADIUS + aim.RADIUS, 2)) {
                aim.removeFromList();
            }
        }));
    }

    /** Checks which bullets must be destroyed due to touching the land. */
    void checkBulletsWithLand() {
        List<Bullet> toDelete = new LinkedList<>();

        bullets.forEach(bullet -> {
            if (bullet.y > land.getY(bullet.x)) {
                toDelete.add(bullet);
            }
        });

        toDelete.forEach(Bullet::removeFromList);
    }

    void moveLeft() {
        speedX = -1.0e-7;
    }

    void moveRight() {
        speedX = 1.0e-7;
    }

    void upTrunk() {
        speedTrunk = -0.03e-7;
    }

    void downTrunk() {
        speedTrunk = 0.03e-7;
    }

    void doNothing() {
        speedX = speedTrunk = 0.0;
    }
}
