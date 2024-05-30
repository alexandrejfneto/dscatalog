package com.alejfneto.dscatalog.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alejfneto.dscatalog.dto.EmailDTO;
import com.alejfneto.dscatalog.entities.PasswordRecover;
import com.alejfneto.dscatalog.entities.User;
import com.alejfneto.dscatalog.repositories.PasswordRecoverRepository;
import com.alejfneto.dscatalog.repositories.UserRepository;
import com.alejfneto.dscatalog.services.exceptions.ResourceNotFoundException;

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
				+ recoverUri + token + "\n\n"
						+ "Validade de " + tokenMinutes + " minutos.";
		
		emailService.sendEmail(body.getEmail(), "Recuperação de senha - DS Catalog", bodyEmail);
		
	}

}
