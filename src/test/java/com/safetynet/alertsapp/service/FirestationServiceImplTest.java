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
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.repository.IFirestationRepository;

@ExtendWith(MockitoExtension.class)
class FirestationServiceImplTest {

	private final Logger logger = LoggerFactory.getLogger(FirestationServiceImplTest.class);

	@InjectMocks
	FirestationServiceImpl firestationServiceCUT;

	@Mock
	IFirestationRepository firestationRepositoryMock;

	@Test
	@DisplayName("Save Firestation: success case")
	void testSaveFirestation() throws Exception {
		//Arrange
		Firestation firestationToSave = new Firestation("NewAddress", 1);    
		Firestation firestationSaved = new Firestation("NewAddress", 1); 
		when(firestationRepositoryMock.getByAddress("NewAddress"))
		.thenReturn(1);//At the end the firestation is present, it returns the station number
		when(firestationRepositoryMock.add(firestationToSave)).thenReturn(true);

		//Act
		Firestation result = firestationServiceCUT.saveFirestation(firestationToSave);

		//Assert
		assertNotNull(result);
		assertEquals(firestationToSave,result);
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
	@DisplayName("Save Firestation: fail case cause multiple Firestation already exists in data")
	void testSaveFirestation_doublesFirstnameLastname_IllegalStateException() throws Exception {
		//Arrange
		Firestation firestationToSave = new Firestation("NewAddress", 1);
		when(firestationRepositoryMock.getByAddress("NewAddress")).thenThrow(IllegalStateException.class);

		//Act
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.saveFirestation(firestationToSave));
	}

	@Test
	@DisplayName("Save Firestation: fail case cause Firestation object is incomplete")
	void testSaveFirestation_incompleteFirestation_BusinessResourceException() throws Exception {
		//Arrange
		Firestation firestationToSave = new Firestation("NewAddress", 1);
		doThrow(BusinessResourceException.class).when(firestationRepositoryMock).add(firestationToSave);

		//Act
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.saveFirestation(firestationToSave));
	}

	@Test
	@DisplayName("Update Firestation: success case")
	void testUpdateFirestation() throws Exception {
		//Arrange
		Firestation firestationToUpdate = new Firestation("address", 1);
		Firestation firestationUpdated = new Firestation("address", 2); 
		when(firestationRepositoryMock.getByAddress("address"))
		.thenReturn(2);//At the end the firestation is present
		when(firestationRepositoryMock.update(firestationUpdated)).thenReturn(true);

		//Act
		Firestation result = firestationServiceCUT.updateFirestation(firestationUpdated);

		//Assert
		assertNotNull(result);
		assertEquals(firestationUpdated,result);
		verify(firestationRepositoryMock, times(1)).update(any(Firestation.class));
	}

	@Test
	@DisplayName("Update Firestation: fail case Firestation does not exist in data")
	void testUpdateFirestation_FirestationDoesNotExist_BusinessResourceException() throws Exception {
		//Arrange
		Firestation firestationToUpdate = new Firestation("address", 1);
		Firestation firestationUpdated = new Firestation("address", 2);  
		when(firestationRepositoryMock.getByAddress("address")).thenThrow(BusinessResourceException.class); //Firestation does not exist in data

		//Act
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.updateFirestation(firestationUpdated));
	}
	
	@Test
	@DisplayName("Update Firestation: fail case cause multiple Firestation already exists in data")
	void testUpdateFirestation_doublesFirstnameLastname_IllegalStateException() throws Exception {
		//Arrange
		Firestation firestationToUpdate = new Firestation("address", 1);
		Firestation firestationUpdated = new Firestation("address", 2);  
		when(firestationRepositoryMock.getByAddress("address")).thenThrow(IllegalStateException.class);

		//Act
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.updateFirestation(firestationUpdated));
	}
	
	@Test
	@DisplayName("Update Firestation: fail case cause Firestation object is incomplete")
	void testUpdateFirestation_incompleteFirestation_BusinessResourceException() throws Exception {
		//Arrange
		Firestation firestationToUpdate = new Firestation("address", 1);
		Firestation firestationUpdated = new Firestation("address", null); 
		doThrow(BusinessResourceException.class).when(firestationRepositoryMock).update(firestationUpdated);

		//Act
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.updateFirestation(firestationUpdated));
	}

	@Test
	@DisplayName("Delete Firestation by address: success case")
	void testDeleteFirestationByAddress() throws Exception {
		//Arrange
		when(firestationRepositoryMock.deleteByAddress("address")).thenReturn(true);

		//Act
		firestationServiceCUT.deleteFirestationByAddress("address");

		//Assert
		verify(firestationRepositoryMock, times(1)).deleteByAddress("address");

	}

	@Test
	@DisplayName("Delete Firestation by address: fail case cause Firestation is unknown")
	void testDeleteFirestationByAddress_firestationUnknown() throws Exception {
		//Arrange
		when(firestationRepositoryMock.deleteByAddress("address")).thenThrow(BusinessResourceException.class);

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.deleteFirestationByAddress("address"));
	}
	
	@Test
	@DisplayName("Delete Firestation by address: fail case cause UnsupportedOperationException occured")
	void testDeleteFirestationByAddress_nothingDeleted_Exception() throws Exception {
		//Arrange
		when(firestationRepositoryMock.deleteByAddress("address")).thenThrow(UnsupportedOperationException.class);

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.deleteFirestationByAddress("address"));
	}

	@Test
	@DisplayName("Delete Firestation by station: success case")
	void testDeleteFirestationByStation() throws Exception {
		//Arrange
		when(firestationRepositoryMock.deleteByStation(1)).thenReturn(true);

		//Act
		firestationServiceCUT.deleteFirestationByStation(1);

		//Assert
		verify(firestationRepositoryMock, times(1)).deleteByStation(1);

	}

	@Test
	@DisplayName("Delete Firestation by station: fail case cause Firestation is unknown")
	void testDeleteFirestationByStation_firestationUnknown() throws Exception {
		//Arrange
		when(firestationRepositoryMock.deleteByStation(1)).thenThrow(BusinessResourceException.class);

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.deleteFirestationByStation(1));
	}
	
	@Test
	@DisplayName("Delete Firestation by station: fail case cause UnsupportedOperationException occured")
	void testDeleteFirestationByStation_nothingDeleted_Exception() throws Exception {
		//Arrange
		when(firestationRepositoryMock.deleteByStation(1)).thenThrow(UnsupportedOperationException.class);

		//Act-Assert
		assertThrows(BusinessResourceException.class,()->firestationServiceCUT.deleteFirestationByStation(1));
	}

	
	

}
