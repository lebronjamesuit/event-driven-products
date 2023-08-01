package com.org.msss.cqrs.saga.ordersservice.query.api;

import com.org.msss.cqrs.saga.ordersservice.command.api.OrderStatus;
import lombok.Data;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class FindOrderQuery {
    private final String orderId;
}
