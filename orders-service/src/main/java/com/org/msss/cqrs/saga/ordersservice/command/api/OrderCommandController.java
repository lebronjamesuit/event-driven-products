package com.org.msss.cqrs.saga.ordersservice.command.api;

import com.org.msss.cqrs.saga.ordersservice.model.OrderSummary;
import com.org.msss.cqrs.saga.ordersservice.query.api.FindOrderQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrderCommandController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @GetMapping
    public String getHelloRest() {
        return "Hello";
    }

    @PostMapping
    public OrderSummary createOrder(@RequestBody OrderRestModel orderRestModel) {

        String orderId = "order" + UUID.randomUUID();

        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .orderId(orderId)
                .userId(orderRestModel.getUserId())
                .productId(orderRestModel.getProductId())
                .quantity(orderRestModel.getQuantity())
                .addressId(orderRestModel.getAddressId())
                .orderStatus(orderRestModel.getOrderStatus()).build();

        commandGateway.sendAndWait(createOrderCommand);

        SubscriptionQueryResult<OrderSummary, OrderSummary> result =
                queryGateway.subscriptionQuery(new FindOrderQuery(orderId),
                        ResponseTypes.instanceOf(OrderSummary.class),
                        ResponseTypes.instanceOf(OrderSummary.class));


        return result.updates().blockFirst();
    }

}
