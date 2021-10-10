package com.u4.completablefuture;

import com.u4.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.u4.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureHelloWorldTest {
    HelloWorldService helloWorldService = new HelloWorldService();
    CompletableFutureHelloWorld completableFutureHelloWorld = new CompletableFutureHelloWorld(helloWorldService);

    @Test
    void helloWorld() {
        CompletableFuture<String> completableFuture = completableFutureHelloWorld.helloWorld();
        completableFuture
                .thenAccept(s -> assertEquals("HELLO WORLD", s))
                .join(); // necessary to execute the lambda
        // .join() blocks the Test worker thread and let the assertion pass
    }

    @Test
    void helloWorldMultipleAsyncCalls() {
        String helloWorld = completableFutureHelloWorld.helloWorldMultipleAsyncCallsApproach();
        assertEquals("HELLO WORLD!", helloWorld);
    }

    @Test
    void helloWorldThreeAsyncCalls() {
        String helloWorld = completableFutureHelloWorld.helloWorldThreeAsyncCallsApproach();
        assertEquals("HELLO WORLD! THIRD COMPLETABLEFUTURE", helloWorld);
    }

    @Test
    void helloWorldThenCompose() {
        stopWatchReset();
        startTimer();
        CompletableFuture<String> completableFuture = completableFutureHelloWorld.helloWorldThenCompose();
        completableFuture.thenAccept(s -> assertEquals("HELLO WORLD!", s)).join();
        timeTaken();
    }
}