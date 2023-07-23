package com.org.msss.cqrs.saga.ordersservice.event.api;

import com.org.msss.cqrs.saga.ordersservice.command.api.OrderStatus;
import com.org.msss.cqrs.saga.ordersservice.core.OrderEntity;
import com.org.msss.cqrs.saga.ordersservice.core.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {

    private final OrderRepository orderRepository;

    @EventHandler
    public void handleCreateOrder(OrderCreateEvent orderCreateEvent) {
        System.out.println(orderCreateEvent.toString());
        OrderEntity entity = new OrderEntity();
        BeanUtils.copyProperties(orderCreateEvent, entity);
        orderRepository.save(entity);
    }

    // Handle Approve Order
    @EventHandler
    public void handleApproveOrder(OrderApproveEvent event) {
        updateStatusOfOrder(event, event.getOrderId(), event.getOrderStatus());
    }

    // Handle Reject Order
    @EventHandler
    public void handleRejectOrder(OrderRejectEvent event) {
        updateStatusOfOrder(event, event.getOrderId(), event.getOrderStatus());
    }

    private void updateStatusOfOrder(Object event, String orderId, OrderStatus orderStatus) {
        if (event instanceof OrderApproveEvent ||
                event instanceof OrderRejectEvent) {
            Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(orderId);
            if (optionalOrderEntity.isEmpty()) {
                return;
            }
            OrderEntity entity = optionalOrderEntity.get();
            entity.setOrderStatus(orderStatus);
            orderRepository.save(entity);
        }
    }
}
