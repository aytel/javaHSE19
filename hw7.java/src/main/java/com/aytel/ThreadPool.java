package com.aytel;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;

public class ThreadPool {
    private boolean isShutdown = false;
    private final Thread[] threads;
    private final Queue<@NotNull Task> tasks = new ArrayDeque<>();
    private final Runnable executeTasks = () -> {
        while (!Thread.interrupted()) {
            try {
                Task task = null;
                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        tasks.wait();
                    }
                    task = tasks.poll();
                }
                task.run();
            } catch (InterruptedException ignored) { break; }
        }
    };

    public ThreadPool(int threadsNumber) {
        checkArgument(threadsNumber > 0);
        threads = new Thread[threadsNumber];
        Arrays.setAll(threads, i -> new Thread(executeTasks));
        Arrays.stream(threads).forEach(Thread::start);
    }

    public <T> LightFuture<T> add(@NotNull Supplier<T> supplier) {
        if (this.isShutdown) {
            throw new IllegalStateException("Threadpool is shutdown already.");
        }

        Task<T> task = new Task<>(supplier);
        synchronized (tasks) {
            tasks.add(task);
            tasks.notifyAll();
        }
        return task;
    }

    public synchronized void shutdown() {
        this.isShutdown = true;
        Arrays.stream(this.threads).forEach(Thread::interrupt);
    }

    private class Task<T> implements LightFuture<T>, Runnable {
        private T result = null;
        private LightExecutionException exception = null;
        private boolean ready = false;
        private final Supplier<T> supplier;
        private final List<Task> waiting = new ArrayList<>();

        Task(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        <K> Task(Task<K> task, Function<? super K, T> function) {
            this.supplier = () -> {
                try {
                    return function.apply(task.get());
                } catch (LightExecutionException e) {
                    Task.this.exception = e;
                } catch (InterruptedException e) {
                    Task.this.exception = new LightExecutionException(e);
                }
                return null; //In this case exception is not null so Task::get will throw it and never will return smth.
            };
        }


        @Override
        public synchronized boolean isReady() {
            return ready;
        }

        @Override
        public synchronized T get() throws LightExecutionException, InterruptedException {
            while (!this.ready) {
                this.wait();
            }

            if (exception != null) {
                throw exception;
            }

            return result;
        }

        @Override
        public synchronized <K> LightFuture<K> thenApply(@NotNull Function<? super T, K> function) {
            var task = new Task<>(this, function);
            if (this.ready) {
                synchronized (tasks) {
                    tasks.add(task);
                    tasks.notifyAll();
                }
            } else {
                waiting.add(task);
            }
            return task;
        }

        @Override
        public synchronized void run() {
            try {
                assert supplier != null;
                this.result = supplier.get();
            } catch (Exception e) {
                this.exception = new LightExecutionException(e);
            }
            this.ready = true;
            synchronized (tasks) {
                tasks.addAll(waiting);
                tasks.notifyAll();
            }
            this.notifyAll();
        }
    }
}
