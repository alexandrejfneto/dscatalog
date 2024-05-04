package com.alejfneto.dscatalog.dto;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.alejfneto.dscatalog.entities.Category;
import com.alejfneto.dscatalog.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO {
	
	private Long id;
	
	@Size (min = 5, max = 60, message = "Nome deve ter entre 5 e 60 caracteres!")
	@NotBlank (message ="Campo obrigatório!")
	private String name;
	
	private String description;
	@Positive (message = "O preço deve ter um valor positivo!")
	private Double price;
	
	private String imgUrl;
	
	@PastOrPresent (message = "A data não pode ser futura!")
	private Instant date;
	
	private Set<CategoryDTO> categories = new HashSet<>();
	
	public ProductDTO(){
	}

	public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}
	
	public ProductDTO(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.imgUrl = entity.getImgUrl();
		this.date = entity.getDate();
	}
	
	public ProductDTO(Product entity, Set<Category> categories) {
		this(entity);
		for (Category cat : categories) {
			this.categories.add(new CategoryDTO(cat));
		}
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Double getPrice() {
		return price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public Instant getDate() {
		return date;
	}

	public Set<CategoryDTO> getCategories() {
		return categories;
	}

}
