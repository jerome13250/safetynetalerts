package com.safetynet.alertsapp.controller;

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
import com.safetynet.alertsapp.service.MedicalrecordService;

//@RestController contains @ResponseBody:
//@ResponseBody annotation tells a controller that the object returned is automatically
//serialized into JSON and passed back into the HttpResponse object.
@RestController
public class MedicalrecordController {

	@Autowired
	private MedicalrecordService medicalrecordService;
	
	@PostMapping("/medicalRecord")
	//the @RequestBody annotation maps the HttpRequest body to a transfer or domain object,
	//enabling automatic deserialization of the inbound HttpRequest body onto a Java object.
	public ResponseEntity<Medicalrecord> postMedicalrecord(@RequestBody Medicalrecord medicalrecord) {
		Medicalrecord medicalrecordSaved = medicalrecordService.saveMedicalrecord(medicalrecord);		
 		return new ResponseEntity<>(medicalrecordSaved, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/medicalRecord")
	public ResponseEntity<Medicalrecord> putMedicalrecord(@RequestBody Medicalrecord medicalrecord) {
		Medicalrecord medicalrecordUpdated = medicalrecordService.updateMedicalrecord(medicalrecord);
		return new ResponseEntity<>(medicalrecordUpdated, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/medicalRecord")
	public ResponseEntity<Void> deleteUser(@RequestParam(required = true) String firstname, @RequestParam(required = true) String lastname) 
			throws BusinessResourceException {
		medicalrecordService.deleteMedicalrecord(firstname,lastname);
		return new ResponseEntity<>(HttpStatus.GONE);
 	}
	
	
}
