package com.alejfneto.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alejfneto.dscatalog.dto.CategoryDTO;
import com.alejfneto.dscatalog.entities.Category;
import com.alejfneto.dscatalog.repositories.CategoryRepository;
import com.alejfneto.dscatalog.services.exceptions.DatabaseException;
import com.alejfneto.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CategoryDTO findByID(Long id) {
		Optional<Category> obj = repository.findById(id);
		CategoryDTO catDTO = new CategoryDTO(obj.orElseThrow(() -> new ResourceNotFoundException("Id não encontrado")));
		return catDTO;
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO catDto) {
		Category cat = new Category(null, catDto.getName());
		cat = repository.save(cat);
		return new CategoryDTO(cat);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO catDto) {
		try {
			Category cat = repository.getReferenceById(id);
			cat.setName(catDto.getName());
			cat = repository.save(cat);
			return new CategoryDTO(cat);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id não encontrado: " + id);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}

}
