package com.goody.utils.xiaobai.model.opennsfw;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * openNsfw practice
 *
 * @author Goody
 * @version 1.0, 2023/3/27
 * @since 1.0.0
 */
public class OpenNsfw {

    private static final NsfwManager NSFW_MANAGER = new NsfwManager();

    private static void predict(String path, Executor executor, CountDownLatch cdl) {
        executor.execute(() -> {
            InputStream is = OpenNsfw.class.getClassLoader().getResourceAsStream(path);
            try {
                NSFW_MANAGER.getPrediction(IOUtils.toByteArray(is));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                cdl.countDown();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i < 17; i++) {
            System.out.println("---------------------" + i + " start----------------------------");
            final int count = i * 5;
            final ThreadPoolExecutor executor = new ThreadPoolExecutor(i, i, 60000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(count));
            final CountDownLatch cdl = new CountDownLatch(count);
            for (int j = 0; j < count; j++) {
                predict("sfw-pic.jpg", executor, cdl);
            }
            cdl.await();
            System.out.println("---------------------" + i + " end----------------------------");
        }
    }
}
