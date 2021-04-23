package com.safetynet.alertsapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.service.FirestationService;

//@WebMvcTest annotation is used for Spring MVC tests. 
//It disables full auto-configuration and instead apply only configuration relevant to MVC tests.
//It auto-configures MockMvc instance as well.
@WebMvcTest(controllers = FirestationController.class)  // we are asking to initialize only one web controller
public class FirestationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirestationService firestationService;

	private ObjectMapper objectMapper;
	private Firestation firestation;

	@BeforeEach
	void setup() {
		objectMapper = new ObjectMapper();
		firestation = new Firestation("address",1);
	}


	@Test
	void testPostFirestation() throws Exception {
		//Arrange
		Firestation firestationToSave = new Firestation("address",1);
		String jsonContent = objectMapper.writeValueAsString(firestationToSave);
		when(firestationService.saveFirestation(any(Firestation.class))).thenReturn(firestation);

		//Act
		MvcResult result = mockMvc
				.perform(post("/firestation").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
				.andExpect(status().isCreated()).andReturn();

		//Assert
		verify(firestationService).saveFirestation(any(Firestation.class));
		Firestation firestationResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Firestation>() {});
		assertNotNull(firestationResult);
		assertEquals(firestationToSave, firestationResult);
	}

	@Test
	void testPutFirestation() throws Exception {
		//Arrange
		Firestation firestationToUpdate = new Firestation("address",1);
		String jsonContent = objectMapper.writeValueAsString(firestationToUpdate);
		Firestation firestationUpdated = new Firestation("address",2);
		when(firestationService.updateFirestation(any(Firestation.class))).thenReturn(firestationUpdated);

		//Act
		MvcResult result = mockMvc
				.perform(put("/firestation").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
				.andExpect(status().isOk()).andReturn();

		//Assert
		verify(firestationService).updateFirestation(any(Firestation.class));
		Firestation firestationResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Firestation>() {});
		assertNotNull(firestationResult);
		assertEquals(firestationUpdated.getStation(), firestationResult.getStation());
	}
	
	@Test
	void testDeleteFirestationByAddress() throws Exception {
		MvcResult result = mockMvc.perform(delete("/firestation")
				.param("address", "address"))
				.andExpect(status().isGone()).andReturn();
		verify(firestationService).deleteFirestationByAddress("address");
	}
	
	@Test
	void testDeleteFirestationByAddress_Unknown() throws Exception {
		//Arrange
		doThrow(new BusinessResourceException("DeleteFirestationError", "Error deleting firestation: John Unknown", HttpStatus.NOT_FOUND))
		.when(firestationService).deleteFirestationByAddress("address");
		//Act
		MvcResult result = mockMvc.perform(delete("/firestation")
				.param("address", "address"))
				.andExpect(status().isNotFound()).andReturn();
		//Assert
		verify(firestationService).deleteFirestationByAddress("address");
	}
	
	@Test
	void testDeleteFirestationByStation() throws Exception {
		MvcResult result = mockMvc.perform(delete("/firestation")
				.param("station", "1"))
				.andExpect(status().isGone()).andReturn();
		verify(firestationService).deleteFirestationByStation(1);
	}
	
	@Test
	void testDeleteFirestationByStation_Unknown() throws Exception {
		//Arrange
		doThrow(new BusinessResourceException("DeleteFirestationError", "Error deleting firestation: 1", HttpStatus.NOT_FOUND))
		.when(firestationService).deleteFirestationByStation(1);
		//Act
		MvcResult result = mockMvc.perform(delete("/firestation")
				.param("station", "1"))
				.andExpect(status().isNotFound()).andReturn();
		//Assert
		verify(firestationService).deleteFirestationByStation(1);
	}
	
	
}
