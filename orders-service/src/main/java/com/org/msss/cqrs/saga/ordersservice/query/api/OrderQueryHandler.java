package com.org.msss.cqrs.saga.ordersservice.query.api;

import com.org.msss.cqrs.saga.ordersservice.core.OrderEntity;
import com.org.msss.cqrs.saga.ordersservice.core.OrderRepository;
import com.org.msss.cqrs.saga.ordersservice.model.OrderSummary;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class OrderQueryHandler {

    private OrderRepository orderRepo;

    @QueryHandler
    public OrderSummary handle(FindOrderQuery query){
        Optional<OrderEntity> opOrderEntity = orderRepo.findById(query.getOrderId());
        return new OrderSummary(opOrderEntity.get().getOrderId(), opOrderEntity.get().getOrderStatus(), "");
    }

}
