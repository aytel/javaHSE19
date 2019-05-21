package com.aytel;

import javafx.scene.canvas.GraphicsContext;

public interface Sprite {
    void draw(GraphicsContext gc);

    default void update(long delta) {}
}
