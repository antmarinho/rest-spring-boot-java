package br.com.acgm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // codigo de erro q vai retornar
public class RequiredObjectIsNullException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public RequiredObjectIsNullException(String ex) {
		
		super(ex);
	}
	
	public RequiredObjectIsNullException() {
		
		super("nao e posisvel persistir um objeto nulo");
	}

}