package com.safetynet.alertsapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.alertsapp.service.SafetynetalertsService;

//@WebMvcTest annotation is used for Spring MVC tests. 
//It disables full auto-configuration and instead apply only configuration relevant to MVC tests.
//It auto-configures MockMvc instance as well.
@WebMvcTest(controllers = SafetynetalertsController.class)  // we are asking to initialize only one web controller
class SafetynetalertsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SafetynetalertsService safetynetalertsService;

	@Test
	void testGetPersonsByStationnumberString() throws Exception {
		mockMvc.perform(get("/firestation?stationNumber=3"))
		.andExpect(status().isOk());
	}

	@Test
	void testGetChildrenByAddressAndListOtherFamilyMembers() throws Exception {
		mockMvc.perform(get("/childAlert?address=adress1"))
		.andExpect(status().isOk());
	}

	@Test
	void testGetPhoneNumbersForStationNumber() throws Exception {
		mockMvc.perform(get("/phoneAlert?firestation=1"))
		.andExpect(status().isOk());
	}

	@Test
	void testGetPersonsFirestationAndMedicalRecordByAddress() throws Exception {
		mockMvc.perform(get("/fire?address=testAddress"))
		.andExpect(status().isOk());
	}

	//http://localhost:8080/flood/stations?stations=2,3
	@Test
	void testGetPersonsAndMedicalRecordByStationNumberAndAddresses() throws Exception {
		mockMvc.perform(get("/flood/stations?stations=2,3"))
		.andExpect(status().isOk());
	}

	//http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
	@Test
	void testgetPersonInfoByFirstNameAndLastName() throws Exception {
		mockMvc.perform(get("/personInfo?firstName=John&lastName=Doe"))
		.andExpect(status().isOk());
	}
	
	//http://localhost:8080/communityEmail?city=<city>
	@Test
	void testgetPhonesInCity() throws Exception {
		mockMvc.perform(get("/communityEmail?city=Gotham"))
		.andExpect(status().isOk());
	}
}
