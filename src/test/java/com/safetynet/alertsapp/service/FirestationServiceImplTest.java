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
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.repository.IFirestationRepository;

@ExtendWith(MockitoExtension.class)
class FirestationServiceImplTest {

	@InjectMocks
	FirestationServiceImpl firestationServiceCUT;

	@Mock
	IFirestationRepository firestationRepositoryMock;

	@Test
	@DisplayName("Save Firestation: success case")
	void testSaveFirestation() throws Exception {
		//Arrange
		Firestation firestationToSave = new Firestation("NewAddress", 1);    
		Firestation firestationExpected = new Firestation("NewAddress", 1); 
		doNothing().when(firestationRepositoryMock).add(firestationToSave);
		when(firestationRepositoryMock.getByAddress("NewAddress")).thenReturn(1);//it returns the station number

		//Act
		Firestation result = firestationServiceCUT.saveFirestation(firestationToSave);

		//Assert
		assertNotNull(result);
		assertEquals(firestationExpected,result);
		verify(firestationRepositoryMock, times(1)).add(any(Firestation.class));

	}

	@Test
	@DisplayName("Save Firestation: fail case Firestation address already exists in data")
	void testSaveFirestation_FirestationAlreadyExist_BusinessResourceException() throws Exception {
		//Arrange
		Firestation firestationToSave = new Firestation("NewAddress", 1);
		when(firestationRepositoryMock.getByAddress("NewAddress")).thenThrow(BusinessResourceException.class); //Firestation already exist

		//Act
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.saveFirestation(firestationToSave));
	}

	@Test
	@DisplayName("Save Firestation: fail cause IllegalStateException")
	void testSaveFirestation_doublesFirstnameLastname_IllegalStateException() throws Exception {
		//Arrange
		Firestation firestationToSave = new Firestation("NewAddress", 1);
		when(firestationRepositoryMock.getByAddress("NewAddress")).thenThrow(IllegalStateException.class);

		//Act
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.saveFirestation(firestationToSave));
	}

	@Test
	@DisplayName("Update Firestation: success case")
	void testUpdateFirestation() throws Exception {
		//Arrange
		Firestation firestationToUpdate = new Firestation("address", 2);
		Firestation firestationUpdated = new Firestation("address", 2); 
		doNothing().when(firestationRepositoryMock).update(firestationToUpdate);
		when(firestationRepositoryMock.getByAddress("address")).thenReturn(2);//the firestation is present, number=2

		//Act
		Firestation result = firestationServiceCUT.updateFirestation(firestationToUpdate);

		//Assert
		assertNotNull(result);
		assertEquals(firestationUpdated,result);
		verify(firestationRepositoryMock, times(1)).update(any(Firestation.class));
	}

	@Test
	@DisplayName("Update Firestation: fail cause BusinessResourceException")
	void testUpdateFirestation_FirestationDoesNotExist_BusinessResourceException() throws Exception {
		//Arrange
		Firestation firestationToUpdate = new Firestation("address", 2);
		when(firestationRepositoryMock.getByAddress("address")).thenThrow(BusinessResourceException.class); //Firestation does not exist in data

		//Act
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.updateFirestation(firestationToUpdate));
	}
	
	@Test
	@DisplayName("Update Firestation: fail cause IllegalStateException")
	void testUpdateFirestation_doublesFirstnameLastname_IllegalStateException() throws Exception {
		//Arrange
		Firestation firestationToUpdate = new Firestation("address", 1);
		when(firestationRepositoryMock.getByAddress("address")).thenThrow(IllegalStateException.class);

		//Act
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.updateFirestation(firestationToUpdate));
	}
	
	@Test
	@DisplayName("Delete Firestation by address: success case")
	void testDeleteFirestationByAddress() throws Exception {
		//Arrange
		doNothing().when(firestationRepositoryMock).deleteByAddress("address");

		//Act
		firestationServiceCUT.deleteFirestationByAddress("address");

		//Assert
		verify(firestationRepositoryMock, times(1)).deleteByAddress("address");

	}

	@Test
	@DisplayName("Delete Firestation by address: fail cause BusinessResourceException")
	void testDeleteFirestationByAddress_firestationUnknown() throws Exception {
		//Arrange
		doThrow(BusinessResourceException.class).when(firestationRepositoryMock).deleteByAddress("address");

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.deleteFirestationByAddress("address"));
	}
	
	@Test
	@DisplayName("Delete Firestation by address: fail case cause UnsupportedOperationException")
	void testDeleteFirestationByAddress_UnsupportedOperationException() throws Exception {
		//Arrange
		doThrow(UnsupportedOperationException.class).when(firestationRepositoryMock).deleteByAddress("address");

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.deleteFirestationByAddress("address"));
	}

	@Test
	@DisplayName("Delete Firestation by station: success case")
	void testDeleteFirestationByStation() throws Exception {
		//Arrange
		doNothing().when(firestationRepositoryMock).deleteByStation(1);

		//Act
		firestationServiceCUT.deleteFirestationByStation(1);

		//Assert
		verify(firestationRepositoryMock, times(1)).deleteByStation(1);

	}

	@Test
	@DisplayName("Delete Firestation by station: fail case cause Firestation is unknown")
	void testDeleteFirestationByStation_firestationUnknown() throws Exception {
		//Arrange
		doThrow(BusinessResourceException.class).when(firestationRepositoryMock).deleteByStation(1);

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.deleteFirestationByStation(1));
	}
	
	@Test
	@DisplayName("Delete Firestation by station: fail case cause UnsupportedOperationException occured")
	void testDeleteFirestationByStation_nothingDeleted_Exception() throws Exception {
		//Arrange
		doThrow(UnsupportedOperationException.class).when(firestationRepositoryMock).deleteByStation(1);

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.deleteFirestationByStation(1));
	}

}
