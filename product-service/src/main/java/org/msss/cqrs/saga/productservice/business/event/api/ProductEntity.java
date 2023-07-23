package org.msss.cqrs.saga.productservice.business.event.api;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {

    @Id
    @Column(unique = true)
    private String productId;

    private String title;
    private BigDecimal price;
    private Integer quantity;

}

