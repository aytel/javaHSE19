package com.aytel;

import javafx.scene.canvas.GraphicsContext;

/** Interface of any drawable object in the game. */
public interface Sprite {
    /** Draws this sprite on the given graphicContext. */
    void draw(GraphicsContext gc);

    /** Updates current position or state of the sprite if past time since last update is delta. */
    default void update(long delta) {}
}
