package com.safetynet.alertsapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.safetynet.alertsapp.service.ISafetynetalertsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@Api(value = "Safety Net Alerts GET data Controller")
public class SafetynetalertsController {

	private static final Logger logger = LoggerFactory.getLogger(SafetynetalertsController.class);
	
	@Autowired
	private ISafetynetalertsService safetynetalertsService;

	@GetMapping("/firestation")
	@ApiOperation(value = "View a list of persons living in a specific firestation zone, also returns number of aduls and children", response = List.class)
	public ResponseEntity<JsonNode> getPersonsByStationnumber(@RequestParam(required=true) Integer stationNumber) {
		logger.info("GET /firestation called");
		JsonNode jsonNode = safetynetalertsService.getPersonsByStationnumber(stationNumber);
		logger.info("GET /firestation response : OK");
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	@GetMapping("/childAlert")
	public ResponseEntity<JsonNode> getChildrenByAddressAndListOtherFamilyMembers(@RequestParam(required=true) String address) {
		logger.info("GET /childAlert called");
		JsonNode jsonNode = safetynetalertsService.getChildrenByAddressAndListOtherFamilyMembers(address);
		logger.info("GET /childAlert response : OK");
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	@GetMapping("/phoneAlert")
	public ResponseEntity<JsonNode> getPhoneNumbersForStationNumber(@RequestParam(required=true) Integer firestation) {
		logger.info("GET /phoneAlert called");
		JsonNode jsonNode = safetynetalertsService.getPhoneNumbersForStationNumber(firestation);
		logger.info("GET /phoneAlert response : OK");
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	//http://localhost:8080/fire?address=<address>
	@GetMapping("/fire")
	public ResponseEntity<JsonNode> getPersonsFirestationAndMedicalRecordByAddress(@RequestParam(required=true) String address) {
		logger.info("GET /fire called");
		JsonNode jsonNode = safetynetalertsService.getPersonsFirestationAndMedicalRecordByAddress(address);
		logger.info("GET /fire response : OK");
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	//http://localhost:8080/flood/stations?stations=<a list of station_numbers>
	@GetMapping("/flood/stations")
	public ResponseEntity<JsonNode> getPersonsAndMedicalRecordByStationNumberAndAddresses(@RequestParam(required=true) List<Integer> stations) {
		logger.info("GET /flood/stations called");
		JsonNode jsonNode = safetynetalertsService.getAddressesListOfPersonsPerStationNumberList(stations);
		logger.info("GET /flood/stations response : OK");
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	//http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
	@GetMapping("/personInfo")
	public ResponseEntity<JsonNode> getPersonInfoByFirstNameAndLastName(@RequestParam(required=true) String firstName, @RequestParam(required=true) String lastName ) {
		logger.info("GET /personInfo called");
		JsonNode jsonNode = safetynetalertsService.getPersonInfoByFirstNameAndLastName(firstName, lastName);
		logger.info("GET /personInfo response : OK");
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	//http://localhost:8080/communityEmail?city=<city>
	@GetMapping("/communityEmail")
	public ResponseEntity<JsonNode> getPhonesInCity(@RequestParam(required=true) String city ) {
		logger.info("GET /communityEmail called");
		JsonNode jsonNode = safetynetalertsService.getPhonesInCity(city);
		logger.info("GET /communityEmail response : OK");
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

}
