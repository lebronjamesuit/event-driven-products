package com.example.productservice.business.query.api;

import com.example.productservice.business.event.api.ProductEntity;
import com.example.productservice.business.event.api.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductQueryHandler {

    private final ProductRepo productRepo;

    @QueryHandler
    public List<ProductRestModel> queryHandlerDummy(FindProductsQuery findProductsQuery){
        List<ProductEntity> productEntities = productRepo.findAll();
        List<ProductRestModel> productRestModelList  = new ArrayList<>();
        productEntities.stream().forEach(entity -> {
            ProductRestModel productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(entity,productRestModel);
            productRestModelList.add(productRestModel);
        });
        return productRestModelList;
    }
}
