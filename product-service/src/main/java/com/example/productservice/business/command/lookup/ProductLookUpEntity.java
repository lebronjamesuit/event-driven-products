package com.example.productservice.business.command.lookup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@Entity
@Table(name = "product_lookup")
public class ProductLookUpEntity {

    public ProductLookUpEntity(){};

    @Id
    private String productId;

    private String title;
}
