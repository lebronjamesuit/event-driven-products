package com.org.msss.cqrs.saga.ordersservice.event.api;


import com.org.msss.cqrs.saga.ordersservice.command.api.OrderStatus;
import lombok.Data;

@Data
public class OrderCreateEvent {

    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;
}
