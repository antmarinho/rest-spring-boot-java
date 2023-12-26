package br.com.acgm.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.acgm.data.vo.v1.PersonVO;
import br.com.acgm.data.vo.v2.PersonVOV2;
import br.com.acgm.services.PersonServices;
import br.com.acgm.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

//@CrossOrigin especifico acesso pra todos
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "endpoints para classe people")
public class PersonController {
	
	@Autowired //para usar com o service para injetar automatico
	private PersonServices service;
	//service vai fazer isso
	//private PersonServices service = new PersonServices(); 
	
	@CrossOrigin(origins = "http://localhost:8080") //apenas para localhost
	@GetMapping(value = "/{id}",
			produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
	@Operation(summary = "find a person", description = "find a person", 
	   tags = {"People"/*a msm da tag do controller*/}, 
	   responses = {
			   @ApiResponse(description = "Success", responseCode = "200", 
				   content = @Content(schema = @Schema(implementation = PersonVO.class))),
			   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			   @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			   @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			   @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
	public PersonVO findById(@PathVariable(value = "id") Long id) {
		
		return service.findById(id);
	}
	
	@GetMapping(
			produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
	@Operation(summary = "finds all people", description = "finds all people", 
			   tags = {"People"/*a msm da tag do controller*/}, 
			   responses = {
					   @ApiResponse(description = "Success", responseCode = "200", 
							   content = {
									   @Content(
											  /* mediaType = "application/json",*/
											   array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
									   )
							   }),
					   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					   @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					   @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
	public List<PersonVO> findAll() {
		
		return service.findAll();
	}
	
	@CrossOrigin(origins = {"http://localhost:8080", "https://erudio.com.br"}) //permisao pra executar o comando
	@PostMapping(
			consumes = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YML}, 
			produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YML}
			)
	@Operation(summary = "Add a new person", 
	   description = "Add a new person by passing in a JSON, XML or YML representation of the person!", 
	   tags = {"People"/*a msm da tag do controller*/}, 
	   responses = {
			   @ApiResponse(description = "Success", responseCode = "200", 
				   content = @Content(schema = @Schema(implementation = PersonVO.class))),
			   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			   @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
	public PersonVO create(@RequestBody PersonVO personVO) {
		
		return service.create(personVO);
		
	}
	
	@PostMapping(value = "/v2", 
			consumes = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YML}, 
			produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
	public PersonVOV2 createV2(@RequestBody PersonVOV2 personVOV2) {
		
		return service.createV2(personVOV2);
		
	}
	
	@PutMapping(
			consumes = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YML}, 
			produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
	@Operation(summary = "Updates a person", 
	   description = "Updates a person by passing in a JSON, XML or YML representation of the person!", 
	   tags = {"People"/*a msm da tag do controller*/}, 
	   responses = {
			   @ApiResponse(description = "Updated", responseCode = "200", 
				   content = @Content(schema = @Schema(implementation = PersonVO.class))),
			   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			   @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			   @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
	public PersonVO update(@RequestBody PersonVO personVO) {
		
		return service.update(personVO);
		
	}
	
	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletes a person", 
	   description = "Deletes a person by passing in a JSON, XML or YML representation of the person!", 
	   tags = {"People"/*a msm da tag do controller*/}, 
	   responses = {
			   @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			   @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			   @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
}
