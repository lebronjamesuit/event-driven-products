package org.msss.cqrs.saga.sharedcommon.event;


import lombok.Value;


@Value
public class PaymentProcessEvent {

    private final String orderId;
    private final String paymentId;


}
