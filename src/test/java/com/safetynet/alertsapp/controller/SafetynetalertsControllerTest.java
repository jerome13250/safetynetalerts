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
public class SafetynetalertsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SafetynetalertsService safetynetalertsService;

	@Test
	public void testGetPersonsByFirestationId() throws Exception {
		mockMvc.perform(get("/firestation?stationNumber=3"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void testGetChildrenByAddressAndListOtherFamilyMembers() throws Exception {
		mockMvc.perform(get("/childAlert?address=adress1"))
		.andExpect(status().isOk());
	}

}
