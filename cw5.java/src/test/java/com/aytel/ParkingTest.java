package com.aytel;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ParkingTest {
    @Test
    void testSingleThread() {
        var parking = new Parking(100);
        for (int i = 0; i < 200; i++) {
            assertEquals(i < 100, parking.enter());
        }
        for (int i = 0; i < 200; i++) {
            assertEquals(i < 100, parking.leave());
        }
    }

    @Test
    void testOnlyIn() {
        var parking = new Parking(100);
        var in = new ArrayList<Thread>();

        Integer[] success = new Integer[1];
        success[0] = 0;

        for (int i = 0; i < 200; i++) {
            in.add(new Thread(() -> {
                if (parking.enter()) {
                    synchronized (success) {
                        success[0]++;
                    }
                }
            }));
        }

        in.forEach(Thread::start);

        for (Thread thread : in) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {}
        }

        assertEquals(0, parking.free());

        assertEquals(100, success[0].intValue());
    }

    @Test
    void testBoth() {
        var parking = new Parking(100);

        var both = new ArrayList<Thread>();

        var random = new Random();

        for (int i = 0; i < 200; i++) {
            if (random.nextInt() % 2 == 0) {
                both.add(new Thread(() -> {
                    parking.enter();
                    int free = parking.free();
                    assertTrue(free >= 0 && free <= 100);
                }));
            } else {
                both.add(new Thread(() -> {
                    parking.leave();
                    int free = parking.free();
                    assertTrue(free >= 0 && free <= 100);
                }));
            }
        }

        both.forEach(Thread::start);

        for (Thread thread : both) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {}
        }
    }
}