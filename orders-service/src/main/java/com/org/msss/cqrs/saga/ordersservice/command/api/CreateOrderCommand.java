package com.org.msss.cqrs.saga.ordersservice.command.api;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;


@Builder
public class CreateOrderCommand {

    @TargetAggregateIdentifier
    private final String orderId;
    private final String userId;
    private final String productId;
    private final int quantity;
    private final String addressId;
    private final OrderStatus orderStatus;

}
