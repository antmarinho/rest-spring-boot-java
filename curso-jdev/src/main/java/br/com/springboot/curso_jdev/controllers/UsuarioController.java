package br.com.springboot.curso_jdev.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.curso_jdev.model.Usuario;
import br.com.springboot.curso_jdev.repositories.UsuarioRepository;

@RestController
@RequestMapping("/user")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository repository;
	
	@GetMapping("/listatodos")
	public ResponseEntity<List<Usuario>> listarUsuario(){
		
		List<Usuario> users = repository.findAll();
		
		return ResponseEntity.status(HttpStatus.OK).body(users);
		
	}
	
	@GetMapping("/byId")
	public ResponseEntity<Usuario> findById(@RequestParam(name = "id") Long id){
		
		Optional<Usuario> user = repository.findById(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(user.get());
	}
	
	@GetMapping("/buscarNome")
	public ResponseEntity<List<Usuario>> buscarNome(@RequestParam(name = "nome") String nome){
		
		List<Usuario> user = repository.buscarNome(nome.trim().toUpperCase());
		
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	@PostMapping("/salvar")
	public ResponseEntity<Usuario> salvar(@RequestBody Usuario user){
		
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(user));
	}
	
	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizar(@RequestBody Usuario user){
		
		if(user.getId() == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("usuario n existe");
		
		return ResponseEntity.status(HttpStatus.OK).body(repository.saveAndFlush(user));
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deletar(@RequestParam(name = "id") Long id){
		
		repository.deleteById(id);
		
		return ResponseEntity.status(HttpStatus.OK).body("usuario deletado");
		
	}
	
	
	
	
}
