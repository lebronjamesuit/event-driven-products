package com.example.productservice.business.command.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {

    private String productId;
    private String title;
    private BigDecimal price;
    private int quantity;
}
