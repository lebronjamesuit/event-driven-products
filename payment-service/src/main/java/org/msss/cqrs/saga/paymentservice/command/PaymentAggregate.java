package org.msss.cqrs.saga.paymentservice.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.msss.cqrs.saga.sharedcommon.command.ProcessPaymentCommand;
import org.msss.cqrs.saga.sharedcommon.event.PaymentProcessEvent;
import org.msss.cqrs.saga.sharedcommon.payment.PaymentDetails;
import org.springframework.beans.BeanUtils;

@Aggregate
public class PaymentAggregate {

    @AggregateIdentifier
    private  String orderId;
    private  String paymentId;

    // Constructors
    public PaymentAggregate(){};

    @CommandHandler
    public void commandHandlingPayment(ProcessPaymentCommand ppc){
       if(ppc.getPaymentId().isEmpty() || ppc.getOrderId().isEmpty()){
           throw new IllegalArgumentException("Wrong ProcessPaymentCommand");
       }
        PaymentProcessEvent event =
               new PaymentProcessEvent(ppc.getPaymentId(), ppc.getOrderId());
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void eventSourcingPayment(PaymentProcessEvent event){
       this.orderId = event.getOrderId();
       this.paymentId = event.getPaymentId();
    }








}
