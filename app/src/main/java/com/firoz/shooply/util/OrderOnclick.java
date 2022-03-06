package com.firoz.shooply.util;


import com.firoz.shooply.model.OrderModel;

public interface OrderOnclick {
    void onDeliveryStarted(OrderModel orderModel);
    void onDeliverdFaild(OrderModel orderModel);
    void onCancel(OrderModel orderModel,String massege);
    void onAccept(OrderModel orderModel);

}
