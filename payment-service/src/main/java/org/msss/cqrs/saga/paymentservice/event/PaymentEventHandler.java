package org.msss.cqrs.saga.paymentservice.event;

import org.axonframework.eventhandling.EventHandler;
import org.msss.cqrs.saga.sharedcommon.event.PaymentProcessEvent;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventHandler {

    @EventHandler
    public void eventHandlePayment(PaymentProcessEvent event){

    }
}
