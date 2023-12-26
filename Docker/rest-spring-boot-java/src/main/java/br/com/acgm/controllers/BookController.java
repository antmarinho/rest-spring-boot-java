package br.com.acgm.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.acgm.data.vo.v1.BookVO;
import br.com.acgm.services.BookServices;
import br.com.acgm.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Book", description = "endpoints para classe book")
public class BookController {
	
	@Autowired //para usar com o service para injetar automatico
	private BookServices service;
	//service vai fazer isso
	//private BookServices service = new BookServices(); 
	
	@GetMapping(value = "/{id}",
			produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
	@Operation(summary = "find a book", description = "find a book", 
	   tags = {"Book"/*a msm da tag do controller*/}, 
	   responses = {
			   @ApiResponse(description = "Success", responseCode = "200", 
				   content = @Content(schema = @Schema(implementation = BookVO.class))),
			   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			   @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			   @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			   @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
	public BookVO findById(@PathVariable(value = "id") Long id) {
		
		return service.findById(id);
	}
	
	@GetMapping(
			produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
	@Operation(summary = "finds all books", description = "finds all books", 
			   tags = {"Book"/*a msm da tag do controller*/}, 
			   responses = {
					   @ApiResponse(description = "Success", responseCode = "200", 
							   content = {
									   @Content(
											  /* mediaType = "application/json",*/
											   array = @ArraySchema(schema = @Schema(implementation = BookVO.class))
									   )
							   }),
					   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					   @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					   @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
	public List<BookVO> findAll() {
		
		return service.findAll();
	}
	
	@PostMapping(
			consumes = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YML}, 
			produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YML}
			)
	@Operation(summary = "Add a new book", 
	   description = "Add a new book by passing in a JSON, XML or YML representation of the book!", 
	   tags = {"Book"/*a msm da tag do controller*/}, 
	   responses = {
			   @ApiResponse(description = "Success", responseCode = "200", 
				   content = @Content(schema = @Schema(implementation = BookVO.class))),
			   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			   @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
	public BookVO create(@RequestBody BookVO BookVO) {
		
		return service.create(BookVO);
		
	}
	
	@PutMapping(
			consumes = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YML}, 
			produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
	@Operation(summary = "Updates a book", 
	   description = "Updates a book by passing in a JSON, XML or YML representation of the book!", 
	   tags = {"Book"/*a msm da tag do controller*/}, 
	   responses = {
			   @ApiResponse(description = "Updated", responseCode = "200", 
				   content = @Content(schema = @Schema(implementation = BookVO.class))),
			   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			   @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			   @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)})
	public BookVO update(@RequestBody BookVO BookVO) {
		
		return service.update(BookVO);
		
	}
	
	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletes a book", 
	   description = "Deletes a book by passing in a JSON, XML or YML representation of the book!", 
	   tags = {"Book"/*a msm da tag do controller*/}, 
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
