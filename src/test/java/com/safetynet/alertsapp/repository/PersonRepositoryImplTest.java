package com.safetynet.alertsapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
import com.safetynet.alertsapp.model.Person;

@ExtendWith(MockitoExtension.class)
class PersonRepositoryImplTest {

	@InjectMocks
	PersonRepositoryImpl personRepositoryCUT;

	@Mock
	private JsonFileMapperImpl jsonFileMapperMock;

	@BeforeEach
	void initializeData() {
		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Person> dataInitialList = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Mike","Doe","1-99999999", 12345, "adress1", "Gotham", "mikedoe@mail.com"),
				new Person("Matt","Damon","1-22222222", 789654, "adress2", "New-York", "mattdamon@mail.com")
				));
		personRepositoryCUT.setPersonList(dataInitialList);
	}

	@Test
	@DisplayName("2 objects Person: LoadJsonDataFromFile")
	void testLoadJsonDataFromFile_2persons()  throws Exception {
		//Arrange
		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Person> mockedList = new ArrayList<> (Arrays.asList( 				
				new Person("Steve","Austin","1", 999, "adress10", "Los Angeles", "steveaustin@mail.com"),
				new Person("Michael","Knight","2", 666, "adress11", "Miami", "michaelknight@mail.com")
				));

		when(jsonFileMapperMock.deserialize(
				"persons",
				Person.class))
		.thenReturn(mockedList);

		List<Person> expectedList = Arrays.asList(
				new Person("Steve","Austin","1", 999, "adress10", "Los Angeles", "steveaustin@mail.com"),
				new Person("Michael","Knight","2", 666, "adress11", "Miami", "michaelknight@mail.com")
				);

		//Act
		personRepositoryCUT.loadJsonDataFromFile();
		List<Person> objectList = personRepositoryCUT.getAll();

		//Assert
		assertEquals(2,objectList.size(),"Expected list size is 2");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList");
	}

	@Test
	@DisplayName("3 objects Person + add one more")
	void testAdd_3persons_addOneMore()  throws Exception {
		//Arrange
		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Person> expectedList = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Mike","Doe","1-99999999", 12345, "adress1", "Gotham", "mikedoe@mail.com"),
				new Person("Matt","Damon","1-22222222", 789654, "adress2", "New-York", "mattdamon@mail.com"),
				new Person("Georges","Clooney","1-444444", 9874, "adress3", "Las Vegas", "georgesclooney@mail.com")
				));

		//Act
		personRepositoryCUT.add(
				new Person("Georges","Clooney","1-444444", 9874, "adress3", "Las Vegas", "georgesclooney@mail.com")
				);
		List<Person> objectList = personRepositoryCUT.getAll();

		//Assert
		assertEquals(4,objectList.size(),"Expected list size is 4");
		assertEquals(expectedList,objectList,"Returned list must be initial List + added person");
	}

	@Test
	@DisplayName("3 objects Person + add one that already exists")
	void testAdd_3persons_addOneThatAlreadyExists()  throws Exception {
		//Arrange
		Person personJohnDoe = new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com");
		List<Person> expectedList = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Mike","Doe","1-99999999", 12345, "adress1", "Gotham", "mikedoe@mail.com"),
				new Person("Matt","Damon","1-22222222", 789654, "adress2", "New-York", "mattdamon@mail.com")
				));

		//Act
		assertThrows(BusinessResourceException.class, ()-> personRepositoryCUT.add(personJohnDoe));
		List<Person> objectList = personRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertEquals(expectedList,objectList,"Returned list must be initial List");
	}
	
	@Test
	@DisplayName("Test add incomplete Person")
	void testAdd_incompletePerson()  throws Exception {
		//Arrange
		List<Person> expectedList = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Mike","Doe","1-99999999", 12345, "adress1", "Gotham", "mikedoe@mail.com"),
				new Person("Matt","Damon","1-22222222", 789654, "adress2", "New-York", "mattdamon@mail.com")));

		Person incompletePerson1 = new Person();
		Person incompletePerson2 = new Person(null,"doe","phone", 12345, "address", "city", "fake@mail.com");
		Person incompletePerson3 = new Person("john",null,"phone", 12345, "address", "city", "fake@mail.com");
		Person incompletePerson4 = new Person("john","doe",null, 12345, "address", "city", "fake@mail.com");
		Person incompletePerson5 = new Person("john","doe","phone", null, "address", "city", "fake@mail.com");
		Person incompletePerson6 = new Person("john","doe","phone", 12345, null, "city", "fake@mail.com");
		Person incompletePerson7 = new Person("john","doe","phone", 12345, "address", null, "fake@mail.com");
		Person incompletePerson8 = new Person("john","doe","phone", 12345, "address", "city", null);
		//Act-Assert
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.add(incompletePerson1));
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.add(incompletePerson2));
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.add(incompletePerson3));
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.add(incompletePerson4));
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.add(incompletePerson5));
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.add(incompletePerson6));
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.add(incompletePerson7));
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.add(incompletePerson8));
		
		assertEquals(expectedList,personRepositoryCUT.getAll(),"Returned list must be same as initial List");
		
	}

	@Test
	@DisplayName("3 objects Person + update one")
	void testUpdate_3persons_updateOne()  throws Exception {
		//Arrange
		List<Person> expectedList = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Mike","Doe","1-99999999", 12345, "adress1", "Gotham", "mikedoe@mail.com"),
				new Person("Matt","Damon","1-0000", 999, "adress0", "Nowhere", "mattdamon@updated.com")
				));

		doNothing().when(jsonFileMapperMock).serialize(any(String.class), any(Class.class), any(List.class));
		
		//Act
		boolean result = personRepositoryCUT.update(
				new Person("Matt","Damon","1-0000", 999, "adress0", "Nowhere", "mattdamon@updated.com")
				);
		List<Person> objectList = personRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertTrue(result,"Expected result to be successful : true");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList except 1 person updated");
	}

	@Test
	@DisplayName("3 objects Person + try update inexistant one")
	void testUpdate_3persons_tryUpdateInexistantOne()  throws Exception {
		//Arrange
		Person personUnknown = new Person("unknown","unknown","0", 0, "none", "no city", "unknown@mail.com");
		List<Person> expectedList = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Mike","Doe","1-99999999", 12345, "adress1", "Gotham", "mikedoe@mail.com"),
				new Person("Matt","Damon","1-22222222", 789654, "adress2", "New-York", "mattdamon@mail.com")
				));

		//Act
		assertThrows(BusinessResourceException.class,()->personRepositoryCUT.update(personUnknown));
		List<Person> objectList = personRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertEquals(expectedList,objectList,"Returned list must be same as initial List");
	}

	@Test
	@DisplayName("Test update incomplete Person")
	void testUpdate_incompletePerson()  throws Exception {
		//Arrange
		List<Person> expectedList = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Mike","Doe","1-99999999", 12345, "adress1", "Gotham", "mikedoe@mail.com"),
				new Person("Matt","Damon","1-22222222", 789654, "adress2", "New-York", "mattdamon@mail.com")));

		Person incompletePerson1 = new Person();
		Person incompletePerson2 = new Person(null,"doe","phone", 12345, "address", "city", "fake@mail.com");
		Person incompletePerson3 = new Person("john",null,"phone", 12345, "address", "city", "fake@mail.com");
		Person incompletePerson4 = new Person("john","doe",null, 12345, "address", "city", "fake@mail.com");
		Person incompletePerson5 = new Person("john","doe","phone", null, "address", "city", "fake@mail.com");
		Person incompletePerson6 = new Person("john","doe","phone", 12345, null, "city", "fake@mail.com");
		Person incompletePerson7 = new Person("john","doe","phone", 12345, "address", null, "fake@mail.com");
		Person incompletePerson8 = new Person("john","doe","phone", 12345, "address", "city", null);
		//Act-Assert
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.update(incompletePerson1));
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.update(incompletePerson2));
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.update(incompletePerson3));
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.update(incompletePerson4));
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.update(incompletePerson5));
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.update(incompletePerson6));
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.update(incompletePerson7));
		assertThrows(BusinessResourceException.class, ()->personRepositoryCUT.update(incompletePerson8));
		
		assertEquals(expectedList,personRepositoryCUT.getAll(),"Returned list must be same as initial List");
		
	}
	
	@Test
	@DisplayName("3 objects Person + delete one")
	void testDelete_3persons_deleteOne()  throws Exception {
		//Arrange
		List<Person> expectedList = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Matt","Damon","1-22222222", 789654, "adress2", "New-York", "mattdamon@mail.com")
				));

		doNothing().when(jsonFileMapperMock).serialize(any(String.class), any(Class.class), any(List.class));
		
		//Act
		boolean result = personRepositoryCUT.delete("Mike","Doe");
		List<Person> objectList = personRepositoryCUT.getAll();

		//Assert
		assertEquals(2,objectList.size(),"Expected list size is 2");
		assertTrue(result,"Expected result to be successful : true");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList except one must be removed");
	}

	@Test
	@DisplayName("3 objects Person + try delete inexistant one")
	void testDelete_3persons_tryDeleteInexistantOne()  throws Exception {
		//Arrange
		List<Person> expectedList = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Mike","Doe","1-99999999", 12345, "adress1", "Gotham", "mikedoe@mail.com"),
				new Person("Matt","Damon","1-22222222", 789654, "adress2", "New-York", "mattdamon@mail.com")
				));

		//Act
		assertThrows(BusinessResourceException.class,()->personRepositoryCUT.delete("unknown","guy"));
		List<Person> objectList = personRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList, nothing deleted");
	}

	@Test
	@DisplayName("3 objects Person + try delete inexistant one")
	void testgetByAddress()  throws Exception {
		//Arrange
		List<Person> expectedList = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Mike","Doe","1-99999999", 12345, "adress1", "Gotham", "mikedoe@mail.com")
				));

		//Act
		List<Person> objectList = personRepositoryCUT.getByAddress("adress1");

		//Assert
		assertEquals(2,objectList.size(),"Expected list size is 3");
		assertEquals(expectedList,objectList,"Returned list must be mockedList, filtered on address1");
	}

	@Test
	@DisplayName("Test get by firstname and lastname")
	void testgetByFirstnameLastname()  throws Exception {
		//Arrange
		Person expected = new Person("Matt","Damon","1-22222222", 789654, "adress2", "New-York", "mattdamon@mail.com");

		//Act
		Person result = personRepositoryCUT.getByFirstnameLastname("Matt","Damon");
		//Assert
		assertEquals(expected,result,"Returned person must be same as mocked");
	}

	@Test
	@DisplayName("Test get by unknown firstname and lastname, must return null")
	void testgetByUnknownFirstnameLastnameMustReturnNull()  throws Exception {
		//Arrange

		//Act
		Person unknownFirstName = personRepositoryCUT.getByFirstnameLastname("Unknown","Damon");
		Person unknownLastName = personRepositoryCUT.getByFirstnameLastname("Matt","Unknown");
		Person unknownBoth = personRepositoryCUT.getByFirstnameLastname("Unknown","Unknown");
		//Assert
		assertNull(unknownFirstName,"Unknown person must return null");
		assertNull(unknownLastName,"Unknown person must return null");
		assertNull(unknownBoth,"Unknown person must return null");
	}

	@Test
	@DisplayName("Test get by firstname and lastname, return more than one entry is error")
	void testgetByUnknownFirstnameLastname_IllegalStateException()  throws Exception {
		//Arrange
		List<Person> dataInitialList = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Mike","Doe","1-99999999", 12345, "adress1", "Gotham", "mikedoe@mail.com"),
				new Person("Matt","Damon","1-22222222", 789654, "adress2", "New-York", "mattdamon@mail.com"),
				new Person("Matt","Damon","1-22222222", 789654, "adress2", "New-York", "mattdamon@mail.com")
				));
		personRepositoryCUT.setPersonList(dataInitialList);

		//Act-Assert
		assertThrows(IllegalStateException.class,()->personRepositoryCUT.getByFirstnameLastname("Matt","Damon"));
	}

	@Test
	@DisplayName("Test get by city")
	void testgetByCity()   throws Exception {
		//Arrange
		List<Person> expected = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Mike","Doe","1-99999999", 12345, "adress1", "Gotham", "mikedoe@mail.com")
				));
		//Act
		List<Person> result = personRepositoryCUT.getByCity("Gotham");
		//Assert
		assertEquals(expected,result,"Returned person must be same as mocked");
	}

	@Test
	@DisplayName("Test get by lastname")
	void testgetByLastname()   throws Exception {
		//Arrange
		List<Person> expected = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Mike","Doe","1-99999999", 12345, "adress1", "Gotham", "mikedoe@mail.com")
				));
		//Act
		List<Person> result = personRepositoryCUT.getByLastname("Doe");
		//Assert
		assertEquals(expected,result,"Returned person must be same as mocked");
	}

}

