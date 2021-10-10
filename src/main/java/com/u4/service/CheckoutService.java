package com.u4.service;

import com.u4.domain.checkout.Cart;
import com.u4.domain.checkout.CartItem;
import com.u4.domain.checkout.CheckoutResponse;
import com.u4.domain.checkout.CheckoutStatus;

import java.util.List;
import java.util.stream.Collectors;

import static com.u4.util.CommonUtil.startTimer;
import static com.u4.util.CommonUtil.timeTaken;
import static com.u4.util.LoggerUtil.log;

public class CheckoutService {

    private final PriceValidatorService priceValidatorService;

    public CheckoutService(PriceValidatorService priceValidatorService) {
        this.priceValidatorService = priceValidatorService;
    }

    public CheckoutResponse checkout(Cart cart) {
        startTimer();
        List<CartItem> priceValidationList = cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> {
                    boolean isPriceInvalid = priceValidatorService.isCartItemInvalid(cartItem);
                    cartItem.setExpired(isPriceInvalid);
                    return cartItem;
                })
                .filter(CartItem::isExpired)
                .collect(Collectors.toList());

        timeTaken();
        if (!priceValidationList.isEmpty()) { // at least one is expired then return FAILURE
            return new CheckoutResponse(CheckoutStatus.FAILURE, priceValidationList);
        }

//        double finalPrice = calculateFinalPrice(cart);
        double finalPrice = calculateFinalPriceReduce(cart);
        log("Checkout complete and the final price is: " + finalPrice);
        return new CheckoutResponse(CheckoutStatus.SUCCESS, finalPrice);
    }

    private double calculateFinalPrice(Cart cart) {
        return cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> cartItem.getQuantity() * cartItem.getRate())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private double calculateFinalPriceReduce(Cart cart) {
        return cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> cartItem.getQuantity() * cartItem.getRate())
                .reduce(0.0, Double::sum);
    }
}
