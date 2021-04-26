package com.safetynet.alertsapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.safetynet.alertsapp.service.SafetynetalertsService;

@RestController
public class SafetynetalertsController {

	@Autowired
	private SafetynetalertsService safetynetalertsService;

	@GetMapping("/firestation")
	public ResponseEntity<JsonNode> getPersonsByStationnumber(@RequestParam(required=true) Integer stationNumber) {
		JsonNode jsonNode = safetynetalertsService.getPersonsByStationnumber(stationNumber);
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	@GetMapping("/childAlert")
	public ResponseEntity<JsonNode> getChildrenByAddressAndListOtherFamilyMembers(@RequestParam(required=true) String address) {
		JsonNode jsonNode = safetynetalertsService.getChildrenByAddressAndListOtherFamilyMembers(address);
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	@GetMapping("/phoneAlert")
	public ResponseEntity<JsonNode> getPhoneNumbersForStationNumber(@RequestParam(required=true) Integer firestation) {
		JsonNode jsonNode = safetynetalertsService.getPhoneNumbersForStationNumber(firestation);
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	//http://localhost:8080/fire?address=<address>
	@GetMapping("/fire")
	public ResponseEntity<JsonNode> getPersonsFirestationAndMedicalRecordByAddress(@RequestParam(required=true) String address) {
		JsonNode jsonNode = safetynetalertsService.getPersonsFirestationAndMedicalRecordByAddress(address);
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	//http://localhost:8080/flood/stations?stations=<a list of station_numbers>
	@GetMapping("/flood/stations")
	public ResponseEntity<JsonNode> getPersonsAndMedicalRecordByStationNumberAndAddresses(@RequestParam(required=true) List<Integer> stations) {
		JsonNode jsonNode = safetynetalertsService.getAddressesListOfPersonsPerStationNumberList(stations);
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	//http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
	@GetMapping("/personInfo")
	public ResponseEntity<JsonNode> getPersonInfoByFirstNameAndLastName(@RequestParam(required=true) String firstName, @RequestParam(required=true) String lastName ) {
		JsonNode jsonNode = safetynetalertsService.getPersonInfoByFirstNameAndLastName(firstName, lastName);
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	//http://localhost:8080/communityEmail?city=<city>
	@GetMapping("/communityEmail")
	public ResponseEntity<JsonNode> getPhonesInCity(@RequestParam(required=true) String city ) {
		JsonNode jsonNode = safetynetalertsService.getPhonesInCity(city);
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

}
