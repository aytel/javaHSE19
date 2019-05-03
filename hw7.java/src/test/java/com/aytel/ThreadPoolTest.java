package com.aytel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.*;

public class ThreadPoolTest {
    private final Supplier<Integer> simpleSupplier = () -> {
      int current = 0;
      var random = new Random();
      for (int i = 0; i < 1000; i++) {
          current ^= random.nextInt();
      }
      return current;
    };

    void withSimpleSupplier(int numberOfThreads, int numberOfTasks) {
        var threadPool = new ThreadPool(numberOfThreads);
        List<LightFuture<Integer>> lightFutures = new ArrayList<>();
        IntStream.range(0, numberOfTasks).forEach((i) -> lightFutures.add(threadPool.add(simpleSupplier)));
        lightFutures.forEach((lightFuture) -> assertTimeout(Duration.ofSeconds(1), lightFuture::get));
    }

    @Test
    void moreThreads() {
        withSimpleSupplier(1000, 100);
    }

    @Test
    void moreTasks() {
        withSimpleSupplier(10, 10000);
    }

    @Test
    void equallyThreadsAndTasks() {
        withSimpleSupplier(1000, 1000);
    }

    @Test
    void testShutdown() {
        var threadPool = new ThreadPool(1);
        LightFuture<Integer> lightFuture = threadPool.add(() -> {
            var random = new Random();
            int current = 0;
            for (int i = 0; i < 1e8; i++) {
                int temp = random.nextInt();
                current ^= temp;
            }
            return current;
        });
        threadPool.shutdown();
        assertThrows(IllegalStateException.class, () -> threadPool.add(() -> 0));
    }

    @Test
    void testHowManyThreads() {
        int startCount = Thread.activeCount();
        var random = new Random();
        int howManyNow = abs(random.nextInt()) % 200;
        var threadPool = new ThreadPool(howManyNow);
        assertEquals(startCount + howManyNow, Thread.activeCount());

    }
}