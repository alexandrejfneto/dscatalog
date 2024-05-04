package com.alejfneto.dscatalog.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.alejfneto.dscatalog.dto.UserDTO;
import com.alejfneto.dscatalog.dto.UserInsertDTO;
import com.alejfneto.dscatalog.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping (value = "/users")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll (Pageable pageable){
		Page <UserDTO> list  = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping (value = "/{id}")
	public ResponseEntity<UserDTO> findById (@PathVariable Long id){
		return ResponseEntity.ok().body(service.findByID (id));
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> insert (@Valid @RequestBody UserInsertDTO userInsertdto){
		UserDTO userDto = service.insert(userInsertdto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
				buildAndExpand(userDto.getId()).toUri();
		return ResponseEntity.created(uri).body(userDto);
	}
	
	@PutMapping (value = "/{id}")
	public ResponseEntity<UserDTO> update (@PathVariable Long id, @Valid @RequestBody UserDTO dto){
		UserDTO userDto = service.update (id, dto);
		return ResponseEntity.ok().body(userDto);
	}
	
	@DeleteMapping (value = "/{id}")
	public ResponseEntity<UserDTO> delete (@PathVariable Long id){
		service.delete (id);
		return ResponseEntity.noContent().build();
	}

}
