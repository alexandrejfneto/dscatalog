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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.alejfneto.dscatalog.dto.ProductDTO;
import com.alejfneto.dscatalog.projections.ProductProjection;
import com.alejfneto.dscatalog.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping (value = "/products")
public class ProductController {
	
	@Autowired
	private ProductService service;
	
	/*@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAll (Pageable pageable){
		Page <ProductDTO> list  = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}*/
	
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> searchProducts (
			@RequestParam (value = "name", defaultValue = "") String name,
			@RequestParam (value = "categoryId", defaultValue = "0") String categoryId,
			Pageable pageable
			){
		Page <ProductDTO> list  = service.searchProducts(name, categoryId, pageable);
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping (value = "/{id}")
	public ResponseEntity<ProductDTO> findById (@PathVariable Long id){
		return ResponseEntity.ok().body(service.findByID (id));
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<ProductDTO> insert (@Valid @RequestBody ProductDTO prodDto){
		ProductDTO dto = service.insert(prodDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
				buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping (value = "/{id}")
	public ResponseEntity<ProductDTO> update (@PathVariable Long id, @Valid @RequestBody ProductDTO prodDto){
		ProductDTO dto = service.update (id, prodDto);
		return ResponseEntity.ok().body(dto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping (value = "/{id}")
	public ResponseEntity<ProductDTO> delete (@PathVariable Long id){
		service.delete (id);
		return ResponseEntity.noContent().build();
	}

}
