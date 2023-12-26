/*package br.com.acgm.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.acgm.data.vo.v1.BookVO;
import br.com.acgm.exceptions.RequiredObjectIsNullException;
import br.com.acgm.model.Book;
import br.com.acgm.repository.BookRepository;
import br.com.acgm.services.BookServices;
import br.com.acgm.unittests.mapper.mocks.MockBook;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {

	MockBook input;
	
	@InjectMocks
	private BookServices service;
	
	@Mock
	BookRepository repository;
	
	@BeforeEach
	void setUpMocks() throws Exception {
		
		input = new MockBook();
		
		MockitoAnnotations.openMocks(this);

	}

	@Test
	void testFindById() {
		
		Book entity = input.mockEntity(1);
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		var result = service.findById(1L);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		System.out.println(result.toString());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Some Author1", result.getAuthor());
		assertEquals("Some Title1", result.getTitle());
		assertEquals(25D, result.getPrice());
		assertNotNull(result.getLaunchDate());
		
	}

	@Test
	void testFindAll() {
		
		List<Book> list = input.mockEntityList();
		
		when(repository.findAll()).thenReturn(list);
		
		var people = service.findAll();
		
		assertNotNull(people);
		assertEquals(14,people.size());
		
		var result = people.get(1);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		System.out.println(result.toString());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Some Author1", result.getAuthor());
		assertEquals("Some Title1", result.getTitle());
		assertEquals(25D, result.getPrice());
		assertNotNull(result.getLaunchDate());
		
		var result4 = people.get(4);
		
		assertNotNull(result4);
		assertNotNull(result4.getKey());
		assertNotNull(result4.getLinks());
		System.out.println(result4.toString());
		assertTrue(result4.toString().contains("links: [</api/book/v1/4>;rel=\"self\"]"));
		assertEquals("Some Author4", result4.getAuthor());
		assertEquals("Some Title4", result4.getTitle());
		assertEquals(25D, result4.getPrice());
		assertNotNull(result4.getLaunchDate());
		
		var result7 = people.get(7);
		
		assertNotNull(result7);
		assertNotNull(result7.getKey());
		assertNotNull(result7.getLinks());
		System.out.println(result7.toString());
		assertTrue(result7.toString().contains("links: [</api/book/v1/7>;rel=\"self\"]"));
		assertEquals("Some Author7", result7.getAuthor());
		assertEquals("Some Title7", result7.getTitle());
		assertEquals(25D, result7.getPrice());
		assertNotNull(result7.getLaunchDate());
		
	}

	@Test
	void testCreate() {
		
		Book entity = input.mockEntity(1);
		Book persisted = entity;
		
		persisted.setId(1L);

		BookVO vo = input.mockVO(1);
		vo.setKey(1L);
		
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.create(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Some Author1", result.getAuthor());
		assertEquals("Some Title1", result.getTitle());
		assertEquals(25D, result.getPrice());
		assertNotNull(result.getLaunchDate());
		
	}
	
	@Test
	void testCreateWithNullBook() {
		
		Exception exception = assertThrows(RequiredObjectIsNullException.class, ()->{
			service.create(null);
		});
		
		String expectedMessage = "nao e posisvel persistir um objeto nulo";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
		
	}
	
	@Test
	void testUpdateWithNullBook() {
		
		Exception exception = assertThrows(RequiredObjectIsNullException.class, ()->{
			service.update(null);
		});
		
		String expectedMessage = "nao e posisvel persistir um objeto nulo";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
		
	}

	@Test
	void testUpdate() {
		
		Book entity = input.mockEntity(1);
		
		entity.setId(1L);
		
		Book persisted = entity;
		
		persisted.setId(1L);

		BookVO vo = input.mockVO(1);
		vo.setKey(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.update(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Some Author1", result.getAuthor());
		assertEquals("Some Title1", result.getTitle());
		assertEquals(25D, result.getPrice());
		assertNotNull(result.getLaunchDate());
		
	}

	@Test
	void testDelete() {
		
		Book entity = input.mockEntity(1);
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		service.delete(1L);
		
	}


}*/
