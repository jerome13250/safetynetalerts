package com.safetynet.alertsapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import com.safetynet.alertsapp.jsonfilemapper.JsonFileMapper;
import com.safetynet.alertsapp.model.Person;

@ExtendWith(MockitoExtension.class)
public class PersonRepositoryTest {

	@InjectMocks
	PersonRepository personRepositoryCUT;

	@Mock
	private JsonFileMapper jsonFileMapperMock;

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

		when(jsonFileMapperMock.map(
				Paths.get("json/data.json").toFile(),
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
	@DisplayName("3 objects Person + update one")
	void testUpdate_3persons_updateOne()  throws Exception {
		//Arrange
		List<Person> expectedList = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Mike","Doe","1-99999999", 12345, "adress1", "Gotham", "mikedoe@mail.com"),
				new Person("Matt","Damon","1-0000", 0, "adress0", "Nowhere", "mattdamon@updated.com")
				));

		//Act
		boolean result = personRepositoryCUT.update(
				new Person("Matt","Damon","1-0000", 0, "adress0", "Nowhere", "mattdamon@updated.com")
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
		List<Person> expectedList = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Mike","Doe","1-99999999", 12345, "adress1", "Gotham", "mikedoe@mail.com"),
				new Person("Matt","Damon","1-22222222", 789654, "adress2", "New-York", "mattdamon@mail.com")
				));

		//Act
		boolean result = personRepositoryCUT.update(
				new Person("unknown","guy","0", 0, "none", "no city", "unknown@mail.com")
				);
		List<Person> objectList = personRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertFalse(result,"Expected result to be failure : false");
		assertEquals(expectedList,objectList,"Returned list must be same as initial List");
	}
	
	@Test
	@DisplayName("3 objects Person + delete one")
	void testDelete_3persons_deleteOne()  throws Exception {
		//Arrange
		List<Person> expectedList = new ArrayList<> (Arrays.asList(
				new Person("John","Doe","1-88888888", 12345, "adress1", "Gotham", "johndoe@mail.com"),
				new Person("Matt","Damon","1-22222222", 789654, "adress2", "New-York", "mattdamon@mail.com")
				));

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
		boolean result = personRepositoryCUT.delete("unknown","guy");
		List<Person> objectList = personRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertFalse(result,"Expected result to be successful : true");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList, nothing deleted");
	}
}
