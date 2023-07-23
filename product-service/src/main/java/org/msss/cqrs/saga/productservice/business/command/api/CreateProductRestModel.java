package org.msss.cqrs.saga.productservice.business.command.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
public class CreateProductRestModel {

    private String productId;
    @NotNull(message = "Title must not be empty")
    @Length(min = 3)
    private String title;

    @Min(value = 1, message = "Price is bigger than 0")
    private BigDecimal price;

    @Min(value = 1, message = "quantity must be greater than 0")
    private int quantity;
}
