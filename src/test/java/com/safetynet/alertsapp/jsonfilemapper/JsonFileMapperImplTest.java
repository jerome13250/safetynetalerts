package com.safetynet.alertsapp.jsonfilemapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.model.Person;

@ExtendWith(MockitoExtension.class)
class JsonFileMapperImplTest {

	private final Logger logger = LoggerFactory.getLogger(JsonFileMapperImplTest.class);

	String jsonTestString;

	@InjectMocks
	JsonFileMapperImpl jsonFileMapperCUT;

	@Mock
	ObjectMapper objectMapperMock;

	@BeforeEach
	void init() {
		jsonTestString = 
				"{"
						+ "\"firestations\": [\r\n"
						+ "		{ \"address\":\"1509 Culver St\", \"station\":\"3\" },\r\n"
						+ "		{ \"address\":\"29 15th St\", \"station\":\"2\" }],\r\n"
						+ "\"medicalrecords\": [\r\n"
						+ "		{ \"firstName\":\"John\", \"lastName\":\"Boyd\", \"birthdate\":\"03/06/1984\", \"medications\":[\"aznol:350mg\", \"hydrapermazol:100mg\"], \"allergies\":[\"nillacilan\"] },\r\n"
						+ "		{ \"firstName\":\"Jacob\", \"lastName\":\"Boyd\", \"birthdate\":\"03/06/1989\", \"medications\":[\"pharmacol:5000mg\", \"terazine:10mg\", \"noznazol:250mg\"], \"allergies\":[] },\r\n"
						+ "		{ \"firstName\":\"Tenley\", \"lastName\":\"Boyd\", \"birthdate\":\"02/18/2012\", \"medications\":[], \"allergies\":[\"peanut\"] }],"
						+ "\"persons\": [\r\n"
						+ "		{ \"firstName\":\"John\", \"lastName\":\"Boyd\", \"address\":\"1509 Culver St\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-6512\", \"email\":\"jaboyd@email.com\" },\r\n"
						+ "		{ \"firstName\":\"Jacob\", \"lastName\":\"Boyd\", \"address\":\"1509 Culver St\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-6513\", \"email\":\"drk@email.com\" }"
						+ "	]}";
	}


	@Test
	@DisplayName("2 objects Firestation are processed")
	void testDeserializeObject_2firestations()  throws Exception {
		//ARRANGE
		//Calling a real ObjectMapper to get JSONNode from the test String:
		JsonNode jsonNodeTest = new ObjectMapper().readTree(jsonTestString);
		logger.debug("jsonNodeTest={}",jsonNodeTest);
		when(objectMapperMock.readTree(any(File.class))).thenReturn(jsonNodeTest);

		//ACT
		List<Firestation> objectList = jsonFileMapperCUT.deserialize(new File(""), "firestations", Firestation.class);

		//ASSERT
		assertEquals(2,objectList.size(),"Expected list size is 2");
		assertEquals("1509 Culver St",objectList.get(0).getAddress(),"Address value must be same as in jsonTestString");
		assertEquals(3,objectList.get(0).getStation(),"firestation number value must be same as in jsonTestString");
		assertEquals("29 15th St", objectList.get(1).getAddress(), "Address value must be same as in jsonTestString");
		assertEquals(2, objectList.get(1).getStation(),"firestation number value must be same as in jsonTestString");
	}

	@Test
	@DisplayName("2 objects Person are processed")
	void testDeserializeObject_2persons()  throws Exception {
		//ARRANGE
		//Calling a real ObjectMapper to get JSONNode from the test String:
		JsonNode jsonNodeTest = new ObjectMapper().readTree(jsonTestString);
		logger.debug("jsonNodeTest={}",jsonNodeTest);
		when(objectMapperMock.readTree(any(File.class))).thenReturn(jsonNodeTest);

		//ACT
		List<Person> objectList = jsonFileMapperCUT.deserialize(new File(""), "persons", Person.class);

		//ASSERT
		assertEquals(2,objectList.size(),"Expected list size is 2");
		assertEquals("John",objectList.get(0).getFirstName(),"FirstName value must be same as in jsonTestString");
		assertEquals("Boyd",objectList.get(0).getLastName(),"LastName value must be same as in jsonTestString");
		assertEquals("1509 Culver St",objectList.get(0).getAddress(),"LastName value must be same as in jsonTestString");
		assertEquals(97451,objectList.get(0).getZip(),"Zip value must be same as in jsonTestString");
		assertEquals("841-874-6512",objectList.get(0).getPhone(),"Phone value must be same as in jsonTestString");
		assertEquals("jaboyd@email.com",objectList.get(0).getEmail(),"Email value must be same as in jsonTestString");

	}

