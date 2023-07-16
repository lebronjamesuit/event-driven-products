package com.example.productservice.business.query.api;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRestModel {

    private String productId;
    private String title;
    private BigDecimal price;
    private int quantity;
}
