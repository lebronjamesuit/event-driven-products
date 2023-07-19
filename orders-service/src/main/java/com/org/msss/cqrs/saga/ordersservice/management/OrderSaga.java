package com.org.msss.cqrs.saga.ordersservice.management;

import com.netflix.discovery.converters.Auto;
import com.org.msss.cqrs.saga.ordersservice.event.api.OrderCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.Message;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.msss.cqrs.saga.sharedcommon.command.ReserveProductCommand;
import org.msss.cqrs.saga.sharedcommon.event.ProductReserveEvent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

@Slf4j
@Saga
public class OrderSaga {

    @Autowired
    private transient  CommandGateway commandGateway;

   // CreateOrderCommand

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void orderCreateEvent(OrderCreateEvent orderCreateEvent) {
        // After placed order ok, roi sao nua?
        // Reserve product -> update quantity in Product Aggregate
        ReserveProductCommand rpc = ReserveProductCommand.builder()
                                    .productId(orderCreateEvent.getProductId())
                                    .quantity(orderCreateEvent.getQuantity())
                                    .orderId(orderCreateEvent.getOrderId())
                                    .userId(orderCreateEvent.getUserId())
                                    .build();

         commandGateway.send(rpc, new CommandCallback<ReserveProductCommand, Object>() {
             @Override
            public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage,
                                 CommandResultMessage<? extends Object> commandResultMessage) {
                if(commandResultMessage.isExceptional()){
                    log.info("HEY, When ReserveProductCommand is done, report to the callback here");
                }
            }
       });

    }

    // Share commom class.
    @SagaEventHandler(associationProperty = "orderId")
    public void orderCreateEvent(ProductReserveEvent productReserveEvent) {
        // Roi sao nua?
        log.info("  // Roi sao nua?");
        log.info("pro ID " + productReserveEvent.getProductId() );
        log.info("order id " + productReserveEvent.getOrderId() );
        log.info(" Quantity " + productReserveEvent.getQuantity());
    }


}
