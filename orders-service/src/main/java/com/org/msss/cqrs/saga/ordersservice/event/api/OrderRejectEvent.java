package com.org.msss.cqrs.saga.ordersservice.event.api;

import com.org.msss.cqrs.saga.ordersservice.command.api.OrderStatus;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class OrderRejectEvent {

    private final String orderId;
    private final String reason;

    private final OrderStatus orderStatus = OrderStatus.REJECTED;

}
