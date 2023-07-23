package org.msss.cqrs.saga.sharedcommon.event;

import lombok.Data;

@Data
public class ProductReserveEvent {
    private String productId;
    private int quantity;
    private String orderId;
    private String userId;
}

