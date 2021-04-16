package com.safetynet.alertsapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.service.SafetynetalertsService;

@RestController
public class SafetynetalertsController {

	@Autowired
	private SafetynetalertsService safetynetalertsService;

	@GetMapping("/firestationMap")
	public Map<String, Object> GetPersonsByStationnumberMap(@RequestParam(required=true) Integer stationNumber) {
		return safetynetalertsService.getPersonsByStationnumberMap(stationNumber);
	}

	@GetMapping("/firestation")
	public String GetPersonsByStationnumberString(@RequestParam(required=true) Integer stationNumber) {
		return safetynetalertsService.getPersonsByStationnumberString(stationNumber);
	}

	@GetMapping("/childAlert")
	public String getChildrenByAddressAndListOtherFamilyMembers(@RequestParam(required=true) String address) {
		return safetynetalertsService.getChildrenByAddressAndListOtherFamilyMembers(address);
	}

	@GetMapping("/phoneAlert")
	public String getPhoneNumbersForStationNumber(@RequestParam(required=true) Integer firestation) {
		return safetynetalertsService.getPhoneNumbersForStationNumber(firestation);
	}

	//http://localhost:8080/fire?address=<address>
	@GetMapping("/fire")
	public String getPersonsFirestationAndMedicalRecordByAddress(@RequestParam(required=true) String address) {
		return safetynetalertsService.getPersonsFirestationAndMedicalRecordByAddress(address);
	}

	//http://localhost:8080/flood/stations?stations=<a list of station_numbers>
	@GetMapping("/flood/stations")
	public String getPersonsAndMedicalRecordByStationNumberAndAddresses(@RequestParam(required=true) List<Integer> stations) {
		return safetynetalertsService.getAddressesListOfPersonsPerStationNumberList(stations);
	}

}
