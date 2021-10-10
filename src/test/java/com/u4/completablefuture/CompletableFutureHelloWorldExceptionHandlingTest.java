package com.u4.completablefuture;

import com.u4.service.HelloWorldService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompletableFutureHelloWorldExceptionHandlingTest {
    @Mock
    private HelloWorldService helloWorldService;

    @InjectMocks
    private CompletableFutureHelloWorldExceptionHandling completableFutureHelloWorldExceptionHandling;

    @Test
    void helloWorldThreeAsyncCallsApproachHandleOneException() {
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception Occurred"));
        when(helloWorldService.world()).thenCallRealMethod();

        String result = completableFutureHelloWorldExceptionHandling.helloWorldThreeAsyncCallsHandleException();
        assertEquals(" WORLD! THIRD COMPLETABLEFUTURE", result);
    }

    @Test
    void helloWorldThreeAsyncCallsApproachHandleTwoExceptions() {
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception Occurred"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception Occurred"));

        String result = completableFutureHelloWorldExceptionHandling.helloWorldThreeAsyncCallsHandleException();
        assertEquals(" THIRD COMPLETABLEFUTURE", result);
    }

    @Test
    void helloWorldThreeAsyncCallsApproachHappyPath() {
        when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        String result = completableFutureHelloWorldExceptionHandling.helloWorldThreeAsyncCallsHandleException();
        assertEquals("HELLO WORLD! THIRD COMPLETABLEFUTURE", result);
    }

    @Test
    void helloWorldThreeAsyncCallsExceptionHandlingUsingExceptionallyHappyPath() {
        when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        String result = completableFutureHelloWorldExceptionHandling.helloWorldThreeAsyncCallsHandleExceptionUsingExceptionally();
        assertEquals("HELLO WORLD! THIRD COMPLETABLEFUTURE", result);
    }

    @Test
    void helloWorldThreeAsyncCallsExceptionHandlingUsingExceptionally() {
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception Occurred"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception Occurred"));

        String result = completableFutureHelloWorldExceptionHandling.helloWorldThreeAsyncCallsHandleExceptionUsingExceptionally();
        assertEquals(" THIRD COMPLETABLEFUTURE", result);
    }

    @Test
    void helloWorldThreeAsyncCallsExceptionHandlingUsingWhenCompleteHappyPath() {
        when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        String result = completableFutureHelloWorldExceptionHandling.helloWorldThreeAsyncCallsHandleExceptionUsingWhenComplete();
        assertEquals("HELLO WORLD! THIRD COMPLETABLEFUTURE", result);
    }

    @Test
    void helloWorldThreeAsyncCallsExceptionHandlingUsingWhenComplete() {
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception Occurred"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception Occurred"));

        String result = completableFutureHelloWorldExceptionHandling.helloWorldThreeAsyncCallsHandleExceptionUsingWhenComplete();
        assertEquals("", result);
    }
}