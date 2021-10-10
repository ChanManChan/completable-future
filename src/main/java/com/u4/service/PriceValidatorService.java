package com.u4.service;

import com.u4.domain.checkout.CartItem;

import static com.u4.util.CommonUtil.delay;
import static com.u4.util.LoggerUtil.log;

public class PriceValidatorService {

    public boolean isCartItemInvalid(CartItem cartItem) {
        int cartId = cartItem.getItemId();
        log("isCartItemInvalid: " + cartItem);
        delay(500);
        if (cartId == 7 || cartId == 9 || cartId == 11) {
            return true;
        }
        return false;
    }
}
