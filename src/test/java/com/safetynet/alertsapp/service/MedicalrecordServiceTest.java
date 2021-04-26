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

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.repository.MedicalrecordRepository;

@ExtendWith(MockitoExtension.class)
class MedicalrecordServiceTest {

	private final Logger logger = LoggerFactory.getLogger(MedicalrecordServiceTest.class);

	@InjectMocks
	MedicalrecordService medicalrecordServiceCUT;

	@Mock
	MedicalrecordRepository medicalrecordRepositoryMock;

	@Test
	@DisplayName("Save Medicalrecord: success case")
	void testSaveMedicalrecord() throws Exception {
		//Arrange
		Medicalrecord medicalrecordToSave = new Medicalrecord(
				"New",
				"NewName",
				LocalDate.of(1984, 3, 6),
				new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
				new ArrayList<> (Arrays.asList("fakeAllergy1"))
				);
		Medicalrecord medicalrecordSaved = new Medicalrecord(
				"New",
				"NewName",
				LocalDate.of(1984, 3, 6),
				new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
				new ArrayList<> (Arrays.asList("fakeAllergy1"))
				);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("New", "NewName"))
		.thenReturn(null)//At first the medicalrecord is not in our data
		.thenReturn(medicalrecordSaved);//At the end the medicalrecord is present
		when(medicalrecordRepositoryMock.add(medicalrecordToSave)).thenReturn(true);

		//Act
		Medicalrecord result = medicalrecordServiceCUT.saveMedicalrecord(medicalrecordToSave);

