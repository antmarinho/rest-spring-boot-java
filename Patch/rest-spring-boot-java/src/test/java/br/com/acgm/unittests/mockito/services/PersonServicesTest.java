package br.com.acgm.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.*;
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

import br.com.acgm.data.vo.v1.PersonVO;
import br.com.acgm.exceptions.RequiredObjectIsNullException;
import br.com.acgm.model.Person;
import br.com.acgm.repository.PersonRepository;
import br.com.acgm.services.PersonServices;
import br.com.acgm.unittests.mapper.mocks.MockPerson;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

	MockPerson input;
	
	@InjectMocks
	private PersonServices service;
	
	@Mock
	PersonRepository repository;
	
	@BeforeEach
	void setUpMocks() throws Exception {
		
		input = new MockPerson();
		
		MockitoAnnotations.openMocks(this);

	}

	@Test
	void testFindById() {
		
		Person entity = input.mockEntity(1);
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		var result = service.findById(1L);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getLinks());
		System.out.println(result.toString());
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
		
	}

	@Test
	void testFindAll() {
		
		List<Person> list = input.mockEntityList();
		
		when(repository.findAll()).thenReturn(list);
		
		var people = service.findAll();
		
		assertNotNull(people);
		assertEquals(14,people.size());
		
		var result = people.get(1);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getLinks());
		System.out.println(result.toString());
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
		
		var result4 = people.get(4);
		
		assertNotNull(result4);
		assertNotNull(result4.getId());
		assertNotNull(result4.getLinks());
		System.out.println(result4.toString());
		assertTrue(result4.toString().contains("links: [</api/person/v1/4>;rel=\"self\"]"));
		assertEquals("Addres Test4", result4.getAddress());
		assertEquals("First Name Test4", result4.getFirstName());
		assertEquals("Last Name Test4", result4.getLastName());
		assertEquals("Male", result4.getGender());
		
		var result7 = people.get(7);
		
		assertNotNull(result7);
		assertNotNull(result7.getId());
		assertNotNull(result7.getLinks());
		System.out.println(result7.toString());
		assertTrue(result7.toString().contains("links: [</api/person/v1/7>;rel=\"self\"]"));
		assertEquals("Addres Test7", result7.getAddress());
		assertEquals("First Name Test7", result7.getFirstName());
		assertEquals("Last Name Test7", result7.getLastName());
		assertEquals("Female", result7.getGender());
		
	}

	@Test
	void testCreate() {
		
		Person entity = input.mockEntity(1);
		Person persisted = entity;
		
		persisted.setId(1L);

		PersonVO vo = input.mockVO(1);
		vo.setId(1L);
		
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.create(vo);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
		
	}
	
	@Test
	void testCreateWithNullPerson() {
		
		Exception exception = assertThrows(RequiredObjectIsNullException.class, ()->{
			service.create(null);
		});
		
		String expectedMessage = "nao e posisvel persistir um objeto nulo";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
		
	}
	
	@Test
	void testUpdateWithNullPerson() {
		
		Exception exception = assertThrows(RequiredObjectIsNullException.class, ()->{
			service.update(null);
		});
		
		String expectedMessage = "nao e posisvel persistir um objeto nulo";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
		
	}

	@Test
	void testUpdate() {
		
		Person entity = input.mockEntity(1);
		
		entity.setId(1L);
		
		Person persisted = entity;
		
		persisted.setId(1L);

		PersonVO vo = input.mockVO(1);
		vo.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.update(vo);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
		
	}

	@Test
	void testDelete() {
		
		Person entity = input.mockEntity(1);
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		service.delete(1L);
		
	}


}
