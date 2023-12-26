package br.com.acgm.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.acgm.configs.TestConfigs;
import br.com.acgm.data.vo.security.TokenVO;
import br.com.acgm.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.acgm.integrationtests.vo.AccountCredentialsVO;
import br.com.acgm.integrationtests.vo.PersonVO;
import br.com.acgm.integrationtests.vo.Wrappers.WrapperPersonVO;
import br.com.acgm.integrationtests.vo.pagedmodels.PagedModelPerson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest{

	private static RequestSpecification specification;
	private static XmlMapper objectMapper;
	
	private static PersonVO person;
	
	@BeforeAll
	public static void setup() {
		
		objectMapper = new XmlMapper();
		
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		person = new PersonVO();
		
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		var accessToken = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
					.accept(TestConfigs.CONTENT_TYPE_XML)
				.body(user)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
								.body()
									.as(TokenVO.class)
								.getAccessToken();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION,"Bearer " + accessToken)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
	}
	
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		
		mockPerson();
		
		var content = given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_XML)
						.accept(TestConfigs.CONTENT_TYPE_XML)
							.body(person)
							.when()
							.post()
						.then()
							.statusCode(200)
								.extract()
									.body()
										.asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content,PersonVO.class);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getlast_name());
		assertNotNull(persistedPerson.getGender());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getfirst_name());
		assertNotNull(persistedPerson.getId());
		assertTrue(persistedPerson.getEnabled());
		
		assertTrue(persistedPerson.getId() > 0);
		
		assertEquals("stallman",persistedPerson.getlast_name());
		assertEquals("male",persistedPerson.getGender());
		assertEquals("new york",persistedPerson.getAddress());
		assertEquals("richard",persistedPerson.getfirst_name());
		
	}
	
	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		
		mockPerson();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.header(TestConfigs.HEADER_PARAM_ORIGIN,TestConfigs.ORIGIN_ERUDIO)
							.pathParam("id", person.getId())
							.when()
							.get("{id}")
						.then()
							.statusCode(200)
								.extract()
									.body()
										.asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content,PersonVO.class);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getlast_name());
		assertNotNull(persistedPerson.getGender());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getfirst_name());
		assertNotNull(persistedPerson.getId());
		assertFalse(persistedPerson.getEnabled());
		
		assertEquals(person.getId(), persistedPerson.getId());
		
		assertEquals("stallman",persistedPerson.getlast_name());
		assertEquals("male",persistedPerson.getGender());
		assertEquals("new york",persistedPerson.getAddress());
		assertEquals("richard",persistedPerson.getfirst_name());
		
	}
	
	@Test
	@Order(2)
	public void testDisablePersonById() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.header(TestConfigs.HEADER_PARAM_ORIGIN,TestConfigs.ORIGIN_ERUDIO)
							.pathParam("id", person.getId())
							.when()
							.patch("{id}")
						.then()
							.statusCode(200)
								.extract()
									.body()
										.asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content,PersonVO.class);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getlast_name());
		assertNotNull(persistedPerson.getGender());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getfirst_name());
		assertNotNull(persistedPerson.getId());
		assertFalse(persistedPerson.getEnabled());
		
		assertEquals(person.getId(), persistedPerson.getId());
		
		assertEquals("stallman",persistedPerson.getlast_name());
		assertEquals("male",persistedPerson.getGender());
		assertEquals("new york",persistedPerson.getAddress());
		assertEquals("richard",persistedPerson.getfirst_name());
		
	}
	
	@Test
	@Order(4)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
	
		person.setlast_name("stallman a");
		
		var content = given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_XML)
						.accept(TestConfigs.CONTENT_TYPE_XML)
							.body(person)
							.when()
							.post()
						.then()
							.statusCode(200)
								.extract()
									.body()
										.asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content,PersonVO.class);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getlast_name());
		assertNotNull(persistedPerson.getGender());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getfirst_name());
		assertNotNull(persistedPerson.getId());
		assertFalse(persistedPerson.getEnabled());
		
		assertEquals(person.getId(), persistedPerson.getId());
		
		assertEquals("stallman a",persistedPerson.getlast_name());
		assertEquals("male",persistedPerson.getGender());
		assertEquals("new york",persistedPerson.getAddress());
		assertEquals("richard",persistedPerson.getfirst_name());
	
	}
	
	@Test
	@Order(5)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		
		 given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
							.pathParam("id", person.getId())
							.when()
							.delete("{id}")
						.then()
							.statusCode(204);
		
		
	}
	
	@Test
	@Order(6)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
	
	
		var content = given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_XML)
						.accept(TestConfigs.CONTENT_TYPE_XML)
						.queryParams("page",3,"size",10,"direction","asc")
							.when()
							.get()
						.then()
							.statusCode(200)
								.extract()
									.body()
										.asString();
		
		PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);

		var people = wrapper.getContent();

		PersonVO foundPersonOne = people.get(0);
		
		assertNotNull(foundPersonOne.getlast_name());
		assertNotNull(foundPersonOne.getGender());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getfirst_name());
		assertNotNull(foundPersonOne.getId());
		
		assertTrue(foundPersonOne.getEnabled());
		
		assertEquals(675,foundPersonOne.getId());
		
		assertEquals("Terbrug",foundPersonOne.getlast_name());
		assertEquals("Male",foundPersonOne.getGender());
		assertEquals("3 Eagle Crest Court",foundPersonOne.getAddress());
		assertEquals("Alic",foundPersonOne.getfirst_name());
		
		PersonVO foundPersonSix = people.get(4);
		
		assertNotNull(foundPersonSix.getlast_name());
		assertNotNull(foundPersonSix.getGender());
		assertNotNull(foundPersonSix.getAddress());
		assertNotNull(foundPersonSix.getfirst_name());
		assertNotNull(foundPersonSix.getId());
		
		assertFalse(foundPersonSix.getEnabled());
		
		assertEquals(712,foundPersonSix.getId());
		
		assertEquals("Astall",foundPersonSix.getlast_name());
		assertEquals("Female",foundPersonSix.getGender());
		assertEquals("72525 Emmet Alley",foundPersonSix.getAddress());
		assertEquals("Alla",foundPersonSix.getfirst_name());
	
	}
	
	@Test
	@Order(7)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
	
		var content = given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_XML)
						.accept(TestConfigs.CONTENT_TYPE_XML)
						.pathParam("firstName", "iko")
						.queryParams("page",0,"size",6,"direction","asc")
							.when()
							.get("findPersonByName/{firstName}")
						.then()
							.statusCode(200)
								.extract()
									.body()
										.asString();
		
		PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);

		var people = wrapper.getContent();

		PersonVO foundPersonOne = people.get(0);
		
		assertNotNull(foundPersonOne.getlast_name());
		assertNotNull(foundPersonOne.getGender());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getfirst_name());
		assertNotNull(foundPersonOne.getId());
		
		assertTrue(foundPersonOne.getEnabled());
		
		assertEquals(8,foundPersonOne.getId());
		
		assertEquals("tesla",foundPersonOne.getlast_name());
		assertEquals("masculino",foundPersonOne.getGender());
		assertEquals("Mvezo africa do sul",foundPersonOne.getAddress());
		assertEquals("Nikola",foundPersonOne.getfirst_name());

	}
	
	@Test
	@Order(8)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
	
	
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();;
		
		given().spec(specificationWithoutToken)
						.contentType(TestConfigs.CONTENT_TYPE_XML)
						.accept(TestConfigs.CONTENT_TYPE_XML)
							.when()
							.get()
						.then()
							.statusCode(403);
	
	}

	@Test
	@Order(9)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {
	
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.queryParams("page",3,"size",10,"direction","asc")
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.asString();
		
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/712</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/684</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/795</href></links>"));
		
		assertTrue(content.contains("<links><rel>first</rel><href>http://localhost:8888/api/person/v1?direction=asc&amp;page=0&amp;size=10&amp;sort=firstName,asc</href></links>"));
		assertTrue(content.contains("<links><rel>prev</rel><href>http://localhost:8888/api/person/v1?direction=asc&amp;page=2&amp;size=10&amp;sort=firstName,asc</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1?page=3&amp;size=10&amp;direction=asc</href></links>"));
		assertTrue(content.contains("<links><rel>next</rel><href>http://localhost:8888/api/person/v1?direction=asc&amp;page=4&amp;size=10&amp;sort=firstName,asc</href></links>"));
		assertTrue(content.contains("<links><rel>last</rel><href>http://localhost:8888/api/person/v1?direction=asc&amp;page=100&amp;size=10&amp;sort=firstName,asc</href></links>"));
		assertTrue(content.contains("<page><size>10</size><totalElements>1006</totalElements><totalPages>101</totalPages><number>3</number></page>"));
		
	}

	private void mockPerson() {
		
		person.setfirst_name("richard");
		person.setlast_name("stallman");
		person.setAddress("new york");
		person.setGender("male");
		person.setEnabled(true);
		
	}

}
