package com.spring.foodapi.service;

import com.spring.foodapi.io.CartRequest;
import com.spring.foodapi.io.CartResponse;

public interface CartService {
    CartResponse addToCart(CartRequest request);

    CartResponse getCart();

    void clearCart();

    CartResponse removeFromCart(CartRequest cartRequest);
}
