package com.safetynet.alertsapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
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

import com.safetynet.alertsapp.config.CustomProperties;
import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.jsonfilemapper.JsonFileMapperImpl;
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.model.Firestation;

@ExtendWith(MockitoExtension.class)
class FirestationRepositoryImplTest {


	@InjectMocks
	FirestationRepositoryImpl firestationRepositoryCUT;

	@Mock
	private JsonFileMapperImpl jsonFileMapperMock;

	@Mock
	private CustomProperties customPropertiesMock;
	
	@BeforeEach
	void initializeData() {
		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Firestation> dataInitialList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));
	
		firestationRepositoryCUT.setFirestationList(dataInitialList);
	}

	@Test
	@DisplayName("2 objects Firestation: LoadJsonDataFromFile")
	void testLoadJsonDataFromFile_2firestations()  throws Exception {
		//Arrange
		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Firestation> mockedList = new ArrayList<> (Arrays.asList( 				
				new Firestation("adress100", 10),
				new Firestation("adress200", 20)
				));
		when(customPropertiesMock.getJsonfile()).thenReturn("thejsonfile.json");
		when(jsonFileMapperMock.deserialize(
				Paths.get("thejsonfile.json").toFile(),
				"firestations",
				Firestation.class))
		.thenReturn(mockedList);

		List<Firestation> expectedList = Arrays.asList(
				new Firestation("adress100", 10),
				new Firestation("adress200", 20)
				);

		//Act
		firestationRepositoryCUT.loadJsonDataFromFile();
		List<Firestation> objectList = firestationRepositoryCUT.getAll();

		//Assert
		assertEquals(2,objectList.size(),"Expected list size is 2");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList");
	}

	@Test
	@DisplayName("3 objects Firestation + add one more")
	void testAdd_3firestations_addOneMore()  throws Exception {
		//Arrange
		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333),
				new Firestation("adress4", 444)
				));

		//Act
		boolean result = firestationRepositoryCUT.add(new Firestation("adress4", 444));
		List<Firestation> objectList = firestationRepositoryCUT.getAll();

		//Assert
		assertTrue(result,"The operation must be success");
		assertEquals(4,objectList.size(),"Expected list size is 4");
		assertEquals(expectedList,objectList,"Returned list must be initial List + added firestation");
	}

	@Test
	@DisplayName("Test add incomplete Firestation")
	void testAdd_incompleteFirestation()  throws Exception {
		//Arrange
		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)));

		Firestation incompleteFirestation1 = new Firestation();
		Firestation incompleteFirestation2 = new Firestation(null,1);
		Firestation incompleteFirestation3 = new Firestation("address",null);
		
		//Act-Assert
		assertThrows(BusinessResourceException.class, ()->firestationRepositoryCUT.add(incompleteFirestation1));
		assertThrows(BusinessResourceException.class, ()->firestationRepositoryCUT.add(incompleteFirestation2));
		assertThrows(BusinessResourceException.class, ()->firestationRepositoryCUT.add(incompleteFirestation3));
		
		assertEquals(expectedList,firestationRepositoryCUT.getAll(),"Returned list must be same as initial List");
		
	}
	
	@Test
	@DisplayName("3 objects Firestation + update one")
	void testUpdate_3firestations_updateOne()  throws Exception {
		//Arrange
		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 1),
				new Firestation("adress3", 333)
				));

		//Act
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
		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));

		//Act
		boolean result = firestationRepositoryCUT.update(new Firestation("adressUnknown", 1));
		List<Firestation> objectList = firestationRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertFalse(result,"Expected result to be failure : false");
		assertEquals(expectedList,objectList,"Returned list must be same as initial List");
	}

	@Test
	@DisplayName("Test update incomplete Firestation")
	void testUpdate_incompleteFirestation()  throws Exception {
		//Arrange
		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)));

		Firestation incompleteFirestation1 = new Firestation();
		Firestation incompleteFirestation2 = new Firestation(null,1);
		Firestation incompleteFirestation3 = new Firestation("address",null);
		
		//Act-Assert
		assertThrows(BusinessResourceException.class, ()->firestationRepositoryCUT.update(incompleteFirestation1));
		assertThrows(BusinessResourceException.class, ()->firestationRepositoryCUT.update(incompleteFirestation2));
		assertThrows(BusinessResourceException.class, ()->firestationRepositoryCUT.update(incompleteFirestation3));
		
		assertEquals(expectedList,firestationRepositoryCUT.getAll(),"Returned list must be same as initial List");
		
	}
	
	@Test
	@DisplayName("3 objects Firestation + delete one by address")
	void testDeleteByAddress_3firestations_deleteOne()  throws Exception {
		//Arrange
		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));

		//Act
		boolean result = firestationRepositoryCUT.deleteByAddress("adress1");
		List<Firestation> objectList = firestationRepositoryCUT.getAll();

		//Assert
		assertEquals(2,objectList.size(),"Expected list size is 2");
		assertTrue(result,"Expected result to be successful : true");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList except address1 must be removed");
	}

	@Test
	@DisplayName("3 objects Firestation + try delete inexistant one by address")
	void testDeleteByAddress_3firestations_tryDeleteInexistantOne()  throws Exception {
		//Arrange
		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));

		//Act
		boolean result = firestationRepositoryCUT.deleteByAddress("adressUnknown");
		List<Firestation> objectList = firestationRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertFalse(result,"Expected result to be failed : result must be false");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList, nothing deleted");
	}

	@Test
	@DisplayName("3 objects Firestation + delete one by station")
	void testDeleteByStation_4firestations_deleteTwo()  throws Exception {
		//Arrange
		List<Firestation> dataInitialList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333),
				new Firestation("adress4", 1000)
				));
		firestationRepositoryCUT.setFirestationList(dataInitialList);
		
		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));

		//Act
		boolean result = firestationRepositoryCUT.deleteByStation(1000);
		List<Firestation> objectList = firestationRepositoryCUT.getAll();

		//Assert
		assertEquals(2,objectList.size(),"Expected list size is 2");
		assertTrue(result,"Expected result to be successful : true");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList except firestation 1000 must be removed");
	}

	@Test
	@DisplayName("3 objects Firestation + try delete inexistant one")
	void testDeleteByStation_3firestations_tryDeleteInexistantOne()  throws Exception {
		//Arrange
		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));

		//Act
		boolean result = firestationRepositoryCUT.deleteByStation(1);
		List<Firestation> objectList = firestationRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertFalse(result,"Expected result to be failed : result must be false");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList, nothing deleted");
	}
	
	@Test
	@DisplayName("Test getByStationnumber")
	void testGetByStationnumber()  throws Exception {
		//Arrange
		List<Firestation> expectedList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000)
				));

		//Act
		List<Firestation> objectList = firestationRepositoryCUT.getByStationnumber(1000);

		//Assert
		assertEquals(1,objectList.size(),"Expected list size is 3");
		assertEquals(expectedList,objectList,"Returned list must one Firestation");
	}
	
	@Test
	@DisplayName("Test getByAddress")
	void testGetByAddress() throws Exception {
		//Arrange
		int expected = 1000;

		//Act
		int result = firestationRepositoryCUT.getByAddress("adress1");

		//Assert
		assertEquals(expected,result,"Returned value must be 1000");
	}
		
	@Test
	@DisplayName("Test getByAddress, address not found")
	void testGetByAddressNotFound() throws Exception {
		//Arrange
		
		//Act
		Integer result = firestationRepositoryCUT.getByAddress("adressUnknown");

		//Assert
		assertNull(result,"Returned value must be null");
	}
	
	@Test
	@DisplayName("Test getByAddress, more than 1 firestation found")
	void testGetByAddressMultipleFirestationForAddress() throws Exception {
		//Arrange
		List<Firestation> dataInitialList = new ArrayList<> (Arrays.asList(
				new Firestation("adress1", 1000),
				new Firestation("adress1", 2000), //this should not exist
				new Firestation("adress2", 12345),
				new Firestation("adress3", 333)
				));
	
		firestationRepositoryCUT.setFirestationList(dataInitialList);
		
		int expected = -1;

		//Act-Assert
		assertThrows(IllegalStateException.class,()->firestationRepositoryCUT.getByAddress("adress1"));
		
	}
	
}
