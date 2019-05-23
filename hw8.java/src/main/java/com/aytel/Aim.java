package com.aytel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

/** Aim which player show fire to. */
public class Aim extends IntrusiveList.IntrusiveContainer<Aim> implements Sprite {
    final double RADIUS = 20;
    final double x, y;

    Aim(Land land) {
        var random = new Random();
        x = random.nextInt((int)(land.width * 0.90)) + land.width * 0.10;
        y = land.getY(x);
    }

    /** Aim is a red circle. */
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }
}
