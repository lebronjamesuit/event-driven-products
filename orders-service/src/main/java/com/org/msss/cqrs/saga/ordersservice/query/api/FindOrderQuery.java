package com.org.msss.cqrs.saga.ordersservice.query.api;

import com.org.msss.cqrs.saga.ordersservice.command.api.OrderStatus;
import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@RequiredArgsConstructor
@Value
public class FindOrderQuery {
    private final String orderId;
}
