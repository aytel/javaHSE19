package com.aytel;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class QuickSorterTest {

    private <T extends Comparable<? super T>>boolean isSorted(List<T> list) {
        for (int i = 0; i + 1 < list.size(); i++) {
            if (list.get(i).compareTo(list.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    @Test
    void sort() throws InterruptedException, ExecutionException {
        var list = Arrays.asList(0, 5, 3, -2, 10);
        QuickSorter.sort(list);
        assertTrue(isSorted(list));
        list = (new Random()).ints((int)1e5).boxed().collect(Collectors.toList());
        QuickSorter.sort(list);
        assertTrue(isSorted(list));
    }

    @Test
    void testTime() {
        var firstList = (new Random()).ints((int)1e6).boxed().collect(Collectors.toList());
        var secondList = new ArrayList<>(firstList);
        System.out.println(countSortTime(() -> {
            try {
                QuickSorter.sort(firstList);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }));
        System.out.println(countSortTime(() -> secondList.sort(Comparator.naturalOrder())));
    }

    private long countSortTime(Runnable runnable) {
        long beginTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();
        return endTime - beginTime;
    }

}