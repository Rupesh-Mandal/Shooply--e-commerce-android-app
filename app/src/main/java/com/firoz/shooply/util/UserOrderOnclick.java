package com.firoz.shooply.util;

import com.firoz.shooply.model.OrderModel;

public interface UserOrderOnclick {
    void onReceived(OrderModel orderModel);
    void onCancel(OrderModel orderModel);

}
