package com.org.msss.cqrs.saga.ordersservice.event.api;

import com.org.msss.cqrs.saga.ordersservice.core.OrderEntity;
import com.org.msss.cqrs.saga.ordersservice.core.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {

    private final OrderRepository orderRepository;

    @EventHandler
    public void handleCreateOrder (OrderCreateEvent orderCreateEvent){
        System.out.println(orderCreateEvent.toString());
        OrderEntity entity = new OrderEntity();
        BeanUtils.copyProperties(orderCreateEvent, entity);
        orderRepository.save(entity);
    }
}
