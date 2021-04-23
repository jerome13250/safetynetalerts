package com.safetynet.alertsapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Person;
import com.safetynet.alertsapp.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

	private final Logger logger = LoggerFactory.getLogger(PersonServiceTest.class);

	@InjectMocks
	PersonService personServiceCUT;

	@Mock
	PersonRepository personRepositoryMock;

	@Test
	@DisplayName("Save Person: success case")
	void testSavePerson() throws Exception {
		//Arrange
		Person personToSave = new Person("New", "NewName", "0-0000", 0, "addressNew", "cityNew", "new@mail.com");    
		Person personSaved = new Person("New", "NewName", "0-0000", 0, "addressNew", "cityNew", "new@mail.com"); 
		when(personRepositoryMock.getByFirstnameLastname("New", "NewName"))
		.thenReturn(null)//At first the person is not in our data
		.thenReturn(personSaved);//At the end the person is present
		when(personRepositoryMock.add(personToSave)).thenReturn(true);

		//Act
		Person result = personServiceCUT.savePerson(personToSave);

		//Assert
		assertNotNull(result);
		assertEquals(personToSave,result);
		verify(personRepositoryMock, times(1)).add(any(Person.class));

	}

	@Test
	@DisplayName("Save Person: fail case Person already exists in data")
	void testSavePerson_PersonAlreadyExist_BusinessResourceException() throws Exception {
		//Arrange
		Person personToSave = new Person("New", "NewName", "0-0000", 0, "addressNew", "cityNew", "new@mail.com");
		when(personRepositoryMock.getByFirstnameLastname("New", "NewName")).thenReturn(personToSave); //Person already exist

		//Act
		assertThrows(BusinessResourceException.class,()->personServiceCUT.savePerson(personToSave));
	}

	@Test
	@DisplayName("Save Person: fail case cause multiple Person already exists in data")
	void testSavePerson_doublesFirstnameLastname_IllegalStateException() throws Exception {
		//Arrange
		Person personToSave = new Person("New", "NewName", "0-0000", 0, "addressNew", "cityNew", "new@mail.com");
		when(personRepositoryMock.getByFirstnameLastname("New", "NewName")).thenThrow(IllegalStateException.class);

		//Act
		assertThrows(BusinessResourceException.class,()->personServiceCUT.savePerson(personToSave));
	}

	@Test
	@DisplayName("Save Person: fail case cause Person object is incomplete")
	void testSavePerson_incompletePerson_BusinessResourceException() throws Exception {
		//Arrange
		Person personToSave = new Person("New", "NewName", null, null, "addressNew", "cityNew", "new@mail.com");
		when(personRepositoryMock.getByFirstnameLastname("New", "NewName")).thenReturn(null);
		doThrow(BusinessResourceException.class).when(personRepositoryMock).add(personToSave);

		//Act
		assertThrows(BusinessResourceException.class,()->personServiceCUT.savePerson(personToSave));
	}

	@Test
	@DisplayName("Update Person: success case")
	void testUpdatePerson() throws Exception {
		//Arrange
		Person personToUpdate = new Person("John", "Doe", "1-1111", 1, "address1", "city1", "johndoe@mail.com");
		Person personUpdated = new Person("John", "Doe", "2-2222", 2, "address2", "city2", "johndoe2@mail.com"); 
		when(personRepositoryMock.getByFirstnameLastname("John", "Doe"))
		.thenReturn(personToUpdate)//The person is in our data
		.thenReturn(personUpdated);//At the end the person is present
		when(personRepositoryMock.update(personUpdated)).thenReturn(true);

		//Act
		Person result = personServiceCUT.updatePerson(personUpdated);

		//Assert
		assertNotNull(result);
		assertEquals(personUpdated,result);
		verify(personRepositoryMock, times(1)).update(any(Person.class));
	}

	@Test
	@DisplayName("Update Person: fail case Person does not exist in data")
	void testUpdatePerson_PersonDoesNotExist_BusinessResourceException() throws Exception {
		//Arrange
		Person personToUpdate = new Person("John", "Unknown", "1-1111", 1, "address1", "city1", "johndoe@mail.com");
		Person personUpdated = new Person("John", "Unknown", "2-2222", 2, "address2", "city2", "johndoe2@mail.com"); 
		when(personRepositoryMock.getByFirstnameLastname("John", "Unknown")).thenReturn(null); //Person does not exist in data

		//Act
		assertThrows(BusinessResourceException.class,()->personServiceCUT.updatePerson(personUpdated));
	}
	
	@Test
	@DisplayName("Update Person: fail case cause multiple Person already exists in data")
	void testUpdatePerson_doublesFirstnameLastname_IllegalStateException() throws Exception {
		//Arrange
		Person personToUpdate = new Person("John", "Doe", "1-1111", 1, "address1", "city1", "johndoe@mail.com");
		Person personUpdated = new Person("John", "Doe", "2-2222", 2, "address2", "city2", "johndoe2@mail.com"); 
		when(personRepositoryMock.getByFirstnameLastname("John", "Doe")).thenThrow(IllegalStateException.class);

		//Act
		assertThrows(BusinessResourceException.class,()->personServiceCUT.updatePerson(personUpdated));
	}
	
	@Test
	@DisplayName("Update Person: fail case cause Person object is incomplete")
	void testUpdatePerson_incompletePerson_BusinessResourceException() throws Exception {
		//Arrange
		Person personToUpdate = new Person("John", "Doe", "1-1111", 1, "address1", "city1", "johndoe@mail.com");
		Person personUpdated = new Person("John", "Doe", null, null, null, null, null); 
		when(personRepositoryMock.getByFirstnameLastname("John", "Doe")).thenReturn(personToUpdate); //Person already exist
		doThrow(BusinessResourceException.class).when(personRepositoryMock).update(personUpdated);

		//Act
		assertThrows(BusinessResourceException.class,()->personServiceCUT.updatePerson(personUpdated));
	}

	@Test
	@DisplayName("Delete Person: success case")
	void testDeletePerson() throws Exception {
		//Arrange
		when(personRepositoryMock.delete("John", "Doe")).thenReturn(true);

		//Act
		personServiceCUT.deletePerson("John", "Doe");

		//Assert
		verify(personRepositoryMock, times(1)).delete("John", "Doe");

	}
	
	@Test
	@DisplayName("Delete Person: fail case cause nothing has been deleted")
	void testDeletePerson_nothingDeleted_BusinessResourceException() throws Exception {
		//Arrange
		when(personRepositoryMock.delete("John", "Doe")).thenReturn(false);

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->personServiceCUT.deletePerson("John", "Doe"));
	}
	
	@Test
	@DisplayName("Delete Person: fail case cause UnsupportedOperationException occured")
	void testDeletePerson_nothingDeleted_Exception() throws Exception {
		//Arrange
		when(personRepositoryMock.delete("John", "Doe")).thenThrow(UnsupportedOperationException.class);

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->personServiceCUT.deletePerson("John", "Doe"));
	}
	
}
