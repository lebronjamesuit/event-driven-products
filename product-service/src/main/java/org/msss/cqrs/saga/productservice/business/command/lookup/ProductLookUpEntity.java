package org.msss.cqrs.saga.productservice.business.command.lookup;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import org.msss.cqrs.saga.productservice.business.event.api.BaseEntity;


@AllArgsConstructor
@Entity
@Table(name = "product_lookup")
public class ProductLookUpEntity extends BaseEntity {

    public ProductLookUpEntity() {
    }

    ;

    @Id
    private String productId;

    private String title;
}
