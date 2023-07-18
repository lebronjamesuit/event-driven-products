package com.org.msss.cqrs.saga.ordersservice.command.api;


import com.org.msss.cqrs.saga.ordersservice.event.api.OrderCreateEvent;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Data
@Aggregate
@ProcessingGroup("order-group")
public class OrderAggregate {

    @AggregateIdentifier
    private  String orderId;
    private  String userId;
    private  String productId;
    private  int quantity;
    private  String addressId;
    private  OrderStatus orderStatus;

    public OrderAggregate(){};

    @CommandHandler
    public OrderAggregate (CreateOrderCommand createOrderCommand){
        //OrderCreateEvent
        OrderCreateEvent orderCreateEvent = new OrderCreateEvent();
        BeanUtils.copyProperties(createOrderCommand, orderCreateEvent);
        AggregateLifecycle.apply(orderCreateEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreateEvent orderCreateEvent){
        this.orderId = orderCreateEvent.getOrderId();
        this.userId = orderCreateEvent.getUserId();
        this.productId = orderCreateEvent.getProductId();
        this.quantity = orderCreateEvent.getQuantity();
        this.addressId = orderCreateEvent.getAddressId();
        this.orderStatus = orderCreateEvent.getOrderStatus();
    }

}
