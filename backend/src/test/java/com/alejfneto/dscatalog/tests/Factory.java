package com.alejfneto.dscatalog.tests;

import java.time.Instant;

import com.alejfneto.dscatalog.dto.ProductDTO;
import com.alejfneto.dscatalog.entities.Category;
import com.alejfneto.dscatalog.entities.Product;

public class Factory {
	
	public static Product createProduct () {
		Product product = new Product (1L, "Phone", "Good Phone", 800.0, "PC Gamer Ex', 1350.0, 'Lorem ipsum dolor sit amet, consectetur adipiscing", Instant.parse("2020-07-14T10:00:00Z"));
		product.getCategories().add(new Category(2L, "Eletronics"));
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO (product, product.getCategories());
	}

}
