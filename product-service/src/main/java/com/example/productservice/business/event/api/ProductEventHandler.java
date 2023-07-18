package com.example.productservice.business.event.api;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@ProcessingGroup("product-group")
public class ProductEventHandler {

    private final ProductRepo productRepo;

    @EventHandler
    public void evenHandlerCreateProduct(ProductCreateEvent productCreateEvent){
       // To do code to save repo for Product.
        System.out.println(productCreateEvent.toString());
        ProductEntity entity = new ProductEntity();
        BeanUtils.copyProperties(productCreateEvent, entity);
        productRepo.save(entity);

        // For Demo Rollback Transaction.
       // throw new ArrayIndexOutOfBoundsException("FORCE THROW ");
    }

    // Catch exception, log it, then continue the execution.
    @ExceptionHandler(resultType = ArrayIndexOutOfBoundsException.class)
    public void handlerTheSameClassedMethodError(ArrayIndexOutOfBoundsException arrayOut){
        throw arrayOut;
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handlerGeneralException(Exception arrayOut) throws Exception {
        throw arrayOut;
    }
}
