package org.msss.cqrs.saga.productservice.business.command.api;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.msss.cqrs.saga.productservice.business.event.api.ProductCreateEvent;
import org.msss.cqrs.saga.sharedcommon.command.CancelProductReservationCommand;
import org.msss.cqrs.saga.sharedcommon.command.ReserveProductCommand;
import org.msss.cqrs.saga.sharedcommon.event.ProductCancelReservationEvent;
import org.msss.cqrs.saga.sharedcommon.event.ProductReserveEvent;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Slf4j
@Aggregate(snapshotTriggerDefinition = "products-snapshot")
//@Aggregate
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private int quantity;

    // Required this constructor
    public ProductAggregate() {
    }


    // Constructor tells Axon that this class has both @Aggregate + many @Command Handler
    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) throws Exception {
        ProductCreateEvent productCreateEvent = new ProductCreateEvent();
        BeanUtils.copyProperties(createProductCommand, productCreateEvent);
        AggregateLifecycle.apply(productCreateEvent);

    }

    // Method tell
    @EventSourcingHandler
    public void on(ProductCreateEvent productCreateEvent) {
        this.productId = productCreateEvent.getProductId();
        this.title = productCreateEvent.getTitle();
        this.price = productCreateEvent.getPrice();
        this.quantity = productCreateEvent.getQuantity();

    }

    // Saga reserve product
    @CommandHandler
    public void methodSagaForReserveProduct(ReserveProductCommand reserveProductCommand) {
        //
        ProductReserveEvent productReserveEvent = new ProductReserveEvent();
        BeanUtils.copyProperties(reserveProductCommand, productReserveEvent);

        // Validate
        if (quantity < productReserveEvent.getQuantity()) {
            throw new IllegalArgumentException("Not enough of Item to reserve");
        }
        log.info("QUANTITY = " + quantity);

        AggregateLifecycle.apply(productReserveEvent);
    }

    @EventSourcingHandler
    public void on(ProductReserveEvent productReserveEvent) {
        quantity -= productReserveEvent.getQuantity();
        log.info("After handler, QUANTITY = " + quantity);
    }

    // Saga rollback, Cancel product reservation
    @CommandHandler
    public void productCancelReservation(CancelProductReservationCommand command) {
        ProductCancelReservationEvent event = new ProductCancelReservationEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ProductCancelReservationEvent event) {
        quantity += event.getQuantity();
        log.info("After ProductCancelReservationEvent handler, QUANTITY = " + quantity);
    }

}
