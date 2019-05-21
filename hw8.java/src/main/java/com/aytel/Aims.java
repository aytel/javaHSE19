package com.aytel;

import javafx.scene.canvas.GraphicsContext;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class Aims implements Sprite {
    private final int DEFAULT_COUNT = 3;

    private final Land land;

    final IntrusiveList<Aim> aims = new IntrusiveList<>();

    Aims(Land land, Mode mode) {
        this.land = land;

        int count = DEFAULT_COUNT;

        switch (mode) {
            case ONE:
                count = 1;
                break;
        }

        IntStream.range(0, count).forEach(i -> aims.add(new Aim(land)));
    }

    @Override
    public void draw(GraphicsContext gc) {
        aims.forEach((aim) -> aim.draw(gc));
    }

    boolean finished() {
        return !aims.iterator().hasNext();
    }

    enum Mode { ONE, DEFAULT }
}
