package com.u4.completablefuture;

import com.u4.service.HelloWorldService;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        String helloWorld = hello
                .thenCombine(world, (h, w) -> h + w)
                .thenCombine(thirdCompletableFuture, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();
        return helloWorld;
    }

    public String helloWorldThreeAsyncCallsApproachCustomThreadPool() {
        stopWatchReset();
        startTimer();

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); // so that it won't use the Common-ForkJoinPool instead

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello, executorService);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world, executorService);
        CompletableFuture<String> thirdCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Third CompletableFuture";
        }, executorService);

        String helloWorld = hello
                .thenCombine(world, (h, w) -> {
                    log("thenCombine h/w"); // these loggers will tell you which thread pool this particular code is getting executed.
                    return h + w;
                })
                .thenCombine(thirdCompletableFuture, (previous, current) -> {
                    log("thenCombine previous/current");
                    return previous + current;
                })
                .thenApply(s -> {
                    log("thenApply");
                    return s.toUpperCase(Locale.ROOT);
                })
                .join();

        timeTaken();
        return helloWorld;
    }

    public String helloWorldThreeAsyncCallsApproachAsync() {
        stopWatchReset();
        startTimer();

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello, executorService);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world, executorService);
        CompletableFuture<String> thirdCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Third CompletableFuture";
        }, executorService);

        String helloWorld = hello
                .thenCombineAsync(world, (h, w) -> {
                    log("thenCombine h/w"); // these loggers will tell you which thread pool this particular code is getting executed.
                    return h + w;
                }, executorService)
                .thenCombineAsync(thirdCompletableFuture, (previous, current) -> {
                    log("thenCombine previous/current");
                    return previous + current;
                }, executorService)
                .thenApplyAsync(s -> {
                    log("thenApply");
                    return s.toUpperCase(Locale.ROOT);
                }, executorService)
                .join();

        //        [pool-1-thread-1] - inside hello <- behaviour of Async will give the same result but the underlying
        //        [pool-1-thread-2] - inside world    // execution is going to be little different because it's going to involve different threads from the provided custom thread pool
        //        [pool-1-thread-4] - thenCombine h/w
        //        [pool-1-thread-5] - thenCombine previous/current
        //        [pool-1-thread-6] - thenApply

        timeTaken();
        return helloWorld;
    }

    public CompletableFuture<String> helloWorldThenCompose() {
        return CompletableFuture.supplyAsync(helloWorldService::hello)
                .thenCompose(previous -> helloWorldService.worldFuture(previous)) // dependent task, therefore waits for the output from the first operation 'hello function'
                .thenApply(String::toUpperCase);
    }

    public String completableFutureAnyOf() {
        stopWatchReset();
        startTimer();
        // make these three calls in parallel but take the response from the call that responds the fastest
        String HELLO_WORLD = "hello world";
        // db call
        CompletableFuture<String> responseFromDb = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            log("response from db");
            return HELLO_WORLD;
        });

        // rest call
        CompletableFuture<String> responseFromRest = CompletableFuture.supplyAsync(() -> {
            delay(2000);
            log("response from rest call");
            return HELLO_WORLD;
        });

        // soap call
        CompletableFuture<String> responseFromSoap = CompletableFuture.supplyAsync(() -> {
            delay(3000);
            log("response from soap call");
            return HELLO_WORLD;
        });

        List<CompletableFuture<String>> completableFutureList = List.of(responseFromDb, responseFromRest, responseFromSoap);
        CompletableFuture<Object> completableFutureAnyOf = CompletableFuture.anyOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        String result = (String) completableFutureAnyOf.thenApply(o -> {
                    if (o instanceof String) {
                        return o;
                    }
                    return null;
                })
                .join();
        timeTaken();
        return result;
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
