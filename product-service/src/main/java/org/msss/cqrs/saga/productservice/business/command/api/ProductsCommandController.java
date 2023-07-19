package org.msss.cqrs.saga.productservice.business.command.api;

import org.msss.cqrs.saga.productservice.business.BaseProductController;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
public class ProductsCommandController extends BaseProductController {


	public ProductsCommandController(CommandGateway commandGateway, QueryGateway queryGateway) {
		super(commandGateway, queryGateway);
	}

	@GetMapping("helloProduct")
	public String getProducts() {
		return "Hello Product";
	}

	@PostMapping
	public String createProduct (@Valid @RequestBody CreateProductRestModel createProductRestModel) throws Exception {
		CreateProductCommand createProductCommand = new CreateProductCommand();
		createProductRestModel.setProductId(UUID.randomUUID().toString());

		BeanUtils.copyProperties(createProductRestModel, createProductCommand);

		String  productId = commandGateway.sendAndWait(createProductCommand);
		return productId;
	}

}