	@Test
	@DisplayName("3 objects Medicalrecord are processed")
	void testDeserializeObject_3medicalrecords()  throws Exception {
		//ARRANGE
		//Calling a real ObjectMapper to get JSONNode from the test String:
		JsonNode jsonNodeTest = new ObjectMapper().readTree(jsonTestString);
		logger.debug("jsonNodeTest={}",jsonNodeTest);
		when(objectMapperMock.readTree(any(File.class))).thenReturn(jsonNodeTest);

		//ACT
		List<Medicalrecord> objectList = jsonFileMapperCUT.deserialize(new File(""), "medicalrecords", Medicalrecord.class);

		//ASSERT
		assertEquals(3,objectList.size(),"Expected list size is 2");
		assertEquals("John",objectList.get(0).getFirstName(),"FirstName value must be same as in jsonTestString");
		assertEquals("Boyd",objectList.get(0).getLastName(),"LastName value must be same as in jsonTestString");
		//creating expected date 
		LocalDate dateMarch6th1984 = LocalDate.of(1984, 3, 6);

		assertEquals(dateMarch6th1984,objectList.get(0).getBirthdate(),"Birthdate value must be same as in jsonTestString");
		assertEquals(Arrays.asList("aznol:350mg","hydrapermazol:100mg"),objectList.get(0).getMedications(),"Medications value must be same as in jsonTestString");
		assertEquals(Arrays.asList("nillacilan"),objectList.get(0).getAllergies(),"Allergies value must be same as in jsonTestString");

	}

	@Test
	@DisplayName("Empty json Array must return an empty List")
	void testDeserializeObject_emptyJsonArray()  throws Exception {
		//ARRANGE
		jsonTestString = 
				"{"
						+ "\"medicalrecords\": [\r\n"
						+ "]}";
		//Calling a real ObjectMapper to get JSONNode from the test String:
		JsonNode jsonNodeTest = new ObjectMapper().readTree(jsonTestString);
		logger.debug("jsonNodeTest={}",jsonNodeTest);
		when(objectMapperMock.readTree(any(File.class))).thenReturn(jsonNodeTest);

		//ACT
		List<Medicalrecord> objectList = jsonFileMapperCUT.deserialize(new File(""), "medicalrecords", Medicalrecord.class);

		//ASSERT
		assertEquals(0,objectList.size(),"Expected list size is 0");

	}

	@Test
	@DisplayName("Missing json array must return an empty List")
	void testDeserializeObject_MissingJsonArray() throws Exception {
		//ARRANGE
		jsonTestString = 
				"{"
						+ "\"fakeObject\": [\r\n"
						+ "]}";
		//Calling a real ObjectMapper to get JSONNode from the test String:
		JsonNode jsonNodeTest = new ObjectMapper().readTree(jsonTestString);
		logger.debug("jsonNodeTest={}",jsonNodeTest);
		when(objectMapperMock.readTree(any(File.class))).thenReturn(jsonNodeTest);

		//ACT
		List<Medicalrecord> objectList = jsonFileMapperCUT.deserialize(new File(""), "medicalrecords", Medicalrecord.class);

		//ASSERT
		assertEquals(0,objectList.size(),"Expected list size is 0");		
	}

	@Test
	@DisplayName("Empty json file must return an empty List")
	void testDeserializeObject_EmptyJsonFile() throws Exception{
		//ARRANGE
		jsonTestString = "";
		//Calling a real ObjectMapper to get JSONNode from the test String:
		JsonNode jsonNodeTest = new ObjectMapper().readTree(jsonTestString);
		logger.debug("jsonNodeTest={}",jsonNodeTest);
		when(objectMapperMock.readTree(any(File.class))).thenReturn(jsonNodeTest);

		//ACT
		List<Medicalrecord> objectList = jsonFileMapperCUT.deserialize(new File(""), "medicalrecords", Medicalrecord.class);

		//ASSERT
		assertEquals(0,objectList.size(),"Expected list size is 0");

	}

	@Test
	@DisplayName("JsonParseException must return an empty List")
	void testDeserializeObject_JsonParseException() throws Exception{
		//ARRANGE
		//JsonEOFException:
		when(objectMapperMock.readTree(any(File.class))).thenThrow(JsonParseException.class);

		//ACT
		List<Medicalrecord> objectList = jsonFileMapperCUT.deserialize(new File(""), "medicalrecords", Medicalrecord.class);

		//ASSERT
		assertEquals(0,objectList.size(),"Expected list size is 0");
	}

