package com.safetynet.alertsapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.service.IFireStationService;

//@RestController contains @ResponseBody:
//@ResponseBody annotation tells a controller that the object returned is automatically
//serialized into JSON and passed back into the HttpResponse object.
@RestController
public class FirestationController {

	private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);
	
	@Autowired
	private IFireStationService firestationService;
	
	@PostMapping("/firestation")
	//the @RequestBody annotation maps the HttpRequest body to a transfer or domain object,
	//enabling automatic deserialization of the inbound HttpRequest body onto a Java object.
	public ResponseEntity<Firestation> postFirestation(@RequestBody Firestation firestation) {
		logger.info("POST /firestation called");
		Firestation firestationSaved = firestationService.saveFirestation(firestation);	
		logger.info("POST /firestation response : CREATED");
 		return new ResponseEntity<>(firestationSaved, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/firestation")
	public ResponseEntity<Firestation> putFirestation(@RequestBody Firestation firestation) {
		logger.info("PUT /firestation called");
		Firestation firestationUpdated = firestationService.updateFirestation(firestation);
		logger.info("PUT /firestation response : OK");
		return new ResponseEntity<>(firestationUpdated, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/firestation", params = "address") // params parameter to filter by HTTP parameters
	public ResponseEntity<Void> deleteUser(@RequestParam(required = true) String address) 
			throws BusinessResourceException {
		logger.info("DELETE /firestation?address called");
		firestationService.deleteFirestationByAddress(address);
		logger.info("DELETE /firestation?address response : GONE");
		return new ResponseEntity<>(HttpStatus.GONE);
 	}
	
	@DeleteMapping(value = "/firestation", params = "station") // params parameter to filter by HTTP parameters
	public ResponseEntity<Void> deleteFirestation(@RequestParam(required = true) Integer station) 
			throws BusinessResourceException {
		logger.info("DELETE /firestation?station called");
		firestationService.deleteFirestationByStation(station);
		logger.info("DELETE /firestation?station response : GONE");
		return new ResponseEntity<>(HttpStatus.GONE);
 	}
	
	
}
