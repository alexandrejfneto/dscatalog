package com.alejfneto.dscatalog.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.alejfneto.dscatalog.dto.ProductDTO;
import com.alejfneto.dscatalog.tests.Factory;
import com.alejfneto.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTestesIntegration {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectmapper;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private Long countTotalProducts;
	ProductDTO productDTO;
	
	private String username, password, bearerToken;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 2L;
		countTotalProducts = 25L;
		productDTO = Factory.createProductDTO();
		
		username = "maria@gmail.com";
		password = "123456";
		
		bearerToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		//bearerToken = "eyJraWQiOiI5YzY2ODA5Yy03YTc5LTQwOTItODY5Yy02ZjFlMTQzOTBkYWQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJteWNsaWVudGlkIiwiYXVkIjoibXljbGllbnRpZCIsIm5iZiI6MTcxNTYyNzY1NywiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIiwiZXhwIjoxNzE1NzE0MDU3LCJpYXQiOjE3MTU2Mjc2NTcsImF1dGhvcml0aWVzIjpbIlJPTEVfT1BFUkFUT1IiLCJST0xFX0FETUlOIl0sInVzZXJuYW1lIjoibWFyaWFAZ21haWwuY29tIn0.bb52xlEnH1Tvcl6uye8ZnJglDmmVRNywxdMJjIBSodhKxtqYYWIm4yMJ0Za4pPLCmKzpx7Mq4E1LnwU9nI6-vPMNadNlt9HjGkZ6LlwfD6dd9nlcQROc9eWcXs19kUNoTQd_nCl76Rlkcd29qRczvFELkMMtOppEaAtO3kwF-_YuJj5BKOwgqVwxgxH3_cT9eqg9Fp4eWD72kWnsnz9u0xkDKxNez5LUE8-b8K1IQjsfgPSZslBNrBt0Q6BE4VLyaIIwKooG06BIfO8zDUJsiaPbZx0_yiYwRBC5ZAy276gMAzrlm2r0vRHo8DRWkGLq0ZAtbrlltknMHgQuEshePw";
	}
	
	@Test
	public void findAllShouldReturnSortedPageWhenSortedByName() throws Exception{
		
		ResultActions result = 
				mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
		result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
		
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenExistingId() throws Exception {
		
		String expectedName = productDTO.getName();
		String expectedDescription = productDTO.getDescription();
		
		String jsonBody = objectmapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.header("Authorization", "Bearer " + bearerToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.description").value(expectedDescription));
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenNonExistingId() throws Exception {
		String jsonBody = objectmapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.header("Authorization", "Bearer " + bearerToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}

}
