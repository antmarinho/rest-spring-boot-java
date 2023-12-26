package br.com.acgm.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import br.com.acgm.exceptions.UnsupportedMathOperationException;
import br.com.acgm.funcoes.FuncoesAuxiliares;
import br.com.acgm.math.SimpleMath;

@RestController
public class MathController {
	
	private FuncoesAuxiliares func = new FuncoesAuxiliares();
	private SimpleMath sm = new SimpleMath();
	
	@RequestMapping(value = "/sum/{numberOne}/{numberTwo}", method = RequestMethod.GET)
	public Double sum(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) throws Exception {
		
		if(!func.isNumeric(numberOne) || !func.isNumeric(numberTwo)) 
			
			throw new UnsupportedMathOperationException("Please set a numeric value");
		
		
		return sm.sum(func.convertToDouble(numberOne), func.convertToDouble(numberTwo));
		
	}
	
	@RequestMapping(value = "/sub/{numero1}/{numero2}", method = RequestMethod.GET)
	public Double sub(@PathVariable(value = "numero1") String numero1, @PathVariable(value = "numero2") String numero2) {
		
		if(!func.isNumeric(numero1) || !func.isNumeric(numero2)) 
			
			throw new UnsupportedMathOperationException("Please set a numeric value");

		
		return sm.sub(func.convertToDouble(numero1), func.convertToDouble(numero2));
	}
	
	@RequestMapping(value = "/mult/{numero1}/{numero2}", method = RequestMethod.GET)
	public Double mult(@PathVariable(value = "numero1") String numero1, @PathVariable(value = "numero2") String numero2) {
		
		if(!func.isNumeric(numero1) || !func.isNumeric(numero2)) 
			
			throw new UnsupportedMathOperationException("Please set a numeric value");

		
		return sm.mult(func.convertToDouble(numero1), func.convertToDouble(numero2));
	}
	
	@RequestMapping(value = "/div/{numero1}/{numero2}", method = RequestMethod.GET)
	public Double div(@PathVariable(value = "numero1") String numero1, @PathVariable(value = "numero2") String numero2) {
		
		if(!func.isNumeric(numero1) || !func.isNumeric(numero2))
			
			throw new UnsupportedMathOperationException("Please set a numeric value");

		
		return sm.div(func.convertToDouble(numero1), func.convertToDouble(numero2));
	}
	
	@RequestMapping(value = "/media/{numero1}/{numero2}", method = RequestMethod.GET)
	public Double media(@PathVariable(value = "numero1") String numero1, @PathVariable(value = "numero2") String numero2) {
		
		if(!func.isNumeric(numero1) || !func.isNumeric(numero2)) 
			
			throw new UnsupportedMathOperationException("Please set a numeric value");
		
		
		return sm.media(func.convertToDouble(numero1), func.convertToDouble(numero2));
	}
	
	@RequestMapping(value = "/raiz/{numero}", method = RequestMethod.GET)
	public Double raiz(@PathVariable(value = "numero") String numero) {
		
		if(!func.isNumeric(numero))
			
			throw new UnsupportedMathOperationException("Please set a numeric value");
		
		if(func.convertToDouble(numero) < 0)
			throw new UnsupportedMathOperationException("Please set a positive numeric value");
		
		
		return sm.raiz(func.convertToDouble(numero));
	}

}
