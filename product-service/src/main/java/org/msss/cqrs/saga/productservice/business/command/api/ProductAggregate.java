package org.msss.cqrs.saga.productservice.business.command.api;

import org.msss.cqrs.saga.productservice.business.event.api.ProductCreateEvent;
import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;
import org.msss.cqrs.saga.sharedcommon.event.ProductReserveEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.msss.cqrs.saga.sharedcommon.command.ReserveProductCommand;
import org.springframework.beans.BeanUtils;

@Slf4j
@Aggregate
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private int quantity;


    // Required this constructor
    public ProductAggregate(){};

    // Constructor tells Axon that this class has both @Aggregate + many @Command Handler
    @CommandHandler
    public ProductAggregate (CreateProductCommand createProductCommand) throws Exception {
        ProductCreateEvent productCreateEvent = new ProductCreateEvent();
        BeanUtils.copyProperties(createProductCommand, productCreateEvent);
        AggregateLifecycle.apply(productCreateEvent);

    }

    // Method tell
    @EventSourcingHandler
    public void on(ProductCreateEvent productCreateEvent){
        this.productId = productCreateEvent.getProductId();
        this.title = productCreateEvent.getTitle();
        this.price= productCreateEvent.getPrice();
        this.quantity = productCreateEvent.getQuantity();

    }

    // Saga
    @CommandHandler
    public void methodSagaForReserveProduct(ReserveProductCommand reserveProductCommand){
        //
        ProductReserveEvent productReserveEvent = new ProductReserveEvent();
        BeanUtils.copyProperties(reserveProductCommand, productReserveEvent);

        // Validate
        if(quantity < productReserveEvent.getQuantity()){
            throw new IllegalArgumentException("Not enough of Item to reserve");
        }
        log.info("QUANTITY = " + quantity);

        AggregateLifecycle.apply(productReserveEvent);
    }

    @EventSourcingHandler
    public void on(ProductReserveEvent productReserveEvent){
        quantity -= productReserveEvent.getQuantity();
        log.info("After handler, QUANTITY = " + quantity);
    }

}
