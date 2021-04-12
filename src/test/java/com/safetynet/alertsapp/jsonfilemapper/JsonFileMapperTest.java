package com.safetynet.alertsapp.jsonfilemapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.model.Person;

@ExtendWith(MockitoExtension.class)
class JsonFileMapperTest {

	private final Logger logger = LoggerFactory.getLogger(JsonFileMapperTest.class);

	@InjectMocks
	JsonFileMapper jsonFileMapperCUT;

	@Mock
	ObjectMapper objectMapperMock;

	@Test
	void testMapObject_2firestations_real() {
		try {
			//Arrange
			String jsonTestString = 
					"{"
							+ "\"firestations\": [\r\n"
							+ "		{ \"address\":\"1509 Culver St\", \"station\":\"3\" },\r\n"
							+ "		{ \"address\":\"29 15th St\", \"station\":\"2\" }\r\n"
							+ "	]}";
			//Calling a real ObjectMapper to get JSONNode from the test String:
			JsonNode jsonNodeTest = new ObjectMapper().readTree(jsonTestString);
			logger.debug("jsonNodeTest={}",jsonNodeTest);
			when(objectMapperMock.readTree(any(File.class))).thenReturn(jsonNodeTest);

			//Act
			List<Firestation> objectList = jsonFileMapperCUT.map(new File(""), "firestations", new TypeReference<List<Firestation>>(){});

			//Assert
			assertEquals(2,objectList.size(),"Expected list size is 2");
			assertEquals("1509 Culver St",objectList.get(0).getAddress(),"Address value must be same as in jsonTestString");
			assertEquals(3,objectList.get(0).getStation(),"firestation number value must be same as in jsonTestString");
			assertEquals("29 15th St", objectList.get(1).getAddress(), "Address value must be same as in jsonTestString");
			assertEquals(2, objectList.get(1).getStation(),"firestation number value must be same as in jsonTestString");

		} catch (IOException e) {
			logger.warn("objectMapperMock.readTree has failed!!!");
			e.printStackTrace();
		}
	}

	@Test
	void testMapObject_2persons() {
		try {
			//Arrange
			String jsonTestString = 
					"{"
							+ "\"persons\": [\r\n"
							+ "		{ \"firstName\":\"John\", \"lastName\":\"Boyd\", \"address\":\"1509 Culver St\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-6512\", \"email\":\"jaboyd@email.com\" },\r\n"
							+ "		{ \"firstName\":\"Jacob\", \"lastName\":\"Boyd\", \"address\":\"1509 Culver St\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-6513\", \"email\":\"drk@email.com\" }"
							+ "	]}";
			//Calling a real ObjectMapper to get JSONNode from the test String:
			JsonNode jsonNodeTest = new ObjectMapper().readTree(jsonTestString);
			logger.debug("jsonNodeTest={}",jsonNodeTest);
			when(objectMapperMock.readTree(any(File.class))).thenReturn(jsonNodeTest);

			//Act
			List<Person> objectList = jsonFileMapperCUT.map(new File(""), "persons", new TypeReference<List<Person>>(){});

			//Assert
			assertEquals(2,objectList.size(),"Expected list size is 2");
			assertEquals("John",objectList.get(0).getFirstName(),"FirstName value must be same as in jsonTestString");
			assertEquals("Boyd",objectList.get(0).getLastName(),"LastName value must be same as in jsonTestString");
			assertEquals("1509 Culver St",objectList.get(0).getAddress(),"LastName value must be same as in jsonTestString");
			assertEquals(97451,objectList.get(0).getZip(),"Zip value must be same as in jsonTestString");
			assertEquals("841-874-6512",objectList.get(0).getPhone(),"Phone value must be same as in jsonTestString");
			assertEquals("jaboyd@email.com",objectList.get(0).getEmail(),"Email value must be same as in jsonTestString");

		} catch (IOException e) {
			logger.warn("objectMapperMock.readTree has failed!!!");
			e.printStackTrace();
		}
	}

	@Test
	void testMapObject_3medicalrecords() {
		try {
			//Arrange
			String jsonTestString = 
					"{"
							+ "\"medicalrecords\": [\r\n"
							+ "		{ \"firstName\":\"John\", \"lastName\":\"Boyd\", \"birthdate\":\"03/06/1984\", \"medications\":[\"aznol:350mg\", \"hydrapermazol:100mg\"], \"allergies\":[\"nillacilan\"] },\r\n"
							+ "		{ \"firstName\":\"Jacob\", \"lastName\":\"Boyd\", \"birthdate\":\"03/06/1989\", \"medications\":[\"pharmacol:5000mg\", \"terazine:10mg\", \"noznazol:250mg\"], \"allergies\":[] },\r\n"
							+ "		{ \"firstName\":\"Tenley\", \"lastName\":\"Boyd\", \"birthdate\":\"02/18/2012\", \"medications\":[], \"allergies\":[\"peanut\"] }"
							+ "]}";
			//Calling a real ObjectMapper to get JSONNode from the test String:
			JsonNode jsonNodeTest = new ObjectMapper().readTree(jsonTestString);
			logger.debug("jsonNodeTest={}",jsonNodeTest);
			when(objectMapperMock.readTree(any(File.class))).thenReturn(jsonNodeTest);

			//Act
			List<Medicalrecord> objectList = jsonFileMapperCUT.map(new File(""), "medicalrecords", new TypeReference<List<Medicalrecord>>(){});

			//Assert
			assertEquals(3,objectList.size(),"Expected list size is 2");
			assertEquals("John",objectList.get(0).getFirstName(),"FirstName value must be same as in jsonTestString");
			assertEquals("Boyd",objectList.get(0).getLastName(),"LastName value must be same as in jsonTestString");
			assertEquals("03/06/1984",objectList.get(0).getBirthdate(),"Birthdate value must be same as in jsonTestString");
			assertEquals(Arrays.asList("aznol:350mg","hydrapermazol:100mg"),objectList.get(0).getMedications(),"Medications value must be same as in jsonTestString");
			assertEquals(Arrays.asList("nillacilan"),objectList.get(0).getAllergies(),"Allergies value must be same as in jsonTestString");

		} catch (IOException e) {
			logger.warn("objectMapperMock.readTree has failed!!!");
			e.printStackTrace();
		}
	}
}
