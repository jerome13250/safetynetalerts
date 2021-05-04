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
	@ApiOperation(value = "Cette url doit retourner une liste des personnes couvertes par la caserne de pompiers correspondante."
			+ "Donc, si le numéro de station = 1, elle doit renvoyer les habitants couverts par la station numéro 1. La liste doit"
			+ " inclure les informations spécifiques suivantes : prénom, nom, adresse, numéro de téléphone. De plus,elle doit fournir"
			+ " un décompte du nombre d'adultes et du nombre d'enfants (tout individu âgé de 18 ans oumoins) dans la zone desservie.")
	public ResponseEntity<JsonNode> getPersonsByStationnumber(@RequestParam(required=true) Integer stationNumber) {
		logger.info("GET /firestation called");
		JsonNode jsonNode = safetynetalertsService.getPersonsByStationnumber(stationNumber);
		logger.info("GET /firestation response : OK");
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	@GetMapping("/childAlert")
	@ApiOperation(value = "Cette url doit retourner une liste d'enfants (tout individu âgé de 18 ans ou moins) habitant à cette adresse.La liste doit comprendre"
			+ " le prénom et le nom de famille de chaque enfant, son âge et une liste des autresmembres du foyer. S'il n'y a pas d'enfant, cette url"
			+ " peut renvoyer une chaîne vide.")
	public ResponseEntity<JsonNode> getChildrenByAddressAndListOtherFamilyMembers(@RequestParam(required=true) String address) {
		logger.info("GET /childAlert called");
		JsonNode jsonNode = safetynetalertsService.getChildrenByAddressAndListOtherFamilyMembers(address);
		logger.info("GET /childAlert response : OK");
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	@GetMapping("/phoneAlert")
	@ApiOperation(value = "Cette url doit retourner une liste des numéros de téléphone des résidents desservis par la caserne depompiers. Nous l'utiliserons"
			+ " pour envoyer des messages texte d'urgence à des foyers spécifiques.")
	public ResponseEntity<JsonNode> getPhoneNumbersForStationNumber(@RequestParam(required=true) Integer firestation) {
		logger.info("GET /phoneAlert called");
		JsonNode jsonNode = safetynetalertsService.getPhoneNumbersForStationNumber(firestation);
		logger.info("GET /phoneAlert response : OK");
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	//http://localhost:8080/fire?address=<address>
	@GetMapping("/fire")
	@ApiOperation(value = "Cette url doit retourner la liste des habitants vivant à l’adresse donnée ainsi que le numéro de la casernede pompiers la desservant."
			+ " La liste doit inclure le nom, le numéro de téléphone, l'âge et les antécédentsmédicaux (médicaments, posologie et allergies) de chaque personne")
	public ResponseEntity<JsonNode> getPersonsFirestationAndMedicalRecordByAddress(@RequestParam(required=true) String address) {
		logger.info("GET /fire called");
		JsonNode jsonNode = safetynetalertsService.getPersonsFirestationAndMedicalRecordByAddress(address);
		logger.info("GET /fire response : OK");
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	//http://localhost:8080/flood/stations?stations=<a list of station_numbers>
	@GetMapping("/flood/stations")
	@ApiOperation(value = "Cette url doit retourner une liste de tous les foyers desservis par la caserne. Cette liste doit regrouper lespersonnes par adresse. Elle doit aussi"
	+ " inclure le nom, le numéro de téléphone et l'âge des habitants, etfaire figurer leurs antécédents médicaux (médicaments, posologie et allergies)"
	+ " à côté de chaque nom.")
	public ResponseEntity<JsonNode> getPersonsAndMedicalRecordByStationNumberAndAddresses(@RequestParam(required=true) List<Integer> stations) {
		logger.info("GET /flood/stations called");
		JsonNode jsonNode = safetynetalertsService.getAddressesListOfPersonsPerStationNumberList(stations);
		logger.info("GET /flood/stations response : OK");
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	//http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
	@ApiOperation(value = "Cette url doit retourner le nom, l'adresse, l'âge, l'adresse mail et les antécédents médicaux (médicaments,posologie, allergies)"
			+ " de chaque habitant. Si plusieurs personnes portent le même nom, elles doiventtoutes apparaître.")
	@GetMapping("/personInfo")
	public ResponseEntity<JsonNode> getPersonInfoByFirstNameAndLastName(@RequestParam(required=true) String firstName, @RequestParam(required=true) String lastName ) {
		logger.info("GET /personInfo called");
		JsonNode jsonNode = safetynetalertsService.getPersonInfoByFirstNameAndLastName(firstName, lastName);
		logger.info("GET /personInfo response : OK");
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

	//http://localhost:8080/communityEmail?city=<city>
	@GetMapping("/communityEmail")
	@ApiOperation(value = "Cette url doit retourner les adresses mail de tous les habitants de la ville.")
	public ResponseEntity<JsonNode> getPhonesInCity(@RequestParam(required=true) String city ) {
		logger.info("GET /communityEmail called");
		JsonNode jsonNode = safetynetalertsService.getPhonesInCity(city);
		logger.info("GET /communityEmail response : OK");
		return new ResponseEntity<>(jsonNode, HttpStatus.OK);
	}

}
