package br.com.acgm.integrationtests.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.acgm.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.acgm.model.Person;
import br.com.acgm.repository.PersonRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest{
	
	@Autowired
	public PersonRepository repository;
	
	private static Person person;
	
	@BeforeAll
	public static void setup() {
		
		person = new Person();
	}
	

	@Test
	@Order(1)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
	
		Pageable pageable = PageRequest.of(0,6,Sort.by(Direction.ASC,"firstName"));
		
		person = repository.findPersonByName("iko", pageable).getContent().get(0);
		
		assertNotNull(person.getLastName());
		assertNotNull(person.getGender());
		assertNotNull(person.getAddress());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getId());
		
		assertTrue(person.getEnabled());
		
		assertEquals(8,person.getId());
		
		assertEquals("tesla",person.getLastName());
		assertEquals("masculino",person.getGender());
		assertEquals("Mvezo africa do sul",person.getAddress());
		assertEquals("Nikola",person.getFirstName());
	
	}
	
	@Test
	@Order(2)
	public void testDisablePerson() throws JsonMappingException, JsonProcessingException {
	
		repository.disablePerson(person.getId());
		
		Pageable pageable = PageRequest.of(0,6,Sort.by(Direction.ASC,"firstName"));
		
		person = repository.findPersonByName("iko", pageable).getContent().get(0);
		
		assertNotNull(person.getLastName());
		assertNotNull(person.getGender());
		assertNotNull(person.getAddress());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getId());
		
		assertFalse(person.getEnabled());
		
		assertEquals(8,person.getId());
		
		assertEquals("tesla",person.getLastName());
		assertEquals("masculino",person.getGender());
		assertEquals("Mvezo africa do sul",person.getAddress());
		assertEquals("Nikola",person.getFirstName());
	
	}

}
