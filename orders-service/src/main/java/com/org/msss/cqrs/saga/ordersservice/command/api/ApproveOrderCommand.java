package com.org.msss.cqrs.saga.ordersservice.command.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@AllArgsConstructor
@Data
public class ApproveOrderCommand {

    @TargetAggregateIdentifier
    private final String orderId;
}

