package org.msss.cqrs.saga.productservice.business.command.api;

import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.msss.cqrs.saga.productservice.business.BaseProductController;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
public class ProductsCommandController extends BaseProductController {


    public ProductsCommandController(CommandGateway commandGateway, QueryGateway queryGateway) {
        super(commandGateway, queryGateway);
    }

    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductRestModel createProductRestModel) throws Exception {
        CreateProductCommand createProductCommand = new CreateProductCommand();
        createProductRestModel.setProductId("product" + UUID.randomUUID().toString());

        BeanUtils.copyProperties(createProductRestModel, createProductCommand);

        String productId = commandGateway.sendAndWait(createProductCommand);
        return productId;
    }

}
