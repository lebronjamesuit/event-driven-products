package com.example.productservice.business.command.api;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/products")
public class ProductsCommandController {

	private CommandGateway commandGateway;

	@Autowired
	public ProductsCommandController(CommandGateway commandGateway){
		this.commandGateway =  commandGateway;
	}


	@GetMapping("helloProduct")
	public String getProducts() {
		return "Hello Product";
	}

	@PostMapping("/add")
	public String createProduct(@RequestBody ProductDTO productDTO){
		CreateProductCommand createProductCommand = new CreateProductCommand();
		productDTO.setProductId(UUID.randomUUID().toString());

		BeanUtils.copyProperties(productDTO, createProductCommand);

		String  productId = commandGateway.sendAndWait(createProductCommand);
		return productId;
	}

}
