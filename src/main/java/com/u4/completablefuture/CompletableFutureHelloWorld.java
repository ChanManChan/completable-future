package com.u4.completablefuture;

import com.u4.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.u4.util.CommonUtil.*;
import static com.u4.util.LoggerUtil.log;

public class CompletableFutureHelloWorld {
    private final HelloWorldService helloWorldService;

    public CompletableFutureHelloWorld(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    public CompletableFuture<String> helloWorld() {
        return CompletableFuture.supplyAsync(helloWorldService::helloWorld) // runs this in a common fork-join pool
                .thenApply(String::toUpperCase);
        //.thenAccept(result -> log("Result is: " + result)) // .thenAccept() consumes the value
        //.join(); //blocks the caller thread (main thread) until the whole computation is completed by the ForkJoinPool thread.
        // don't use .join() if main thread has to be released immediately
        // .join() it gets the string out of the CompletableFuture
    }

    public String helloWorldSequentialCallsApproach() {
        String hello = helloWorldService.hello();
        String world = helloWorldService.world();
        return hello + world;
    }

    public String helloWorldMultipleAsyncCallsApproach() {
        stopWatchReset();
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world);
        String helloWorld = hello.
                thenCombine(world, (h, w) -> h + w)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();
        return helloWorld;
    }

    public String helloWorldThreeAsyncCallsApproach() {
        stopWatchReset();
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> thirdCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Third CompletableFuture";
        });

        String helloWorld = hello.
                thenCombine(world, (h, w) -> h + w)
                .thenCombine(thirdCompletableFuture, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();
        return helloWorld;
    }

    public CompletableFuture<String> helloWorldThenCompose() {
        return CompletableFuture.supplyAsync(helloWorldService::hello)
                .thenCompose(previous -> helloWorldService.worldFuture(previous)) // dependent task, therefore waits for the output from the first operation 'hello function'
                .thenApply(String::toUpperCase);
    }

    public static void main(String[] args) {
        HelloWorldService helloWorldService = new HelloWorldService();

        CompletableFuture.supplyAsync(helloWorldService::helloWorld)
                .thenApply(String::toUpperCase)
                .thenAccept(result -> log("Result is: " + result))
                .join();
        log("Done");
        // without adding delay (use 'join' chainable method above)
        // delay(2000); // had to add this delay so as to not terminate the main thread immediately,
        // delay of 2s will ensure the printing of "Result is: hello world" which will be executed after a 1000ms
        // delay by the ForkJoinPool threads
    }
}
