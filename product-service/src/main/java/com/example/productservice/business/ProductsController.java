package com.example.productservice.business;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
public class ProductsController {


	@GetMapping("helloProduct")
	public String getProducts() {
		return "Hello Product";
	}
}
