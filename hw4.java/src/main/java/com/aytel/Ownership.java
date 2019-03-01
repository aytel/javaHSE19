package com.aytel;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import xyz.morphia.annotations.*;


/** Simple class, containing name of owner and his phone number.
 * Both name and number must be not null.
 */
@Entity
public class Ownership {
    @Id private ObjectId id;
    @Indexed @NotNull String name, number;

    private Ownership() {
        this.name = "John Cena";
        this.number = "88005553535";
    }

    Ownership(@NotNull String name, @NotNull String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return "(" + name + ": " + number + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Ownership) {
            Ownership ownership = (Ownership) other;
            return name.equals(ownership.name) && number.equals(ownership.number);
        }
        return false;
    }
}
