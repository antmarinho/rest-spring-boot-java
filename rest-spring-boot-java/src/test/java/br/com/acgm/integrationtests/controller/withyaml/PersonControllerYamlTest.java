package br.com.acgm.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.acgm.configs.TestConfigs;
import br.com.acgm.data.vo.security.TokenVO;
import br.com.acgm.integrationtests.controller.withyaml.mapper.YMLMapper;
import br.com.acgm.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.acgm.integrationtests.vo.AccountCredentialsVO;
import br.com.acgm.integrationtests.vo.PersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest{

	private static RequestSpecification specification;
	private static YMLMapper objectMapper;
	
	private static PersonVO person;
	
	@BeforeAll
	public static void setup() {
		
		objectMapper = new YMLMapper();
		
		person = new PersonVO();
		
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		var accessToken = given()
				.config(RestAssuredConfig.config()
						.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_YML)
					.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(user, objectMapper)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
								.body()
									.as(TokenVO.class, objectMapper)
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
		
		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig.config()
						.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.accept(TestConfigs.CONTENT_TYPE_YML)
							.body(person, objectMapper)
							.when()
							.post()
						.then()
							.statusCode(200)
								.extract()
									.body()
										.as(PersonVO.class,objectMapper);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getlast_name());
		assertNotNull(persistedPerson.getGender());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getfirst_name());
		assertNotNull(persistedPerson.getId());
		
		assertTrue(persistedPerson.getId() > 0);
		
		assertEquals("stallman",persistedPerson.getlast_name());
		assertEquals("male",persistedPerson.getGender());
		assertEquals("new york",persistedPerson.getAddress());
		assertEquals("richard",persistedPerson.getfirst_name());
		
	}
	
	@Test
	@Order(2)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		
		mockPerson();
		
		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig.config()
						.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.header(TestConfigs.HEADER_PARAM_ORIGIN,TestConfigs.ORIGIN_ERUDIO)
							.pathParam("id", person.getId())
							.when()
							.get("{id}")
						.then()
							.statusCode(200)
								.extract()
								.body()
								.as(PersonVO.class,objectMapper);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getlast_name());
		assertNotNull(persistedPerson.getGender());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getfirst_name());
		assertNotNull(persistedPerson.getId());
		
		assertEquals(person.getId(), persistedPerson.getId());
		
		assertEquals("stallman",persistedPerson.getlast_name());
		assertEquals("male",persistedPerson.getGender());
		assertEquals("new york",persistedPerson.getAddress());
		assertEquals("richard",persistedPerson.getfirst_name());
		
	}
	
	@Test
	@Order(3)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
	
		person.setlast_name("stallman a");
		
		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig.config()
						.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.accept(TestConfigs.CONTENT_TYPE_YML)
							.body(person,objectMapper)
							.when()
							.post()
						.then()
							.statusCode(200)
								.extract()
								.body()
								.as(PersonVO.class,objectMapper);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getlast_name());
		assertNotNull(persistedPerson.getGender());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getfirst_name());
		assertNotNull(persistedPerson.getId());
		
		assertEquals(person.getId(), persistedPerson.getId());
		
		assertEquals("stallman a",persistedPerson.getlast_name());
		assertEquals("male",persistedPerson.getGender());
		assertEquals("new york",persistedPerson.getAddress());
		assertEquals("richard",persistedPerson.getfirst_name());
	
	}
	
	@Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		
		 given().spec(specification)
		 .config(RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
							.pathParam("id", person.getId())
							.when()
							.delete("{id}")
						.then()
							.statusCode(204);
		
		
	}
	
	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
	
	
	
		var content = given().spec(specification)
				.config(RestAssuredConfig.config()
						.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.accept(TestConfigs.CONTENT_TYPE_YML)
							.when()
							.get()
						.then()
							.statusCode(200)
								.extract()
								.body()
								.as(PersonVO[].class,objectMapper);
		
		List<PersonVO> people = Arrays.asList(content);

		
		PersonVO foundPersonOne = people.get(0);
		
		assertNotNull(foundPersonOne.getlast_name());
		assertNotNull(foundPersonOne.getGender());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getfirst_name());
		assertNotNull(foundPersonOne.getId());
		
		assertEquals(1,foundPersonOne.getId());
		
		assertEquals("silva",foundPersonOne.getlast_name());
		assertEquals("masculino",foundPersonOne.getGender());
		assertEquals("sao paulo brasil",foundPersonOne.getAddress());
		assertEquals("jose",foundPersonOne.getfirst_name());
		
		PersonVO foundPersonSix = people.get(4);
		
		assertNotNull(foundPersonSix.getlast_name());
		assertNotNull(foundPersonSix.getGender());
		assertNotNull(foundPersonSix.getAddress());
		assertNotNull(foundPersonSix.getfirst_name());
		assertNotNull(foundPersonSix.getId());
		
		assertEquals(8,foundPersonSix.getId());
		
		assertEquals("tesla",foundPersonSix.getlast_name());
		assertEquals("masculino",foundPersonSix.getGender());
		assertEquals("Mvezo africa do sul",foundPersonSix.getAddress());
		assertEquals("Nikola",foundPersonSix.getfirst_name());
	
	}
	
	@Test
	@Order(6)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
	
	
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();;
		
		given().spec(specificationWithoutToken)
		.config(RestAssuredConfig.config()
				.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.accept(TestConfigs.CONTENT_TYPE_YML)
							.when()
							.get()
						.then()
							.statusCode(403);
	
	}


	private void mockPerson() {
		
		person.setfirst_name("richard");
		person.setlast_name("stallman");
		person.setAddress("new york");
		person.setGender("male");
		
	}

}
