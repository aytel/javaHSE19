package com.aytel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

/** Mountain placed on the land. Cannon, aims and bullet can't be lower than any mountain. */
public class Mountain extends IntrusiveList.IntrusiveContainer<Mountain> implements Sprite {
    private final double MAX_WIDTH;
    private final Land land;

    final double firstBaseX, secondBaseX, apexX, apexY;

    /** Creates mountain in random place of the land. */
    public Mountain(Land land) {
        this.land = land;
        MAX_WIDTH = land.width / 5;
        var random = new Random();
        int width = random.nextInt((int)(MAX_WIDTH / 2)) + (int)(MAX_WIDTH / 2);
        firstBaseX = random.nextInt((int)(land.width * 0.9 - MAX_WIDTH)) + land.width * 0.1;
        secondBaseX = firstBaseX + width;
        apexY = land.plainHeight - random.nextInt((int)(land.height * 0.3));
        apexX = random.nextInt((int)(secondBaseX - firstBaseX - 1)) + firstBaseX + 0.5;
    }

    /** Returns y-coordinate of highest point of this mountain with given x-coordinate. */
    double getY(double x) {
        if (x <= firstBaseX || x >= secondBaseX) {
            return Double.POSITIVE_INFINITY;
        }

        if (x <= apexX) {
            return apexY + (land.plainHeight - apexY) * (apexX - x) / (apexX - firstBaseX);
        } else {
            return apexY + (land.plainHeight - apexY) * (x - apexX) / (secondBaseX - apexX);
        }
    }

    /** Mountain is a gray triangle with two vertices on the land and one above them. */
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillPolygon(new double[]{firstBaseX, apexX, secondBaseX},
                new double[]{land.plainHeight, apexY, land.plainHeight},
                3);
    }
}
