package com.alejfneto.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alejfneto.dscatalog.dto.CategoryDTO;
import com.alejfneto.dscatalog.entities.Category;
import com.alejfneto.dscatalog.repositories.CategoryRepository;
import com.alejfneto.dscatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional (readOnly=true)
	public List<CategoryDTO> findAll(){
		List <Category> list = repository.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional (readOnly=true)
	public CategoryDTO findByID(Long id) {
		Optional<Category> obj = repository.findById(id);
		CategoryDTO catDTO = new CategoryDTO(obj.orElseThrow(()-> new EntityNotFoundException("Id não encontrado")));
		return catDTO;
	}

}
