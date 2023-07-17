package com.example.productservice.business.command.api;

import com.example.productservice.business.event.api.ProductCreateEvent;
import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.ApplyMore;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;


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

}
