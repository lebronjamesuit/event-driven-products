package com.example.productservice.business.command.lookup;


import com.example.productservice.business.event.api.ProductCreateEvent;
import com.example.productservice.business.event.api.ProductEntity;
import com.example.productservice.business.event.api.ProductRepo;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ProcessingGroup("product-group")
public class ProductLookUpEventHandler {

    private final ProductLookUpRepo productLookUpRepo;

    @EventHandler
    public void evenHandlerCreateProduct(ProductCreateEvent productCreateEvent){
        productLookUpRepo.save(
                new ProductLookUpEntity(productCreateEvent.getProductId(),
                        productCreateEvent.getTitle()));
    }
}
