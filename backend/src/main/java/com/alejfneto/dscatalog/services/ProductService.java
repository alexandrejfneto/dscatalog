package com.alejfneto.dscatalog.services;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alejfneto.dscatalog.dto.CategoryDTO;
import com.alejfneto.dscatalog.dto.ProductDTO;
import com.alejfneto.dscatalog.entities.Category;
import com.alejfneto.dscatalog.entities.Product;
import com.alejfneto.dscatalog.projections.ProductProjection;
import com.alejfneto.dscatalog.repositories.CategoryRepository;
import com.alejfneto.dscatalog.repositories.ProductRepository;
import com.alejfneto.dscatalog.services.exceptions.DatabaseException;
import com.alejfneto.dscatalog.services.exceptions.ResourceNotFoundException;
import com.alejfneto.dscatalog.util.Utils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable){
		Page<Product> list = repository.findAll(pageable);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findByID(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id não encontrado"));
		ProductDTO dto = new ProductDTO(entity, entity.getCategories());
		return dto;
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product prod = new Product();
		prod = dtoToEntity(dto, prod);
		prod = repository.save(prod);
		return new ProductDTO(prod, prod.getCategories());
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product prod = repository.getReferenceById(id);
			prod = dtoToEntity(dto, prod);
			prod = repository.save(prod);
			return new ProductDTO(prod, prod.getCategories());
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id não encontrado: " + id);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Id não encontrado");
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}
	
	private Product dtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		entity.setDate(Instant.now());
		entity.getCategories().clear();
		for (CategoryDTO catDTO : dto.getCategories()) {
			Category catEntity = categoryRepository.getReferenceById(catDTO.getId());
			entity.getCategories().add(new Category(catEntity.getId(),catEntity.getName()));
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<ProductDTO> searchProducts(String name, String categoryId, Pageable pageable) {
		
		List <Long> catIds = Arrays.asList();
		if (!"0".equals(categoryId)) {
		/*String[] vetor = categoryId.split(",");
		List<String> list = Arrays.asList(vetor);
		catIds = list.stream().map(x-> Long.parseLong(x)).toList();*/
		//Linhas acima resumidas em uma única
		catIds = Arrays.asList(categoryId.split(",")).stream().map(Long::parseLong).toList();
		}
		Page<ProductProjection> page = repository.searchProducts(catIds, name, pageable);
		
		List <Long> productIds = page.map(x -> x.getId()).toList();
		List <Product> entities = repository.searchProductsWhithCategories(productIds);
		List <ProductDTO> dtos = entities.stream().map(x -> new ProductDTO(x, x.getCategories())).toList();
		//Linhas acima resumidas em uma única
		//List <ProductDTO> dtos = repository.searchProductsWhithCategories(page.map(x -> x.getId()).toList()).stream().map(x -> new ProductDTO(x, x.getCategories())).toList();
		entities = (List<Product>) Utils.replace(page.getContent(), entities);
		
		Page<ProductDTO> pageDto = new PageImpl<>(dtos, page.getPageable(),page.getTotalElements());
		return pageDto;
	}

}
