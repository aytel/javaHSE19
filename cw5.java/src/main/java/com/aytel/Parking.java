package com.aytel;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.UnaryOperator;

/** Class which emulates parking where cars can go in and out. */
public class Parking {
    volatile private Integer free;
    private Integer capacity;
    private static final AtomicReferenceFieldUpdater<Parking, Integer> updater =
            AtomicReferenceFieldUpdater.newUpdater(Parking.class, Integer.class, "free");

    private static final UnaryOperator<Integer> enterUpdate = (x) -> (x == 0 ? 0 : x - 1);
    private final UnaryOperator<Integer> leaveUpdate = (x) -> (x.equals(capacity) ? capacity : x + 1);

    /** Creates parking with given capacity. */
    public Parking(int capacity) {
        this.capacity = this.free = capacity;
    }

    /** Tries to park a car. Returns true if succeeded and false otherwise. */
    public boolean enter() {
        return updater.getAndUpdate(this, enterUpdate) > 0;
    }\

    /** Tries to unpark a car. Returns true if succeeded and false otherwise. */
    public boolean leave() {
        return updater.getAndUpdate(this, leaveUpdate) < capacity;
    }

    /** Returns number of free parking  places. */
    int free() {
        return free;
    }
}
