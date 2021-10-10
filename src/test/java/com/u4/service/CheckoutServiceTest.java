package com.u4.service;

import com.u4.domain.checkout.Cart;
import com.u4.domain.checkout.CheckoutResponse;
import com.u4.domain.checkout.CheckoutStatus;
import com.u4.util.DataSet;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {

    PriceValidatorService priceValidatorService = new PriceValidatorService();
    CheckoutService checkoutService = new CheckoutService(priceValidatorService);

    @Test
    void numberOfCores() {
        System.out.println("Number of cores: " + Runtime.getRuntime().availableProcessors());
    }

    @Test
    void parallelism() {
        // one of the thread that going to be part of the parallelism is the thread which actually invokes or initiates the whole computation.
        // default parallelism is number of cores - 1
        System.out.println("Parallelism: " + ForkJoinPool.getCommonPoolParallelism());
    }

    @Test
    void checkout() {
        Cart cart = DataSet.createCart(6);
        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);
        assertEquals(CheckoutStatus.SUCCESS, checkoutResponse.getCheckoutStatus());
        assertTrue(checkoutResponse.getFinalRate() > 0);
    }

    @Test
    void checkoutStress() {
        // parallel streams tip:- number of tasks run in parallel is equal to the number of cores
        Cart cart = DataSet.createCart(16);
        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);
        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
    }

    @Test
    void modifyParallelism() {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "100");
        Cart cart = DataSet.createCart(100);
        CheckoutResponse checkoutResponse = checkoutService.checkout(cart);
        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
    }
}