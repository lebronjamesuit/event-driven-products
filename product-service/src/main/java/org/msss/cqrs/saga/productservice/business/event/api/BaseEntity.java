package org.msss.cqrs.saga.productservice.business.event.api;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

// @MappedSuperclass tells Hibernate do not ignore the mapping information of this superclass

@Data
@MappedSuperclass
public class BaseEntity {

    @CreationTimestamp
    @Column(updatable = false)
    protected Timestamp createdDate;

    @UpdateTimestamp
    protected Timestamp lastModifiedDate;

}
