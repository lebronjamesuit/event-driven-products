package com.org.msss.cqrs.saga.ordersservice.query.api;

import lombok.*;


@RequiredArgsConstructor
@Value
public class FindOrderQuery {
    private final String orderId;
}
