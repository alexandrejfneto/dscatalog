package com.alejfneto.dscatalog.dto;

import com.alejfneto.dscatalog.entities.Category;

public class CategoryDTO {
	
	private Long id;
	private String name;
	
	public CategoryDTO() {
	}

	public CategoryDTO(Long id, String name) {
		id = id;
		this.name = name;
	}
	
	public CategoryDTO (Category entity) {
		id = entity.getId();
		name = entity.getName();
	}
	
}
