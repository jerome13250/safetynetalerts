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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

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
import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.service.MedicalrecordServiceImpl;

//@WebMvcTest annotation is used for Spring MVC tests. 
//It disables full auto-configuration and instead apply only configuration relevant to MVC tests.
//It auto-configures MockMvc instance as well.
@WebMvcTest(controllers = MedicalrecordController.class)  // we are asking to initialize only one web controller
class MedicalrecordControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MedicalrecordServiceImpl medicalrecordService;

	private ObjectMapper objectMapper;
	private Medicalrecord medicalrecord;

	@BeforeEach
	void setup() {
		objectMapper = new ObjectMapper();
		medicalrecord = new Medicalrecord(
				"John",
				"Doe",
				LocalDate.of(1984, 3, 6),
				new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
				new ArrayList<> (Arrays.asList("fakeAllergy1"))
				);
	}


	@Test
	void testPostMedicalrecord() throws Exception {
		//Arrange
		Medicalrecord medicalrecordToSave = new Medicalrecord(
				"John",
				"Doe",
				LocalDate.of(1984, 3, 6),
				new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
				new ArrayList<> (Arrays.asList("fakeAllergy1"))
				);
		String jsonContent = objectMapper.writeValueAsString(medicalrecordToSave);
		when(medicalrecordService.saveMedicalrecord(any(Medicalrecord.class))).thenReturn(medicalrecord);

		//Act
		MvcResult result = mockMvc
				.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
				.andExpect(status().isCreated()).andReturn();

		//Assert
		verify(medicalrecordService).saveMedicalrecord(any(Medicalrecord.class));
		Medicalrecord medicalrecordResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Medicalrecord>() {});
		assertNotNull(medicalrecordResult);
		assertEquals(medicalrecordToSave, medicalrecordResult);
	}

	@Test
	void testPutMedicalrecord() throws Exception {
		//Arrange
		Medicalrecord medicalrecordToUpdate = new Medicalrecord(
				"John",
				"Doe",
				LocalDate.of(1984, 3, 6),
				new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
				new ArrayList<> (Arrays.asList("fakeAllergy1"))
				);
		String jsonContent = objectMapper.writeValueAsString(medicalrecordToUpdate);
		Medicalrecord medicalrecordUpdated = new Medicalrecord(
				"John",
				"Doe",
				LocalDate.of(1990, 10, 12),
				new ArrayList<> (Arrays.asList("fakeMedic1")),
				new ArrayList<> (Arrays.asList("fakeAllergy1","fakeAllergy2"))
				);
		when(medicalrecordService.updateMedicalrecord(any(Medicalrecord.class))).thenReturn(medicalrecordUpdated);

		//Act
		MvcResult result = mockMvc
				.perform(put("/medicalRecord").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
				.andExpect(status().isOk()).andReturn();

		//Assert
		verify(medicalrecordService).updateMedicalrecord(any(Medicalrecord.class));
		Medicalrecord medicalrecordResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Medicalrecord>() {});
		assertNotNull(medicalrecordResult);
		assertEquals(medicalrecordUpdated, medicalrecordResult);
	}
	
	@Test
	void testDeleteMedicalrecord() throws Exception {
		MvcResult result = mockMvc.perform(delete("/medicalRecord")
				.param("firstname", "John")
				.param("lastname", "Doe"))
				.andExpect(status().isGone()).andReturn();
		verify(medicalrecordService).deleteMedicalrecord("John", "Doe");
	}
	
	@Test
	void testDeleteMedicalrecord_Unknown() throws Exception {
		//Arrange
		doThrow(new BusinessResourceException("DeleteMedicalrecordError", "Error deleting medicalrecord: John Unknown", HttpStatus.NOT_FOUND))
		.when(medicalrecordService).deleteMedicalrecord("John","Unknown");
		//Act
		MvcResult result = mockMvc.perform(delete("/medicalRecord")
				.param("firstname", "John")
				.param("lastname", "Unknown"))
				.andExpect(status().isNotFound()).andReturn();
		//Assert
		verify(medicalrecordService).deleteMedicalrecord("John", "Unknown");
	}
	

}
