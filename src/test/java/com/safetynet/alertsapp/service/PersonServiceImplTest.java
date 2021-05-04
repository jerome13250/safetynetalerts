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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Person;
import com.safetynet.alertsapp.repository.IPersonRepository;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

	@InjectMocks
	PersonServiceImpl personServiceCUT;

	@Mock
	IPersonRepository personRepositoryMock;

	@Test
	@DisplayName("Save Person: success case")
	void testSavePerson() throws Exception {
		//Arrange
		Person personToSave = new Person("New", "NewName", "0-0000", 0, "addressNew", "cityNew", "new@mail.com");    
		Person personSaved = new Person("New", "NewName", "0-0000", 0, "addressNew", "cityNew", "new@mail.com"); 
		doNothing().when(personRepositoryMock).add(personToSave);
		when(personRepositoryMock.getByFirstnameLastname("New", "NewName")).thenReturn(personSaved);

		//Act
		Person result = personServiceCUT.savePerson(personToSave);

		//Assert
		assertNotNull(result);
		assertEquals(personSaved,result);
		verify(personRepositoryMock, times(1)).add(any(Person.class));

	}

	@Test
	@DisplayName("Save Person: fail cause BusinessResourceException")
	void testSavePerson_BusinessResourceException() throws Exception {
		//Arrange
		Person personToSave = new Person("New", "NewName", "0-0000", 0, "addressNew", "cityNew", "new@mail.com");
		when(personRepositoryMock.getByFirstnameLastname("New", "NewName")).thenThrow(BusinessResourceException.class); //Person already exist

		//Act
		assertThrows(BusinessResourceException.class,()->personServiceCUT.savePerson(personToSave));
	}

	@Test
	@DisplayName("Save Person: fail cause IllegalStateException")
	void testSavePerson_IllegalStateException() throws Exception {
		//Arrange
		Person personToSave = new Person("New", "NewName", "0-0000", 0, "addressNew", "cityNew", "new@mail.com");
		when(personRepositoryMock.getByFirstnameLastname("New", "NewName")).thenThrow(IllegalStateException.class);

		//Act
		assertThrows(BusinessResourceException.class,()->personServiceCUT.savePerson(personToSave));
	}

	@Test
	@DisplayName("Update Person: success case")
	void testUpdatePerson() throws Exception {
		//Arrange
		Person personUpdated = new Person("John", "Doe", "2-2222", 2, "address2", "city2", "johndoe2@mail.com"); 
		when(personRepositoryMock.getByFirstnameLastname("John", "Doe"))
		.thenReturn(personUpdated);//At the end the person is present
		doNothing().when(personRepositoryMock).update(personUpdated);

		//Act
		Person result = personServiceCUT.updatePerson(personUpdated);

		//Assert
		assertNotNull(result);
		assertEquals(personUpdated,result);
		verify(personRepositoryMock, times(1)).update(any(Person.class));
	}

	@Test
	@DisplayName("Update Person: fail cause BusinessResourceException")
	void testUpdatePerson_BusinessResourceException() throws Exception {
		//Arrange
		Person personToUpdate = new Person("John", "Unknown", "2-2222", 2, "address2", "city2", "johndoe2@mail.com"); 
		when(personRepositoryMock.getByFirstnameLastname("John", "Unknown")).thenThrow(BusinessResourceException.class);

		//Act
		assertThrows(BusinessResourceException.class,()->personServiceCUT.updatePerson(personToUpdate));
	}
	
	@Test
	@DisplayName("Update Person: fail cause IllegalStateException")
	void testUpdatePerson_IllegalStateException() throws Exception {
		//Arrange
		Person personUpdated = new Person("John", "Doe", "2-2222", 2, "address2", "city2", "johndoe2@mail.com"); 
		when(personRepositoryMock.getByFirstnameLastname("John", "Doe")).thenThrow(IllegalStateException.class);

		//Act
		assertThrows(BusinessResourceException.class,()->personServiceCUT.updatePerson(personUpdated));
	}
	
	@Test
	@DisplayName("Delete Person: success case")
	void testDeletePerson() throws Exception {
		//Arrange
		doNothing().when(personRepositoryMock).delete("John", "Doe");

		//Act
		personServiceCUT.deletePerson("John", "Doe");

		//Assert
		verify(personRepositoryMock, times(1)).delete("John", "Doe");

	}
	
	@Test
	@DisplayName("Delete Person: fail cause BusinessResourceException")
	void testDeletePerson_BusinessResourceException() throws Exception {
		//Arrange
		doThrow(BusinessResourceException.class).when(personRepositoryMock).delete("John", "Doe");

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->personServiceCUT.deletePerson("John", "Doe"));
	}
	
	@Test
	@DisplayName("Delete Person: fail case cause UnsupportedOperationException occured")
	void testDeletePerson_nothingDeleted_Exception() throws Exception {
		//Arrange
		doThrow(UnsupportedOperationException.class).when(personRepositoryMock).delete("John", "Doe");

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->personServiceCUT.deletePerson("John", "Doe"));
	}
	
}
