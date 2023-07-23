package org.msss.cqrs.saga.productservice.business.command.lookup;


import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.msss.cqrs.saga.productservice.business.event.api.ProductCreateEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ProcessingGroup("product-group")
public class ProductLookUpEventHandler {

    private final ProductLookUpRepo productLookUpRepo;

    @EventHandler
    public void evenHandlerCreateProduct(ProductCreateEvent productCreateEvent) {
        productLookUpRepo.save(
                new ProductLookUpEntity(productCreateEvent.getProductId(),
                        productCreateEvent.getTitle()));
    }
}
