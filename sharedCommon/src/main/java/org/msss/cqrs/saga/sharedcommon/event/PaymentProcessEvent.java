package org.msss.cqrs.saga.sharedcommon.event;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class PaymentProcessEvent {

    private final String paymentId;
    private final String orderId;

}