	@Test
	@DisplayName("IOException must return an empty List")
	void testDeserializeObject_IOException() throws Exception{
		//ARRANGE
		//JsonEOFException:
		when(objectMapperMock.readTree(any(File.class))).thenThrow(IOException.class);

		//ACT
		List<Medicalrecord> objectList = jsonFileMapperCUT.deserialize(new File(""), "medicalrecords", Medicalrecord.class);

		//ASSERT
		assertEquals(0,objectList.size(),"Expected list size is 0");
	}


	@Test
	@DisplayName("2 objects Firestation already exist, 2 serialized")
	void testSerializeObject_2firestations()  throws Exception {
		//ARRANGE
		jsonTestString = 
				"{"
						+ "\"firestations\": [\r\n"
						+ "		{ \"address\":\"1509 Culver St\", \"station\":\"3\" },\r\n"
						+ "		{ \"address\":\"29 15th St\", \"station\":\"2\" }\r\n"
						+ "	]}";
		
		//Calling a real ObjectMapper to get JSONNode from the test String:
		JsonNode jsonNodeTest = new ObjectMapper().readTree(jsonTestString);
		logger.debug("jsonNodeTest={}",jsonNodeTest);
		when(objectMapperMock.readTree(any(File.class))).thenReturn(jsonNodeTest);
		List<Firestation> listToSave = new ArrayList<>();
		listToSave.add(new Firestation("addressNew1",1000));
		listToSave.add(new Firestation("addressNew2",2000));

		String jsonStringExpected = 
				"{\"firestations\":[{\"address\":\"addressNew1\",\"station\":1000},{\"address\":\"addressNew2\",\"station\":2000}]}";
		JsonNode jsonNodeExpected = new ObjectMapper().readTree(jsonStringExpected);

		doNothing().when(objectMapperMock).writeValue(new File(""),jsonNodeExpected);

		//ACT
		boolean result = jsonFileMapperCUT.serialize(new File(""), "firestations", Firestation.class, listToSave);

		//ASSERT
		assertTrue(result,"Success operation, expect true");
		verify(objectMapperMock, times(1)).writeValue(new File(""),jsonNodeExpected);

	}
	
	
	@Test
	@DisplayName("No Firestation already exist, 2 serialized")
	void testSerializeObject_NoFirestationInFile_serialize2firestations()  throws Exception {
		//ARRANGE
		jsonTestString = 
				"{\"fakeObjects\": [{ \"fakeName1\":\"fake1\", \"fakeName2\":\"fake2\"}]}";
		
		//Calling a real ObjectMapper to get JSONNode from the test String:
		JsonNode jsonNodeTest = new ObjectMapper().readTree(jsonTestString);
		logger.debug("jsonNodeTest={}",jsonNodeTest);
		when(objectMapperMock.readTree(any(File.class))).thenReturn(jsonNodeTest);
		List<Firestation> listToSave = new ArrayList<>();
		listToSave.add(new Firestation("addressNew1",1000));
		listToSave.add(new Firestation("addressNew2",2000));

		String jsonStringExpected = 
				"{"
				+ "\"fakeObjects\": [{ \"fakeName1\":\"fake1\", \"fakeName2\":\"fake2\"}],"
				+ "\"firestations\":[{\"address\":\"addressNew1\",\"station\":1000},{\"address\":\"addressNew2\",\"station\":2000}]"
				+ "}";
		JsonNode jsonNodeExpected = new ObjectMapper().readTree(jsonStringExpected);

		doNothing().when(objectMapperMock).writeValue(new File(""),jsonNodeExpected);

		//ACT
		boolean result = jsonFileMapperCUT.serialize(new File(""), "firestations", Firestation.class, listToSave);

		//ASSERT
		assertTrue(result,"Success operation, expect true");
		verify(objectMapperMock, times(1)).writeValue(new File(""),jsonNodeExpected);

	}

	@Test
	@DisplayName("IOException must return false")
	void testSerializeObject_IOException() throws Exception{
		//ARRANGE
		//JsonEOFException:
		List<Firestation> listToSave = new ArrayList<>();
		when(objectMapperMock.readTree(any(File.class))).thenThrow(IOException.class);

		//ACT
		boolean result = jsonFileMapperCUT.serialize(new File(""), "firestations", Firestation.class, listToSave);

		//ASSERT
		assertFalse(result,"Failed operation due to exception, expect false");
	}
	
}
