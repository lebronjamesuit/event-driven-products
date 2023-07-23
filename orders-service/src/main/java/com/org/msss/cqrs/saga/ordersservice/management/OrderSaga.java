package com.org.msss.cqrs.saga.ordersservice.management;

import com.org.msss.cqrs.saga.ordersservice.command.api.ApproveOrderCommand;
import com.org.msss.cqrs.saga.ordersservice.event.api.OrderApproveEvent;
import com.org.msss.cqrs.saga.ordersservice.event.api.OrderCreateEvent;
import com.org.msss.cqrs.saga.ordersservice.event.api.OrderRejectEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.msss.cqrs.saga.sharedcommon.command.CancelProductReservationCommand;
import org.msss.cqrs.saga.sharedcommon.command.ProcessPaymentCommand;
import org.msss.cqrs.saga.sharedcommon.command.RejectOrderCommand;
import org.msss.cqrs.saga.sharedcommon.command.ReserveProductCommand;
import org.msss.cqrs.saga.sharedcommon.event.PaymentProcessEvent;
import org.msss.cqrs.saga.sharedcommon.event.ProductCancelReservationEvent;
import org.msss.cqrs.saga.sharedcommon.event.ProductReserveEvent;
import org.msss.cqrs.saga.sharedcommon.payment.UserPayment;
import org.msss.cqrs.saga.sharedcommon.query.FetchUserPaymentDetailsQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Saga
public class OrderSaga {

    private transient CommandGateway commandGateway;
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreateEvent orderCreateEvent) {
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
                if (commandResultMessage.isExceptional()) {
                    log.info("HEY, When ReserveProductCommand is done, report to the callback here");
                }
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReserveEvent productReserveEvent) {
        String orderId = productReserveEvent.getOrderId();
        FetchUserPaymentDetailsQuery queryUserPayment =
                new FetchUserPaymentDetailsQuery();
        queryUserPayment.setUserId(productReserveEvent.getUserId());
        UserPayment userPayment = null;
        try {
            userPayment = queryGateway.query(queryUserPayment, ResponseTypes.instanceOf(UserPayment.class)).join();
        } catch (Exception e) {
            cancelProductReservation(e.getMessage(), productReserveEvent);
            return;
        }
        if (userPayment == null) {
            cancelProductReservation("userPayment is null, cannot continue, so rollback the transaction", productReserveEvent);
            return;
        }

        ProcessPaymentCommand processPaymentCommand =
                ProcessPaymentCommand.builder()
                        .paymentId("payment" + UUID.randomUUID())
                        .orderId(orderId)
                        .paymentDetails(userPayment.getPaymentDetails())
                        .build();

        String result = null;
        try {
            result = commandGateway.sendAndWait(processPaymentCommand);
        } catch (Exception e) {
            log.error(e.getMessage());
            cancelProductReservation(e.getMessage(), productReserveEvent);
        }

        if (result == null) {
            cancelProductReservation("ProcessPaymentCommand is null, cannot continue, so rollback the transaction", productReserveEvent);
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessEvent paymentProcessEvent) {
        log.info("PaymentProcessEvent paymentProcessEvent");
        ApproveOrderCommand approveOrderCommand =
                new ApproveOrderCommand(paymentProcessEvent.getOrderId());

        commandGateway.send(approveOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApproveEvent orderApproveEvent) {
        log.info("Finished OrderApproveEvent");
    }

    // This is a new saga lifecycle
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductCancelReservationEvent event) {
        log.info("This is after ProductCancelReservationEvent, do the reject order command");
        String reason = "This is a reason for reject order command ";
        RejectOrderCommand command = new RejectOrderCommand(event.getOrderId(), reason);
        commandGateway.sendAndWait(command);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectEvent event) {
        log.info("OrderRejectEvent method");
    }

    private String cancelProductReservation(String reason, ProductReserveEvent event) {

        CancelProductReservationCommand can = CancelProductReservationCommand.builder()
                .productId(event.getProductId())
                .orderId(event.getOrderId())
                .quantity(event.getQuantity())
                .userId(event.getUserId())
                .reason(reason)
                .build();

        String result = commandGateway.sendAndWait(can);
        return result;

    }

}
