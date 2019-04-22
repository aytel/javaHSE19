package com.aytel;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import xyz.morphia.annotations.*;


/**
 * Simple class, containing name of owner and his phone phoneNumber.
 * Both name and phoneNumber must be not null.
 */
@Entity
public class Ownership {
    @Id private ObjectId id;
    @Indexed @NotNull String name, phoneNumber;

    final static String NAME = "name";
    final static String PHONE_NUMBER = "phoneNumber";

    /** Morphia needs default constructor. */
    private Ownership() {
        this.name = "John Cena";
        this.phoneNumber = "88005553535";
    }

    Ownership(@NotNull String name, @NotNull String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "(" + name + ": " + phoneNumber + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Ownership) {
            Ownership ownership = (Ownership) other;
            return name.equals(ownership.name) && phoneNumber.equals(ownership.phoneNumber);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode() ^ phoneNumber.hashCode();
    }
}
