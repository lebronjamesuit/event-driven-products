package org.msss.cqrs.saga.productservice.business.query.api;

import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.msss.cqrs.saga.productservice.business.event.api.ProductEntity;
import org.msss.cqrs.saga.productservice.business.event.api.ProductRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductQueryHandler {

    private final ProductRepo productRepo;

    @QueryHandler
    public List<ProductRestModel> queryHandlerDummy(FindProductsQuery findProductsQuery) {
        List<ProductEntity> productEntities = productRepo.findAll();
        List<ProductRestModel> productRestModelList = new ArrayList<>();
        productEntities.stream().forEach(entity -> {
            ProductRestModel productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(entity, productRestModel);
            productRestModelList.add(productRestModel);
        });
        return productRestModelList;
    }
}
