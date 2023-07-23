package org.msss.cqrs.saga.productservice.business.query.api;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.msss.cqrs.saga.productservice.business.BaseProductController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class ProductQueryController extends BaseProductController {

    public ProductQueryController(CommandGateway commandGateway, QueryGateway queryGateway) {
        super(commandGateway, queryGateway);
    }

    // Find all
    @GetMapping
    public List<ProductRestModel> findAllProducts() {
        FindProductsQuery findProductsQuery = new FindProductsQuery();

        CompletableFuture<List<ProductRestModel>> futureProducts = queryGateway.query(findProductsQuery, ResponseTypes.multipleInstancesOf(ProductRestModel.class));
        List<ProductRestModel> productRestModels = futureProducts.join();

        return productRestModels;
    }
}
