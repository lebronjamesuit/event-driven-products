package com.org.msss.cqrs.saga.ordersservice.command.api;

import lombok.Data;

@Data
public class OrderRestModel {

    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;
}
