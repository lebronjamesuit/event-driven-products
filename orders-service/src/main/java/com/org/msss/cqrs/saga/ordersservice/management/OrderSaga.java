package com.org.msss.cqrs.saga.ordersservice.management;

import com.org.msss.cqrs.saga.ordersservice.command.api.ApproveOrderCommand;
import com.org.msss.cqrs.saga.ordersservice.event.api.OrderApproveEvent;
import com.org.msss.cqrs.saga.ordersservice.event.api.OrderCreateEvent;
import com.org.msss.cqrs.saga.ordersservice.event.api.OrderRejectEvent;
import com.org.msss.cqrs.saga.ordersservice.model.OrderSummary;
import com.org.msss.cqrs.saga.ordersservice.query.api.FindOrderQuery;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.*;
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
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Predicate;


@NoArgsConstructor
@Slf4j
@Saga
public class OrderSaga {

    // Transient means it's not part of the Saga serialization process of Axon
    private transient final String DEADLINE_NAME = "deadline-payment";
    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;
    @Autowired
    private transient DeadlineManager deadlineManager;

    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    // I want to store this field with Saga in JpaSagaStore,
    private String scheduleId = "";

    public OrderSaga(final CommandGateway commandGateway, final QueryGateway queryGateway, final DeadlineManager deadlineManager) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.deadlineManager = deadlineManager;
    }

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
                    RejectOrderCommand command = new RejectOrderCommand(orderCreateEvent.getOrderId(),
                            commandResultMessage.exceptionResult().getMessage());

                    commandGateway.send(command);
                    return;
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

        // I'm afraid payment process will take too long, so I wrote a deadline
        // When test it, do not start payment-service
        scheduleId = this.deadlineManager.schedule(Duration.of(10, ChronoUnit.SECONDS), DEADLINE_NAME, productReserveEvent);
        log.info("create + scheduleId: " + scheduleId);
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
        cancelDeadline();
        log.info("PaymentProcessEvent + scheduleId: " + scheduleId);
        ApproveOrderCommand approveOrderCommand =
                new ApproveOrderCommand(paymentProcessEvent.getOrderId());

        commandGateway.send(approveOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApproveEvent event) {
        log.info("Finished OrderApproveEvent");
        // after finished saga, I need to emit for the controller
        queryUpdateEmitter.emit(FindOrderQuery.class, p -> true,
                new OrderSummary(event.getOrderId(), event.getOrderStatus(),""));
    }

    // new saga lifecycle
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
        // after finished saga, I need to emit for the controller
        queryUpdateEmitter.emit(FindOrderQuery.class, p -> true,
                new OrderSummary(event.getOrderId(), event.getOrderStatus(),""));

        queryUpdateEmitter.complete(FindOrderQuery.class, p -> true);
    }

    private String cancelProductReservation(String reason, ProductReserveEvent event) {

        // if is there any reason the product cannot reserved, then the deadline should be cancel as well
        cancelDeadline();

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

    private void cancelDeadline() {
        log.info("cancelDeadline + scheduleId: " + scheduleId);
        if (StringUtils.hasText(scheduleId)) {
            deadlineManager.cancelSchedule(this.DEADLINE_NAME, this.scheduleId);
            scheduleId = "";
        }
    }

    // After command bus send the command, It waits a certain time
    // which is 10 seconds in this case, Deadline schedule will trigger
    @DeadlineHandler(deadlineName = DEADLINE_NAME)
    public void onHandleDeadlinePayment(ProductReserveEvent e) {
        log.info(" Cannot reach payment, start compensation process");
        cancelProductReservation("Payment process timeout", e);
    }

}
