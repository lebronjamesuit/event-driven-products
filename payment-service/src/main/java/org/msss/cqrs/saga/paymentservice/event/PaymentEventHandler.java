package org.msss.cqrs.saga.paymentservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.msss.cqrs.saga.paymentservice.dao.PaymentEntity;
import org.msss.cqrs.saga.paymentservice.dao.PaymentRepository;
import org.msss.cqrs.saga.sharedcommon.event.PaymentProcessEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentEventHandler {

    private final PaymentRepository paymentRepository;

    public PaymentEventHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessEvent event) {
        log.info("PaymentProcessedEvent is called for orderId: " + event.getOrderId());
        PaymentEntity paymentEntity = new PaymentEntity();
        BeanUtils.copyProperties(event, paymentEntity);
        paymentRepository.save(paymentEntity);
        log.info(" paymentRepository.save(paymentEntity)");

    }
}
