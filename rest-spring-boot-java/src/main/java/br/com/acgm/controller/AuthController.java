package br.com.acgm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.acgm.data.vo.security.AccountCredentialsVO;
import br.com.acgm.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Endpoint")
public class AuthController {
	
	@Autowired
	AuthServices authServices;
	
	@SuppressWarnings("rawtypes")
	@Operation(summary = "Authenticates a user and returns a token")
	@PostMapping(value = "/signin")
	public ResponseEntity signin(@RequestBody AccountCredentialsVO data) {
		
		if(checkIfParamsIsNotNull(data))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("invalid client request");
		
		var token = authServices.signin(data);
		
		if(token == null)
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("invalid client request");

		return token;
	}
	
	@SuppressWarnings("rawtypes")
	@Operation(summary = "refresh token for authenticated a user and returns a token")
	@PutMapping(value = "/refresh/{userName}")
	public ResponseEntity refreshToken(@PathVariable("userName") String userName,
			@RequestHeader("Authorization") String refreshToken) {
		
		if(checkIfParamsIsNotNull(userName, refreshToken))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("invalid client request");
		
		var token = authServices.refreshToken(userName,refreshToken);
		
		if(token == null)
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("invalid client request");
		
		return token;
	}

	private boolean checkIfParamsIsNotNull(String userName, String refreshToken) {
		return refreshToken == null || refreshToken.isBlank() || userName == null || userName.isBlank();
	}
	
	private boolean checkIfParamsIsNotNull(AccountCredentialsVO data) {
		return data == null || data.getUserName() == null || data.getUserName().isBlank() || data.getPassword() == null || data.getPassword().isBlank();
	}

}
