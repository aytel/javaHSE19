package com.aytel;

import org.jetbrains.annotations.NotNull;
import xyz.morphia.annotations.*;

@Entity
public class Ownership {

    @Indexed @NotNull String name, number;

    private Ownership() {
        this.name = "John Cena";
        this.number = "88005553535";
    }

    public Ownership(@NotNull String name, @NotNull String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return "(" + name + ": " + number + ")";
    }
}
