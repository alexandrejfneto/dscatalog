package com.alejfneto.dscatalog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alejfneto.dscatalog.dto.EmailDTO;
import com.alejfneto.dscatalog.dto.NewPasswordDTO;
import com.alejfneto.dscatalog.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping (value = "/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping (value = "/recover-token")
	public ResponseEntity<Void> createRecoveryToken (@Valid @RequestBody EmailDTO body){
		authService.createRecoverToken(body);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping (value = "/new-password/{token}/{email}")
	public ResponseEntity<Void> saveNewPassword (@PathVariable String email, @Valid @RequestBody NewPasswordDTO newPasswordDto){
		authService.saveNewPassword(newPasswordDto, email);
		return ResponseEntity.noContent().build();
	}
	
}
