package com.aytel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/** Hasher with ForkJoinPool which helps to calculate hash faster. */
public class ForkJoinMD5Hasher implements MD5Hasher {
    private ForkJoinPool exec = new ForkJoinPool();

    @Override
    public MessageDigest hash(@NotNull File file) throws IOException {
        return exec.invoke(new FileHasher(file));
    }

    private class FileHasher extends RecursiveTask<MessageDigest> {
        private final File file;

        FileHasher(File file) {
            this.file = file;
        }

        @Override
        protected MessageDigest compute() {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                List<FileHasher> tasks = new LinkedList<>();
                if (file.isDirectory()) {
                    md.update(file.getName().getBytes());
                    for (var child: file.listFiles()) {
                        FileHasher task = new FileHasher(child);
                        task.fork();
                        tasks.add(task);
                    }
                    for (FileHasher task: tasks) {
                        md.update(task.join().digest());
                    }
                } else {
                    MessageDigestWithFileUpdater.update(md, file);
                }
                return md;
            } catch (NoSuchAlgorithmException ignored) {
                throw new RuntimeException(); // This can't happen because algo is always MD5.
            } catch (IOException e) {
                throw new RuntimeException(e); // I can't throw another exception from here due to signature.
            }
        }
    }
}
