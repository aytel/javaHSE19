package com.aytel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.stream.IntStream;

import static java.lang.Math.max;
import static java.lang.Math.min;

class Land implements Sprite {
    private final double HEIGHT_COEFFICIENT = 0.7;
    private final int PLAIN_WIDTH = 7;
    private final int DEFAULT_COUNT = 20;

    final double width;
    final double height;
    final double plainHeight;
    private final IntrusiveList<Mountain> mountains = new IntrusiveList<>();

    Land(double width, double height, Mode mode) {
        this.width = width;
        this.height = height;
        this.plainHeight = height * HEIGHT_COEFFICIENT;


        int count = DEFAULT_COUNT;

        switch (mode) {
            case EMPTY:
                count = 0;
                break;
        }

        IntStream.range(0, count).forEach(i -> mountains.add(new Mountain(this)));
    }

    @Override
    public void draw(GraphicsContext gc) {
        drawPlain(gc);
        drawMountains(gc);
    }

    double getY(double x) {
        double[] result = new double[1];
        result[0] = plainHeight;
        mountains.forEach((mountain -> result[0] = min(result[0], mountain.getY(x))));
        return result[0];
    }

    private void drawMountains(GraphicsContext gc) {
        mountains.forEach(mountain -> mountain.draw(gc));
    }

    private void drawPlain(GraphicsContext gc) {
        gc.setStroke(Color.FIREBRICK);
        gc.setLineWidth(PLAIN_WIDTH);
        gc.strokeLine(0, plainHeight, width, plainHeight);
    }

    enum Mode { EMPTY, DEFAULT }
}
