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
import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.service.IMedicalrecordService;

//@RestController contains @ResponseBody:
//@ResponseBody annotation tells a controller that the object returned is automatically
//serialized into JSON and passed back into the HttpResponse object.
@RestController
public class MedicalrecordController {

	private static final Logger logger = LoggerFactory.getLogger(MedicalrecordController.class);
	
	@Autowired
	private IMedicalrecordService medicalrecordService;
	
	@PostMapping("/medicalRecord")
	//the @RequestBody annotation maps the HttpRequest body to a transfer or domain object,
	//enabling automatic deserialization of the inbound HttpRequest body onto a Java object.
	public ResponseEntity<Medicalrecord> postMedicalrecord(@RequestBody Medicalrecord medicalrecord) {
		logger.info("POST /medicalRecord called");
		Medicalrecord medicalrecordSaved = medicalrecordService.saveMedicalrecord(medicalrecord);	
		logger.info("POST /medicalRecord response : CREATED");
 		return new ResponseEntity<>(medicalrecordSaved, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/medicalRecord")
	public ResponseEntity<Medicalrecord> putMedicalrecord(@RequestBody Medicalrecord medicalrecord) {
		logger.info("PUT /medicalRecord called");
		Medicalrecord medicalrecordUpdated = medicalrecordService.updateMedicalrecord(medicalrecord);
		logger.info("PUT /medicalRecord response : OK");
		return new ResponseEntity<>(medicalrecordUpdated, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/medicalRecord")
	public ResponseEntity<Void> deleteUser(@RequestParam(required = true) String firstname, @RequestParam(required = true) String lastname) 
			throws BusinessResourceException {
		logger.info("DELETE /medicalRecord called");
		medicalrecordService.deleteMedicalrecord(firstname,lastname);
		logger.info("DELETE /medicalRecord response : GONE");
		return new ResponseEntity<>(HttpStatus.GONE);
 	}
	
	
}
