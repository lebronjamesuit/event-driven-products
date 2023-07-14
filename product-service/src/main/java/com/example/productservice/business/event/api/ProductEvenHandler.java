package com.example.productservice.business.event.api;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class ProductEvenHandler {


    @EventHandler
    public void evenHandlerCreateProduct(ProductCreateEvent productCreateEvent){
       // To do code to save repo for Product.
        System.out.println(productCreateEvent.toString());
    }
}
