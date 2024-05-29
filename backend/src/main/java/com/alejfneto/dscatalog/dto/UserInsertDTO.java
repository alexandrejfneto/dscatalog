package com.alejfneto.dscatalog.dto;

import com.alejfneto.dscatalog.services.validation.UserInsertValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO {
	private static final long serialVersionUID = 1L;
	
	@NotBlank (message = "Campo obrigatório")
	@Size (min = 8, message = "Deve ter no mínimo 8 caracteres")
	private String password;
	
	public UserInsertDTO() {
		super ();
	}

	public UserInsertDTO(String password) {
		super();
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
