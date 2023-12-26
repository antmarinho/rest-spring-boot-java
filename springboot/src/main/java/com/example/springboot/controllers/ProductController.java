package com.example.springboot.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.model.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class ProductController {
	
	@Autowired
	ProductRepository productRepository;
	
	@PostMapping("/products")
	public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto proddto){
		
		var producModel = new ProductModel();
		
		BeanUtils.copyProperties(proddto, producModel);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(producModel));
	}
	
	@GetMapping("/products")
	public ResponseEntity<List<ProductModel>> getAllProducts(){
		
		List<ProductModel> productList = productRepository.findAll();
		
		if(!productList.isEmpty()) {
			
			for(ProductModel product : productList) {
				
				UUID id = product.getId();
				
				product.add(linkTo(methodOn(ProductController.class).getProducts(id)).withSelfRel());
			}
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(productList);
	}
	
	@GetMapping("/products/{id}")
	public ResponseEntity<Object> getProducts(@PathVariable(value = "id") UUID id){
	
		Optional<ProductModel> productO = productRepository.findById(id);
		
		if(productO.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto nao existe");
		
		productO.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
		
		return ResponseEntity.status(HttpStatus.OK).body(productO.get());
	}
	
	@PutMapping("/products/{id}")
	public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProductRecordDto proddto) {
		
		Optional<ProductModel> productO = productRepository.findById(id);
		
		if(productO.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto nao existe");
		
		var producModel = productO.get();
		
		BeanUtils.copyProperties(proddto, producModel);
		
		return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(producModel));
	}
	
	@DeleteMapping("/products/{id}")
	public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
		
		Optional<ProductModel> productO = productRepository.findById(id);
		
		if(productO.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto nao existe");
		
		productRepository.delete(productO.get());
		
		return ResponseEntity.status(HttpStatus.OK).body("produto deletado");
		
	}

}
