package com.alejfneto.dscatalog.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.alejfneto.dscatalog.dto.CategoryDTO;
import com.alejfneto.dscatalog.services.CategoryService;

@RestController
@RequestMapping (value = "/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService service;
	
	@GetMapping
	public ResponseEntity<Page<CategoryDTO>> findAll (Pageable pageable){
		Page <CategoryDTO> page  = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(page);
	}
	
	@GetMapping (value = "/{id}")
	public ResponseEntity<CategoryDTO> findById (@PathVariable Long id){
		return ResponseEntity.ok().body(service.findByID (id));
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<CategoryDTO> insert (@RequestBody CategoryDTO catDto){
		CategoryDTO dto = service.insert(catDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
				buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping (value = "/{id}")
	public ResponseEntity<CategoryDTO> update (@PathVariable Long id, @RequestBody CategoryDTO catDto){
		CategoryDTO dto = service.update (id, catDto);
		return ResponseEntity.ok().body(dto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping (value = "/{id}")
	public ResponseEntity<CategoryDTO> delete (@PathVariable Long id){
		service.delete (id);
		return ResponseEntity.noContent().build();
	}

}
