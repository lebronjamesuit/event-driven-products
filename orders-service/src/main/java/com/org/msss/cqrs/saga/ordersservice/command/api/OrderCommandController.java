package com.org.msss.cqrs.saga.ordersservice.command.api;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrderCommandController {

    private final CommandGateway commandGateway;

    @GetMapping
    public String getHelloRest(){
        return "Hello";
    }

    @PostMapping
    public String createOrder(@RequestParam OrderRestModel orderRestModel){
        CreateOrderCommand createOrderCommand   = CreateOrderCommand.builder().orderId(UUID.randomUUID().toString())
                .userId(orderRestModel.getUserId())
                .productId(orderRestModel.getProductId())
                .quantity(orderRestModel.getQuantity())
                .addressId(orderRestModel.getAddressId())
                .orderStatus(orderRestModel.getOrderStatus();

        String resultId = commandGateway.sendAndWait(createOrderCommand);

        return resultId;
    }


}
