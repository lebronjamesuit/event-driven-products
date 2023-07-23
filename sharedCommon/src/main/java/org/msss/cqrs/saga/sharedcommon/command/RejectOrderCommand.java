package org.msss.cqrs.saga.sharedcommon.command;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@AllArgsConstructor
@Value
public class RejectOrderCommand {

    @TargetAggregateIdentifier
    private final String orderId;

    private final String reason;
}
