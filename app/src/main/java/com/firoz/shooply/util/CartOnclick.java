package com.firoz.shooply.util;

import com.firoz.shooply.model.CartModel;

public interface CartOnclick {
    void increaseCart(CartModel cartModel);
    void decreaseCart(CartModel cartModel);
    void deleteCart(CartModel cartModel);
}
