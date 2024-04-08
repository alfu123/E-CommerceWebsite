package com.learning.ecommerce.converters;

import java.util.stream.Collectors;

import com.learning.ecommerce.dto.ProductDto;
import com.learning.ecommerce.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoConverter {
	public ProductDto convertEntityToDto(Product product) {

		ProductDto productDto = new ProductDto();
		productDto.setPid(product.getPid());
		productDto.setPname(product.getPname());
		productDto.setDescription(product.getDescription());
		productDto.setBrand(product.getBrand());
		productDto.setImageUrl(product.getImageUrl());
		productDto.setPrice(product.getPrice());

		ServiceabilityDtoConverter serviceabilityDtoConverter = new ServiceabilityDtoConverter();

		productDto.setServiceability(product.getServiceability().stream()
				.map((x) -> serviceabilityDtoConverter.convertEntityToDto(x)).collect(Collectors.toList()));

		return productDto;
	}

	public Product convertDtoToEntity(ProductDto productDto) {

		Product product = new Product();
		product.setPid(productDto.getPid());
		product.setPname(productDto.getPname());
		product.setDescription(productDto.getDescription());
		product.setBrand(productDto.getBrand());
		product.setImageUrl(productDto.getImageUrl());
		product.setPrice(productDto.getPrice());

		ServiceabilityDtoConverter serviceabilityDtoConverter = new ServiceabilityDtoConverter();

		product.setServiceability(productDto.getServiceability().stream()
				.map((x) -> serviceabilityDtoConverter.convertDtoToEntity(x)).collect(Collectors.toList()));

		return product;
	}
}
