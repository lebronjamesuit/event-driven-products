package org.msss.cqrs.saga.productservice.business.event.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCreateEvent {

    private String productId;
    private String title;
    private BigDecimal price;
    private int quantity;

}
