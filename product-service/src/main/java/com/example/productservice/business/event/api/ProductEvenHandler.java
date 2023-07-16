package com.example.productservice.business.event.api;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@ProcessingGroup("product-group")
public class ProductEvenHandler {

    private final ProductRepo productRepo;

    @EventHandler
    public void evenHandlerCreateProduct(ProductCreateEvent productCreateEvent){
       // To do code to save repo for Product.
        System.out.println(productCreateEvent.toString());
        ProductEntity entity = new ProductEntity();
        BeanUtils.copyProperties(productCreateEvent, entity);
        productRepo.save(entity);
    }
}
