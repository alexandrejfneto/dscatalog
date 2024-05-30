package com.alejfneto.dscatalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewPasswordDTO {
	
	@NotBlank(message = "Campo obrigatório")
	private String token;
	@NotBlank(message = "Campo obrigatório")
	@Size (min = 8)
	private String password;
	
	public NewPasswordDTO() {
	}

	public NewPasswordDTO(String token, String password) {
		this.token = token;
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public String getPassword() {
		return password;
	}
	
}
