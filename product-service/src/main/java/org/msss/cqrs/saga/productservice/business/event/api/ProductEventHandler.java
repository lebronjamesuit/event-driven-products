package org.msss.cqrs.saga.productservice.business.event.api;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.msss.cqrs.saga.sharedcommon.event.ProductCancelReservationEvent;
import org.msss.cqrs.saga.sharedcommon.event.ProductReserveEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
@ProcessingGroup("product-group")
public class ProductEventHandler {

    private final ProductRepo productRepo;

    @EventHandler
    public void evenHandlerCreateProduct(ProductCreateEvent productCreateEvent) {
        // To do code to save repo for Product.
        System.out.println(productCreateEvent.toString());
        ProductEntity entity = new ProductEntity();
        BeanUtils.copyProperties(productCreateEvent, entity);
        productRepo.save(entity);
    }

    // Catch exception, log it, then continue the execution.
    @ExceptionHandler(resultType = ArrayIndexOutOfBoundsException.class)
    public void handlerTheSameClassedMethodError(ArrayIndexOutOfBoundsException arrayOut) {
        throw arrayOut;
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handlerGeneralException(Exception arrayOut) throws Exception {
        throw arrayOut;
    }

    // Previous step is @EventSourcingHandler in ProductAggregate.class
    // Update read db when product is reserved, it's crutial because of consistency
    @EventHandler
    public void evenHandlerReserveProduct(ProductReserveEvent productReserveEvent) {
        Optional<ProductEntity> productOp = productRepo.findById(productReserveEvent.getProductId());
        if (productOp.isPresent()) {
            ProductEntity entity = productOp.get();
            entity.setQuantity(entity.getQuantity() - productReserveEvent.getQuantity());
            productRepo.save(entity);
        }
    }

    @EventHandler
    public void handleCancelProductReservation(ProductCancelReservationEvent event) {
        Optional<ProductEntity> productOp = productRepo.findById(event.getProductId());
        if (productOp.isPresent()) {
            ProductEntity entity = productOp.get();
            entity.setQuantity(entity.getQuantity() + event.getQuantity());
            productRepo.save(entity);
        }
    }

}
