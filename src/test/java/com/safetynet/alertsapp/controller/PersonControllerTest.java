package com.safetynet.alertsapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Person;
import com.safetynet.alertsapp.service.PersonServiceImpl;

//@WebMvcTest annotation is used for Spring MVC tests. 
//It disables full auto-configuration and instead apply only configuration relevant to MVC tests.
//It auto-configures MockMvc instance as well.
@WebMvcTest(controllers = PersonController.class)  // we are asking to initialize only one web controller
class PersonControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PersonServiceImpl personService;

	private ObjectMapper objectMapper;
	private Person person;

	@BeforeEach
	void setup() {
		objectMapper = new ObjectMapper();
		person = new Person("John", "Doe", "1-1111", 10000, "address", "city", "johndoe@mail.com");
	}


	@Test
	void testPostPerson() throws Exception {
		//Arrange
		Person personToSave = new Person("John", "Doe", "1-1111", 10000, "address", "city", "johndoe@mail.com");
		String jsonContent = objectMapper.writeValueAsString(personToSave);
		when(personService.savePerson(any(Person.class))).thenReturn(person);

		//Act
		MvcResult result = mockMvc
				.perform(post("/person").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
				.andExpect(status().isCreated()).andReturn();

		//Assert
		verify(personService).savePerson(any(Person.class));
		Person personResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Person>() {});
		assertNotNull(personResult);
		assertEquals(personToSave, personResult);
	}

	@Test
	void testPutPerson() throws Exception {
		//Arrange
		Person personToUpdate = new Person("John", "Doe", "1-1111", 10000, "address", "city", "newMail@mail.com");
		String jsonContent = objectMapper.writeValueAsString(personToUpdate);
		Person personUpdated = new Person("John", "Doe", "1-1111", 10000, "address", "city", "newMail@mail.com");
		when(personService.updatePerson(any(Person.class))).thenReturn(personUpdated);

		//Act
		MvcResult result = mockMvc
				.perform(put("/person").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
				.andExpect(status().isOk()).andReturn();

		//Assert
		verify(personService).updatePerson(any(Person.class));
		Person personResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Person>() {});
		assertNotNull(personResult);
		assertEquals(personToUpdate.getEmail(), personResult.getEmail());
	}
	
	@Test
	void testDeletePerson() throws Exception {
		MvcResult result = mockMvc.perform(delete("/person")
				.param("firstname", "John")
				.param("lastname", "Doe"))
				.andExpect(status().isGone()).andReturn();
		verify(personService).deletePerson("John", "Doe");
	}
	
	@Test
	void testDeletePerson_Unknown() throws Exception {
		//Arrange
		doThrow(new BusinessResourceException("DeletePersonError", "Error deleting person: John Unknown", HttpStatus.NOT_FOUND))
		.when(personService).deletePerson("John","Unknown");
		//Act
		MvcResult result = mockMvc.perform(delete("/person")
				.param("firstname", "John")
				.param("lastname", "Unknown"))
				.andExpect(status().isNotFound()).andReturn();
		//Assert
		verify(personService).deletePerson("John", "Unknown");
	}
	

}
