package org.msss.cqrs.saga.productservice.business.command.api;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Data
public class CreateProductCommand {

    @TargetAggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private int quantity;

}
