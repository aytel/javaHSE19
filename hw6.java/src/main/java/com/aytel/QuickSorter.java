package com.aytel;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class QuickSorter {

    private static final Random random = new Random();
    private static final int SIZE_OF_SMALL = 70;

    public static <T extends Comparable<? super T>> void sort(List<T> list) throws ExecutionException, InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Future future = exec.submit(new SortSegmentTask<>(exec, list, 0, list.size()));
        future.get();
    }

    private static class SortSegmentTask<T extends Comparable<? super T>> implements Runnable {
        private final int l, r;

        @NotNull private final List<T> list;

        @NotNull private final ExecutorService exec;

        SortSegmentTask(@NotNull ExecutorService exec, @NotNull List<T> list, int l, int r) {
            this.exec = exec;
            this.list = list;
            this.l = l;
            this.r = r;
        }

        @Override
        public void run() {
            if (r - l < SIZE_OF_SMALL) {
                insertionSort();
            } else {
                int pivotPosition = random.nextInt(r - l) + l;
                T pivot = list.get(pivotPosition);
                Collections.swap(list, l, pivotPosition);

                int begin = l + 1, end = r - 1;
                while (begin != end) {
                    if (list.get(begin).compareTo(pivot) < 0) {
                        begin++;
                    } else {
                        while (begin != end && pivot.compareTo(list.get(end)) < 0) {
                            end--;
                        }
                        Collections.swap(list, begin, end);
                    }
                }

                //begin--;
                Collections.swap(list, l, begin);

                try {
                    exec.submit(new SortSegmentTask<>(exec, list, l, begin + 1)).get();
                    exec.submit(new SortSegmentTask<>(exec, list, end, r)).get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        private void insertionSort() {
            for (int i = l + 1; i < r; i++) {
                for (int j = i - 1; j >= l && list.get(j).compareTo(list.get(j + 1)) > 0; j--) {
                    Collections.swap(list, j, j + 1);
                }
            }
        }
    }
}
