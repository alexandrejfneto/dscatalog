package com.alejfneto.dscatalog.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alejfneto.dscatalog.dto.EmailDTO;
import com.alejfneto.dscatalog.dto.NewPasswordDTO;
import com.alejfneto.dscatalog.entities.PasswordRecover;
import com.alejfneto.dscatalog.entities.User;
import com.alejfneto.dscatalog.repositories.PasswordRecoverRepository;
import com.alejfneto.dscatalog.repositories.UserRepository;
import com.alejfneto.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.validation.Valid;

@Service
public class AuthService {
	
	@Value("${email.password-recover.token.minutes}")
	private Long tokenMinutes;
	
	@Value("${email.password-recover.uri}")
	private String recoverUri;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordRecoverRepository passwordRecoverRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public void createRecoverToken(EmailDTO body) {
		
		User user = userRepository.findByEmail(body.getEmail());
		if (user == null) {
			throw new ResourceNotFoundException("Email não cadastrado");
		}
		
		String token = UUID.randomUUID().toString();
		
		PasswordRecover entity = new PasswordRecover();
		entity.setEmail(body.getEmail());
		entity.setToken(token);
		entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
		
		entity = passwordRecoverRepository.save(entity);
		
		String bodyEmail = "Acesse o link para definir uma nova senha:\n\n"
				+ recoverUri + token + "/" + body.getEmail() + "\n\n"
						+ "Validade de " + tokenMinutes + " minutos.";
		
		emailService.sendEmail(body.getEmail(), "Recuperação de senha - DS Catalog", bodyEmail);
		
	}

	@Transactional
	public void saveNewPassword(@Valid NewPasswordDTO newPasswordDto, String email) {
		List<PasswordRecover> result = passwordRecoverRepository.searchValidTokens(newPasswordDto.getToken(), Instant.now(), email);
		if (result.size() == 0) {
			throw new ResourceNotFoundException("Token inválido");
		}
		
		/*
		if (!email.equals(result.get(0).getEmail())) {
			throw new ResourceNotFoundException("Token não pertence ao usuário " + email + " " + result.get(0).getEmail());
		}
		*/
		
		User user = userRepository.findByEmail(result.get(0).getEmail());
		user.setPassword(passwordEncoder.encode(newPasswordDto.getPassword()));
		user = userRepository.save(user);
	}

}