		//Assert
		assertNotNull(result);
		assertEquals(medicalrecordToSave,result);
		verify(medicalrecordRepositoryMock, times(1)).add(any(Medicalrecord.class));

	}

	@Test
	@DisplayName("Save Medicalrecord: fail case Medicalrecord already exists in data")
	void testSaveMedicalrecord_MedicalrecordAlreadyExist_BusinessResourceException() throws Exception {
		//Arrange
		Medicalrecord medicalrecordToSave = new Medicalrecord(
				"New",
				"NewName",
				LocalDate.of(1984, 3, 6),
				new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
				new ArrayList<> (Arrays.asList("fakeAllergy1"))
				);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("New", "NewName")).thenReturn(medicalrecordToSave); //Medicalrecord already exist

		//Act
		assertThrows(BusinessResourceException.class,()->medicalrecordServiceCUT.saveMedicalrecord(medicalrecordToSave));
	}

	@Test
	@DisplayName("Save Medicalrecord: fail case cause multiple Medicalrecord already exists in data")
	void testSaveMedicalrecord_doublesFirstnameLastname_IllegalStateException() throws Exception {
		//Arrange
		Medicalrecord medicalrecordToSave = new Medicalrecord(
				"New",
				"NewName",
				LocalDate.of(1984, 3, 6),
				new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
				new ArrayList<> (Arrays.asList("fakeAllergy1"))
				);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("New", "NewName")).thenThrow(IllegalStateException.class);

		//Act
		assertThrows(BusinessResourceException.class,()->medicalrecordServiceCUT.saveMedicalrecord(medicalrecordToSave));
	}

	@Test
	@DisplayName("Save Medicalrecord: fail case cause Medicalrecord object is incomplete")
	void testSaveMedicalrecord_incompleteMedicalrecord_BusinessResourceException() throws Exception {
		//Arrange
		Medicalrecord medicalrecordToSave = new Medicalrecord(
				"New",
				"NewName",
				LocalDate.of(1984, 3, 6),
				new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
				new ArrayList<> (Arrays.asList("fakeAllergy1"))
				);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("New", "NewName")).thenReturn(null);
		doThrow(BusinessResourceException.class).when(medicalrecordRepositoryMock).add(medicalrecordToSave);

		//Act
		assertThrows(BusinessResourceException.class,()->medicalrecordServiceCUT.saveMedicalrecord(medicalrecordToSave));
	}

	@Test
	@DisplayName("Update Medicalrecord: success case")
	void testUpdateMedicalrecord() throws Exception {
		//Arrange
		Medicalrecord medicalrecordToUpdate = new Medicalrecord(
				"John", 
				"Doe", 
				LocalDate.of(1984, 3, 6),
				new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
				new ArrayList<> (Arrays.asList("fakeAllergy1"))
				);
		Medicalrecord medicalrecordUpdated = new Medicalrecord(
				"John", 
				"Doe", 
				LocalDate.of(1990, 12, 10),
				new ArrayList<> (Arrays.asList("fakeMedic1")),
				new ArrayList<> (Arrays.asList("fakeAllergy1","fakeAllergy2"))
				);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John", "Doe"))
		.thenReturn(medicalrecordToUpdate)//The medicalrecord is in our data
		.thenReturn(medicalrecordUpdated);//At the end the medicalrecord is present
		when(medicalrecordRepositoryMock.update(medicalrecordUpdated)).thenReturn(true);

		//Act
		Medicalrecord result = medicalrecordServiceCUT.updateMedicalrecord(medicalrecordUpdated);

		//Assert
		assertNotNull(result);
		assertEquals(medicalrecordUpdated,result);
		verify(medicalrecordRepositoryMock, times(1)).update(any(Medicalrecord.class));
	}

	@Test
	@DisplayName("Update Medicalrecord: fail case Medicalrecord does not exist in data")
	void testUpdateMedicalrecord_MedicalrecordDoesNotExist_BusinessResourceException() throws Exception {
		//Arrange
		Medicalrecord medicalrecordToUpdate = new Medicalrecord(
				"John", 
				"Doe", 
				LocalDate.of(1984, 3, 6),
				new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
				new ArrayList<> (Arrays.asList("fakeAllergy1"))
				);
		Medicalrecord medicalrecordUpdated = new Medicalrecord(
				"John", 
				"Doe", 
				LocalDate.of(1990, 12, 10),
				new ArrayList<> (Arrays.asList("fakeMedic1")),
				new ArrayList<> (Arrays.asList("fakeAllergy1","fakeAllergy2"))
				);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John", "Doe")).thenReturn(null); //Medicalrecord does not exist in data

		//Act
		assertThrows(BusinessResourceException.class,()->medicalrecordServiceCUT.updateMedicalrecord(medicalrecordUpdated));
	}
	
	@Test
	@DisplayName("Update Medicalrecord: fail case cause multiple Medicalrecord already exists in data")
	void testUpdateMedicalrecord_doublesFirstnameLastname_IllegalStateException() throws Exception {
		//Arrange
		Medicalrecord medicalrecordToUpdate = new Medicalrecord(
				"John", 
				"Doe", 
				LocalDate.of(1984, 3, 6),
				new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
				new ArrayList<> (Arrays.asList("fakeAllergy1"))
				);
		Medicalrecord medicalrecordUpdated = new Medicalrecord(
				"John", 
				"Doe", 
				LocalDate.of(1990, 12, 10),
				new ArrayList<> (Arrays.asList("fakeMedic1")),
				new ArrayList<> (Arrays.asList("fakeAllergy1","fakeAllergy2"))
				);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John", "Doe")).thenThrow(IllegalStateException.class);

		//Act
		assertThrows(BusinessResourceException.class,()->medicalrecordServiceCUT.updateMedicalrecord(medicalrecordUpdated));
	}
	
	@Test
	@DisplayName("Update Medicalrecord: fail case cause Medicalrecord object is incomplete")
	void testUpdateMedicalrecord_incompleteMedicalrecord_BusinessResourceException() throws Exception {
		//Arrange
		Medicalrecord medicalrecordToUpdate = new Medicalrecord(
				"John", 
				"Doe", 
				LocalDate.of(1984, 3, 6),
				new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
				new ArrayList<> (Arrays.asList("fakeAllergy1"))
				);
		Medicalrecord medicalrecordUpdated = new Medicalrecord(
				"John", 
				"Doe", 
				null,
				null,
				null);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John", "Doe")).thenReturn(medicalrecordToUpdate); //Medicalrecord already exist
		doThrow(BusinessResourceException.class).when(medicalrecordRepositoryMock).update(medicalrecordUpdated);

		//Act
		assertThrows(BusinessResourceException.class,()->medicalrecordServiceCUT.updateMedicalrecord(medicalrecordUpdated));
	}

	@Test
	@DisplayName("Delete Medicalrecord: success case")
	void testDeleteMedicalrecord() throws Exception {
		//Arrange
		when(medicalrecordRepositoryMock.delete("John", "Doe")).thenReturn(true);

		//Act
		medicalrecordServiceCUT.deleteMedicalrecord("John", "Doe");

		//Assert
		verify(medicalrecordRepositoryMock, times(1)).delete("John", "Doe");

	}
	
	@Test
	@DisplayName("Delete Medicalrecord: fail case cause nothing has been deleted")
	void testDeleteMedicalrecord_nothingDeleted_BusinessResourceException() throws Exception {
		//Arrange
		when(medicalrecordRepositoryMock.delete("John", "Doe")).thenReturn(false);

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->medicalrecordServiceCUT.deleteMedicalrecord("John", "Doe"));
	}
	
	@Test
	@DisplayName("Delete Medicalrecord: fail case cause UnsupportedOperationException occured")
	void testDeleteMedicalrecord_nothingDeleted_Exception() throws Exception {
		//Arrange
		when(medicalrecordRepositoryMock.delete("John", "Doe")).thenThrow(UnsupportedOperationException.class);

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->medicalrecordServiceCUT.deleteMedicalrecord("John", "Doe"));
	}
	
}
