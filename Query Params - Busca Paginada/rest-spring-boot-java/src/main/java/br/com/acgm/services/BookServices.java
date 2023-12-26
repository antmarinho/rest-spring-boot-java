package br.com.acgm.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.acgm.controller.BookController;
import br.com.acgm.data.vo.v1.BookVO;
import br.com.acgm.exceptions.RequiredObjectIsNullException;
import br.com.acgm.exceptions.ResourceNotFoundException;
import br.com.acgm.mapper.DozerMapper;
import br.com.acgm.model.Book;
import br.com.acgm.repository.BookRepository;

@Service //spring instancia o objeto onde for usado
public class BookServices {
	
	private Logger logger = Logger.getLogger(BookServices.class.getName());
	
	@Autowired 
	BookRepository repository;
	
	
	public BookVO findById(Long id) {
		
		logger.info("procurando um livro");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("nao existe regristo"));
		
		var vo = DozerMapper.parseObject(entity, BookVO.class);
		
		vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		
		return vo;
	}
	
	public List<BookVO> findAll() {
		
		logger.info("procurando todos livros");
		
		var books = DozerMapper.parseListObjects(repository.findAll(),BookVO.class);
		
		books.stream().forEach(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel()));
		
		return books;
	}

	
	public BookVO create(BookVO book) {
		
		if(book == null) throw new RequiredObjectIsNullException();
		
		logger.info("criando 1 livro");
		
		var entity = DozerMapper.parseObject(book,Book.class);
		
		var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		
		return vo;
		
	}
	
	public BookVO update(BookVO book) {
		
		if(book == null) throw new RequiredObjectIsNullException();
		
		logger.info("editando 1 livro");
		
		var entity = repository.findById(book.getKey()).orElseThrow(() -> new ResourceNotFoundException("nao existe regristo"));
		
		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		entity.setTitle(book.getTitle());
		
		var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		
		return vo;
	}
	
	public void delete(Long id) {
		
		logger.info("deletando 1 livro");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("nao existe regristo"));
		
		repository.delete(entity);
	}
	
}
