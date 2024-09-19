package com.goody.utils.jinjunmei.controller;

import com.alibaba.nacos.common.utils.ThreadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * async interface controller
 *
 * @author Goody
 * @version 1.0, 2024/9/19
 */
@RestController
@RequestMapping("/goody")
@RequiredArgsConstructor
@Slf4j
public class GoodyAsyncController {

    private static final AtomicInteger COUNT = new AtomicInteger(0);
    private static final Executor EXECUTOR = new ThreadPoolExecutor(
        10,
        10,
        10,
        TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(10),
        r -> new Thread(r, String.format("customer-t-%s", COUNT.addAndGet(1)))
    );

    @GetMapping("async/query1")
    public CompletionStage<String> asyncQuery1() {
        log.info("async query start");
        return CompletableFuture.supplyAsync(() -> {
            log.info("async query sleep start");
            ThreadUtils.sleep(10000);
            log.info("async query sleep done");
            log.info("async query done");
            return "done";
        }, EXECUTOR);
    }

    @GetMapping("sync/query1")
    public String syncQuery1() throws InterruptedException {
        log.info("sync query start");
        final CountDownLatch latch = new CountDownLatch(1);
        EXECUTOR.execute(() -> {
            log.info("sync query sleep start");
            ThreadUtils.sleep(1000);
            log.info("sync query sleep done");
            latch.countDown();
        });
        latch.await();
        log.info("sync query done");
        return "done";
    }
}
