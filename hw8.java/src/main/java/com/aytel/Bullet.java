package com.aytel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Bullet extends IntrusiveList.IntrusiveContainer<Bullet> implements Sprite {
    private final double g = 9.8e-17 * 3;

    final int RADIUS;
    private final double START_SPEED;

    double x, y;
    private double speedX, speedY;


    Bullet(double x, double y, double angle, Mode mode) {
        switch (mode) {
            case SMALL:
                RADIUS = 3;
                START_SPEED = 6e-7;
                break;
            case LARGE:
                RADIUS = 7;
                START_SPEED = 3.5e-7;
                break;
            default:
                RADIUS = 5;
                START_SPEED = 4.2e-7;
                break;
        }

        this.x = x;
        this.y = y;
        this.speedX = START_SPEED * cos(angle);
        this.speedY = START_SPEED * sin(angle);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillOval(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }

    @Override
    public void update(long delta) {
        speedY += g * delta;
        x += speedX * delta;
        y += speedY * delta;
    }

    enum Mode { SMALL, LARGE, DEFAULT }
}
