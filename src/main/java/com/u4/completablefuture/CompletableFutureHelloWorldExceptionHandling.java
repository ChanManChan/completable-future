package com.u4.completablefuture;

import com.u4.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.u4.util.CommonUtil.*;
import static com.u4.util.CommonUtil.timeTaken;
import static com.u4.util.LoggerUtil.log;

public class CompletableFutureHelloWorldExceptionHandling {
    private final HelloWorldService helloWorldService;

    public CompletableFutureHelloWorldExceptionHandling(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    public String helloWorldThreeAsyncCallsHandleException() {
        stopWatchReset();
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> thirdCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Third CompletableFuture";
        });

        String helloWorld = hello
                .handle((res, e) -> { // handle() can catch and recover from exceptions
                    if (e != null) {
                        log("Exception occurred after hello invocation: " + e.getMessage());
                        return ""; // if any exception happens, we are going to provide this recoverable value
                    }
                    return res;
                }) // this handles the exceptions thrown from the hello method call.
                .thenCombine(world, (h, w) -> h + w) // if exception occurred then h + w = " world!"
                .handle((res, e) -> {
                    if (e != null) {
                        log("Exception occurred after world invocation: " + e.getMessage());
                        return "";
                    }
                    return res;
                }) // when the world call fails, handle the exception within this handle block
                .thenCombine(thirdCompletableFuture, (previous, current) -> previous + current) // if exception occurred then previous + current = " Third CompletableFuture"
                .thenApply(String::toUpperCase) // if exception occurred then String::toUpperCase = " THIRD COMPLETABLEFUTURE"
                .join();

        timeTaken();
        return helloWorld;
    }

    public String helloWorldThreeAsyncCallsHandleExceptionUsingExceptionally() {
        stopWatchReset();
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> thirdCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Third CompletableFuture";
        });

        String helloWorld = hello
                .exceptionally(e -> {
                    log("Exception occurred after hello invocation: " + e.getMessage());
                    return "";
                })
                .thenCombine(world, (h, w) -> h + w)
                .exceptionally(e -> {
                    log("Exception occurred after world invocation: " + e.getMessage());
                    return "";
                })
                .thenCombine(thirdCompletableFuture, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();
        return helloWorld;
    }

    public String helloWorldThreeAsyncCallsHandleExceptionUsingWhenComplete() {
        stopWatchReset();
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> thirdCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Third CompletableFuture";
        });

        String helloWorld = hello
                .whenComplete((res, e) -> { // if this whenComplete() is triggered then it will invoke the next handler in the
                    if (e != null) {        // pipeline which in this case in another whenComplete() and will skip all success paths.
                        log("Exception occurred after hello invocation: " + e.getMessage());
                    }
                })
                .thenCombine(world, (h, w) -> h + w)
                .whenComplete((res, e) -> {
                    if (e != null) {
                        log("Exception occurred after world invocation: " + e.getMessage());
                    }
                })
                .thenCombine(thirdCompletableFuture, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .exceptionally(e -> {
                    log("Exception occurred in the pipeline: " + e.getMessage());
                    return "";
                })
                .join();

        timeTaken();
        return helloWorld;
    }
}
