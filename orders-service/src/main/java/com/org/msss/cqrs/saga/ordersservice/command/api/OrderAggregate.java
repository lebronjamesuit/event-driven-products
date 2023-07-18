package com.org.msss.cqrs.saga.ordersservice.command.api;


import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

@Data
@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    public  String orderId;
    private  String userId;
    private  String productId;
    private  int quantity;
    private  String addressId;
    private  OrderStatus orderStatus;

}
