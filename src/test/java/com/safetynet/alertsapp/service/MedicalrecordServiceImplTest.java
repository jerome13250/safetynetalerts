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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.repository.IMedicalrecordRepository;

@ExtendWith(MockitoExtension.class)
class MedicalrecordServiceImplTest {

	@InjectMocks
	MedicalrecordServiceImpl medicalrecordServiceCUT;

	@Mock
	IMedicalrecordRepository medicalrecordRepositoryMock;

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
		.thenReturn(medicalrecordSaved);//At the end the medicalrecord is present
		doNothing().when(medicalrecordRepositoryMock).add(medicalrecordToSave);

		//Act
		Medicalrecord result = medicalrecordServiceCUT.saveMedicalrecord(medicalrecordToSave);

		//Assert
		assertNotNull(result);
		assertEquals(medicalrecordToSave,result);
		verify(medicalrecordRepositoryMock, times(1)).add(any(Medicalrecord.class));

	}

	@Test
	@DisplayName("Save Medicalrecord: fail cause BusinessResourceException")
	void testSaveMedicalrecord_BusinessResourceException() throws Exception {
		//Arrange
		Medicalrecord medicalrecordToSave = new Medicalrecord("New","NewName",LocalDate.of(1984, 3, 6),new ArrayList<>(),new ArrayList<>());
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("New", "NewName")).thenThrow(BusinessResourceException.class);

		//Act
		assertThrows(BusinessResourceException.class,()->medicalrecordServiceCUT.saveMedicalrecord(medicalrecordToSave));
	}

	@Test
	@DisplayName("Save Medicalrecord: fail cause IllegalStateException")
	void testSaveMedicalrecord_IllegalStateException() throws Exception {
		//Arrange
		Medicalrecord medicalrecordToSave = new Medicalrecord("New","NewName",LocalDate.of(1984, 3, 6),new ArrayList<>(),new ArrayList<>());
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("New", "NewName")).thenThrow(IllegalStateException.class);

		//Act
		assertThrows(BusinessResourceException.class,()->medicalrecordServiceCUT.saveMedicalrecord(medicalrecordToSave));
	}

	@Test
	@DisplayName("Update Medicalrecord: success case")
	void testUpdateMedicalrecord() throws Exception {
		//Arrange
		Medicalrecord medicalrecordToUpdate = new Medicalrecord("John","Doe",LocalDate.of(1984, 3, 6),new ArrayList<>(),new ArrayList<>());
		Medicalrecord medicalrecordExpected = new Medicalrecord("John","Doe",LocalDate.of(1984, 3, 6),new ArrayList<>(),new ArrayList<>());
		doNothing().when(medicalrecordRepositoryMock).update(medicalrecordToUpdate);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John", "Doe"))
		.thenReturn(medicalrecordToUpdate);//At the end the medicalrecord is present

		//Act
		Medicalrecord result = medicalrecordServiceCUT.updateMedicalrecord(medicalrecordToUpdate);

		//Assert
		assertNotNull(result);
		assertEquals(medicalrecordExpected,result);
		verify(medicalrecordRepositoryMock, times(1)).update(any(Medicalrecord.class));
	}

	@Test
	@DisplayName("Update Medicalrecord: fail cause BusinessResourceException")
	void testUpdateMedicalrecord_BusinessResourceException() throws Exception {
		//Arrange
		Medicalrecord medicalrecordUnknown = new Medicalrecord("Unknown","Guy",LocalDate.of(1984, 3, 6),new ArrayList<>(),new ArrayList<>());
		doThrow(BusinessResourceException.class).when(medicalrecordRepositoryMock).update(medicalrecordUnknown);

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->medicalrecordServiceCUT.updateMedicalrecord(medicalrecordUnknown));
	}
	
	@Test
	@DisplayName("Update Medicalrecord: fail cause IllegalStateException")
	void testUpdateMedicalrecord_IllegalStateException() throws Exception {
		//Arrange
		Medicalrecord medicalrecordToUpdate = new Medicalrecord(
				"John", 
				"Doe", 
				LocalDate.of(1984, 3, 6),
				new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
				new ArrayList<> (Arrays.asList("fakeAllergy1"))
				);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John", "Doe")).thenThrow(IllegalStateException.class);

		//Act
		assertThrows(BusinessResourceException.class,()->medicalrecordServiceCUT.updateMedicalrecord(medicalrecordToUpdate));
	}
	
	@Test
	@DisplayName("Delete Medicalrecord: success case")
	void testDeleteMedicalrecord() throws Exception {
		//Arrange
		doNothing().when(medicalrecordRepositoryMock).delete("John", "Doe");

		//Act
		medicalrecordServiceCUT.deleteMedicalrecord("John", "Doe");

		//Assert
		verify(medicalrecordRepositoryMock, times(1)).delete("John", "Doe");

	}
	
	@Test
	@DisplayName("Delete Medicalrecord: fail case cause nothing has been deleted")
	void testDeleteMedicalrecord_nothingDeleted_BusinessResourceException() throws Exception {
		//Arrange
		doThrow(BusinessResourceException.class).when(medicalrecordRepositoryMock).delete("John", "Doe");

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->medicalrecordServiceCUT.deleteMedicalrecord("John", "Doe"));
	}
	
	@Test
	@DisplayName("Delete Medicalrecord: fail case cause UnsupportedOperationException occured")
	void testDeleteMedicalrecord_UnsupportedOperationException() throws Exception {
		//Arrange
		doThrow(UnsupportedOperationException.class).when(medicalrecordRepositoryMock).delete("John", "Doe");

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->medicalrecordServiceCUT.deleteMedicalrecord("John", "Doe"));
	}
	
}
