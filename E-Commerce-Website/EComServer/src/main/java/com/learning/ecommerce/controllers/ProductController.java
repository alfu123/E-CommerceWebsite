package com.learning.ecommerce.controllers;

import com.learning.ecommerce.dto.ProductDto;
import com.learning.ecommerce.models.Product;
import com.learning.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")

public class ProductController {

	@Autowired
    ProductService productService;

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getProduct(@RequestParam(required = false) Integer pid,
			@RequestParam(required = false) String pname, @RequestParam(required = false) String brand) {

		return productService.getProduct(pid, pname, brand);
	}

	@GetMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> checkAvailability(@RequestParam("pid") int pid,
			@RequestParam("pincode") int pincode) {
		return productService.getAvailability(pid, pincode);
	}

	@PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public String addProduct(@RequestBody ProductDto productDto) {

		return productService.addProduct(productDto);
	}



	@PreAuthorize("hasRole('User')")
	@GetMapping({"/getProductDetails/{isSingleProductCheckout}/{productId}"})
	public ResponseEntity<String> getProductDetails(@PathVariable(name = "isSingleProductCheckout" ) boolean isSingleProductCheckout,
													@PathVariable(name = "productId")  Integer productId) {
		System.out.println("alahdjjd");
		return productService.getProductDetails(isSingleProductCheckout, productId);
	}

}
