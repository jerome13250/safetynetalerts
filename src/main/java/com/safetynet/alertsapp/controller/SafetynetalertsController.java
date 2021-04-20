package com.safetynet.alertsapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.service.SafetynetalertsService;

@RestController
public class SafetynetalertsController {

	@Autowired
	private SafetynetalertsService safetynetalertsService;

	@GetMapping("/firestation")
	public JsonNode getPersonsByStationnumber(@RequestParam(required=true) Integer stationNumber) {
		return safetynetalertsService.getPersonsByStationnumber(stationNumber);
	}

	@GetMapping("/childAlert")
	public JsonNode getChildrenByAddressAndListOtherFamilyMembers(@RequestParam(required=true) String address) {
		return safetynetalertsService.getChildrenByAddressAndListOtherFamilyMembers(address);
	}

	@GetMapping("/phoneAlert")
	public JsonNode getPhoneNumbersForStationNumber(@RequestParam(required=true) Integer firestation) {
		return safetynetalertsService.getPhoneNumbersForStationNumber(firestation);
	}

	//http://localhost:8080/fire?address=<address>
	@GetMapping("/fire")
	public JsonNode getPersonsFirestationAndMedicalRecordByAddress(@RequestParam(required=true) String address) {
		return safetynetalertsService.getPersonsFirestationAndMedicalRecordByAddress(address);
	}

	//http://localhost:8080/flood/stations?stations=<a list of station_numbers>
	@GetMapping("/flood/stations")
	public JsonNode getPersonsAndMedicalRecordByStationNumberAndAddresses(@RequestParam(required=true) List<Integer> stations) {
		return safetynetalertsService.getAddressesListOfPersonsPerStationNumberList(stations);
	}

	//http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
	@GetMapping("/personInfo")
	public JsonNode getPersonInfoByFirstNameAndLastName(@RequestParam(required=true) String firstName, @RequestParam(required=true) String lastName ) {
		return safetynetalertsService.getPersonInfoByFirstNameAndLastName(firstName, lastName);
	}

	//http://localhost:8080/communityEmail?city=<city>
	@GetMapping("/communityEmail")
	public JsonNode getPhonesInCity(@RequestParam(required=true) String city ) {
		return safetynetalertsService.getPhonesInCity(city);
	}

}
