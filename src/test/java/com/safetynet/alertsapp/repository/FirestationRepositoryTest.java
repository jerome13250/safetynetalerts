package com.safetynet.alertsapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.safetynet.alertsapp.jsonfilemapper.JsonFileMapper;
import com.safetynet.alertsapp.model.Firestation;

@ExtendWith(MockitoExtension.class)
class FirestationRepositoryTest {

	private final Logger logger = LoggerFactory.getLogger(FirestationRepositoryTest.class);

	@InjectMocks
	FirestationRepository firestationRepositoryCUT;

	@Mock
	private JsonFileMapper jsonFileMapperMock;

	//TODO : ONLY TEST FUNCTION map ONCE with Mock
	//Then create @BeforeEach to create a default mockedList
	
	@Test
	@DisplayName("2 objects Firestation, test getAll")
	void testgetAll_2firestations()  throws Exception {
		//Arrange
		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Firestation> mockedList = new ArrayList<> (Arrays.asList( 				
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345)
				));
		when(jsonFileMapperMock.map(any(File.class), any(String.class), any(TypeReference.class))).thenReturn(mockedList);

		List<Firestation> expectedList = Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345)
				);

		//Act
		firestationRepositoryCUT.loadJsonDataFromFile(); //need explicit call, @Postconstruct is not executed by Mockito
		List<Firestation> objectList = firestationRepositoryCUT.getAll();

		//Assert
		assertEquals(2,objectList.size(),"Expected list size is 2");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList");
	}

	@Test
	@DisplayName("2 objects Firestation + add one more")
	void testAdd_2firestations_addOneMore()  throws Exception {
		//Arrange
		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Firestation> mockedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345)
				));
		when(jsonFileMapperMock.map(any(File.class), any(String.class), any(TypeReference.class))).thenReturn(mockedList);

		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));

		//Act
		firestationRepositoryCUT.loadJsonDataFromFile(); //need explicit call, @Postconstruct is not executed by Mockito
		firestationRepositoryCUT.add(new Firestation("adress3", 333));
		List<Firestation> objectList = firestationRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertEquals(expectedList,objectList,"Returned list must be mockedList + added firestation");
	}
	
	
	@Test
	@DisplayName("3 objects Firestation + update one")
	void testUpdate_3firestations_updateOne()  throws Exception {
		//Arrange
		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Firestation> mockedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));
		when(jsonFileMapperMock.map(any(File.class), any(String.class), any(TypeReference.class))).thenReturn(mockedList);

		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 1),
				new Firestation("adress3", 333)
				));

		//Act
		firestationRepositoryCUT.loadJsonDataFromFile(); //need explicit call, @Postconstruct is not executed by Mockito
		boolean result = firestationRepositoryCUT.update(new Firestation("adress2", 1));
		List<Firestation> objectList = firestationRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertTrue(result,"Expected result to be successful : true");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList except firestation must be 1 for address2");
	}
	
	@Test
	@DisplayName("3 objects Firestation + try update inexistant one")
	void testUpdate_3firestations_tryUpdateInexistantOne()  throws Exception {
		//Arrange
		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Firestation> mockedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));
		when(jsonFileMapperMock.map(any(File.class), any(String.class), any(TypeReference.class))).thenReturn(mockedList);

		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));

		//Act
		firestationRepositoryCUT.loadJsonDataFromFile(); //need explicit call, @Postconstruct is not executed by Mockito
		boolean result = firestationRepositoryCUT.update(new Firestation("adressUnknown", 1));
		List<Firestation> objectList = firestationRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertFalse(result,"Expected result to be failure : false");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList except firestation must be 1 for address2");
	}
	
	@Test
	@DisplayName("3 objects Firestation + delete one")
	void testDelete_3firestations_deleteOne()  throws Exception {
		//Arrange
		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Firestation> mockedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));
		when(jsonFileMapperMock.map(any(File.class), any(String.class), any(TypeReference.class))).thenReturn(mockedList);

		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));

		//Act
		firestationRepositoryCUT.loadJsonDataFromFile(); //need explicit call, @Postconstruct is not executed by Mockito
		boolean result = firestationRepositoryCUT.delete("adress1");
		List<Firestation> objectList = firestationRepositoryCUT.getAll();

		//Assert
		assertEquals(2,objectList.size(),"Expected list size is 2");
		assertTrue(result,"Expected result to be successful : true");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList except address1 must be removed");
	}

	@Test
	@DisplayName("3 objects Firestation + try delete inexistant one")
	void testDelete_3firestations_tryDeleteInexistantOne()  throws Exception {
		//Arrange
		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Firestation> mockedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));
		when(jsonFileMapperMock.map(any(File.class), any(String.class), any(TypeReference.class))).thenReturn(mockedList);

		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));

		//Act
		firestationRepositoryCUT.loadJsonDataFromFile(); //need explicit call, @Postconstruct is not executed by Mockito
		boolean result = firestationRepositoryCUT.delete("adressUnknown");
		List<Firestation> objectList = firestationRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertFalse(result,"Expected result to be successful : true");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList, nothing deleted");
	}
	
}
