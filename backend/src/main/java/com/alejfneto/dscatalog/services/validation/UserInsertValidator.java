package com.alejfneto.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alejfneto.dscatalog.controllers.exceptions.FieldMessage;
import com.alejfneto.dscatalog.dto.UserInsertDTO;
import com.alejfneto.dscatalog.entities.User;
import com.alejfneto.dscatalog.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		// Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
		List<FieldMessage> list = new ArrayList<>();	
		User user = repository.findByEmail(dto.getEmail());
		if (user != null) {
			list.add(new FieldMessage("email", "Email já cadastrado!"));
		}
		
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}