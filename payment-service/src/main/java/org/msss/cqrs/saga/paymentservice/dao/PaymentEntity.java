package org.msss.cqrs.saga.paymentservice.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    private  String paymentId;
    private  String orderId;
}
