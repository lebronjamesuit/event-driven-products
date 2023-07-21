package com.org.msss.cqrs.saga.ordersservice.management;

import com.org.msss.cqrs.saga.ordersservice.event.api.OrderCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.distributed.CommandDispatchException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.msss.cqrs.saga.sharedcommon.command.ProcessPaymentCommand;
import org.msss.cqrs.saga.sharedcommon.command.ReserveProductCommand;
import org.msss.cqrs.saga.sharedcommon.event.ProductReserveEvent;
import org.msss.cqrs.saga.sharedcommon.payment.UserPayment;
import org.msss.cqrs.saga.sharedcommon.query.FetchUserPaymentDetailsQuery;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Saga
public class OrderSaga {

    @Autowired
    private transient  CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

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

        FetchUserPaymentDetailsQuery queryUserPayment =
                new FetchUserPaymentDetailsQuery();
        queryUserPayment.setUserId(productReserveEvent.getUserId());

        UserPayment userPayment = null;
        try{
            userPayment = queryGateway.query(queryUserPayment, ResponseTypes.instanceOf(UserPayment.class)).join();
        }
        catch(Exception e){
            log.error("Start a compensation transaction");
            return ;
        }

       if(userPayment == null){
           log.error("Start a compensation transaction");
           return ;
       }

       log.info("DONE Payment process ok" + userPayment.getFirstName() + userPayment.getPaymentDetails());

        ProcessPaymentCommand processPaymentCommand =
                ProcessPaymentCommand.builder()
                        .paymentId(UUID.randomUUID().toString())
                        .orderId(userPayment.getUserId())
                        .paymentDetails(userPayment.getPaymentDetails())
                        .build();

        String result = null;
        try {
             result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
        }catch (CommandExecutionException e){
            log.error(e.getMessage());
            // Compensation here
            log.info("need to compensation here");
        }

        if(result == null){
            log.info("need to compensation here 2");
        }


    }

}
