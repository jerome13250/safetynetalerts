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
import com.safetynet.alertsapp.model.Person;
import com.safetynet.alertsapp.service.PersonService;

//@RestController contains @ResponseBody:
//@ResponseBody annotation tells a controller that the object returned is automatically
//serialized into JSON and passed back into the HttpResponse object.
@RestController
public class PersonController {

	@Autowired
	private PersonService personService;
	
	@PostMapping("/person")
	//the @RequestBody annotation maps the HttpRequest body to a transfer or domain object,
	//enabling automatic deserialization of the inbound HttpRequest body onto a Java object.
	public ResponseEntity<Person> postPerson(@RequestBody Person person) {
		Person personSaved = personService.savePerson(person);		
 		return new ResponseEntity<>(personSaved, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/person")
	public ResponseEntity<Person> putPerson(@RequestBody Person person) {
		Person personUpdated = personService.updatePerson(person);
		return new ResponseEntity<>(personUpdated, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/person")
	public ResponseEntity<Void> deleteUser(@RequestParam(required = true) String firstname, @RequestParam(required = true) String lastname) 
			throws BusinessResourceException {
		personService.deletePerson(firstname,lastname);
		return new ResponseEntity<>(HttpStatus.GONE);
 	}
	
	
}
