package com.aytel;

import javafx.scene.canvas.GraphicsContext;

import java.util.stream.IntStream;

/** Container for aims. */
public class Aims implements Sprite {
    private final int DEFAULT_COUNT = 3;

    private final Land land;

    final IntrusiveList<Aim> aims = new IntrusiveList<>();

    /** Creates aims in random places according to chosen mode. */
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

    /** Returns true if there's no aims left and false otherwise. */
    boolean finished() {
        return !aims.iterator().hasNext();
    }

    /** Mode of creating aims. ONE - creates one aim, DEFAULT - three aims. */
    enum Mode { ONE, DEFAULT }
}
