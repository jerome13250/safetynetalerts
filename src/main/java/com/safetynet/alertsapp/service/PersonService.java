package com.safetynet.alertsapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Person;
import com.safetynet.alertsapp.repository.PersonRepository;

@Service
public class PersonService {

	private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	PersonRepository personRepository;

	public Person savePerson(Person person) throws BusinessResourceException{
		try{
			Person personFromDB = personRepository.getByFirstnameLastname(person.getFirstName(), person.getLastName());

			if(personFromDB != null) {
				logger.error("Person already exist: {} {}",person.getFirstName(),person.getLastName());
				throw new BusinessResourceException("SavePersonError", "Person already exist: "+person.getFirstName()+" "+person.getLastName(), HttpStatus.CONFLICT);
			} 

			personRepository.add(person);
			return personRepository.getByFirstnameLastname(person.getFirstName(), person.getLastName());
		}
		catch (BusinessResourceException e) {
			throw e;
		}
		catch(Exception ex){
			logger.error("Technical error creating person {} {}", person.getFirstName(), person.getLastName());
			throw new BusinessResourceException("SaveOrUpdateUserError", "Technical error creating or updating person: "+person.getFirstName()+" "+person.getLastName(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public Person updatePerson(Person person) throws BusinessResourceException{
		try{
			Person personFromDB = personRepository.getByFirstnameLastname(person.getFirstName(), person.getLastName());

			if(personFromDB == null) {
				logger.error("Person does not exist: {} {}",person.getFirstName(),person.getLastName());
				throw new BusinessResourceException("UpdatePersonError", "Person does not exist: "+person.getFirstName()+" "+person.getLastName(), HttpStatus.NOT_FOUND);
			} 
			
			personRepository.update(person);
			return personRepository.getByFirstnameLastname(person.getFirstName(), person.getLastName());
		}
		catch (BusinessResourceException e) {
			throw e;
		}
		catch(Exception ex){
			logger.error("Technical error updating person {} {}", person.getFirstName(), person.getLastName());
			throw new BusinessResourceException("SaveOrUpdateUserError", "Technical error creating or updating person: "+person.getFirstName()+" "+person.getLastName(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public void deletePerson(String firstname, String lastname) {
		try{
			boolean successDelete = personRepository.delete(firstname, lastname);
			if (!successDelete) {
				logger.error("Person not found: {} {}",firstname,lastname);
				throw new BusinessResourceException("DeletePersonError", "Error deleting person: "+firstname+" "+lastname, HttpStatus.NOT_FOUND);
			}
		}catch (BusinessResourceException e) {
				throw e;
		}catch(Exception ex){
			logger.error("Technical error deleting person {} {}", firstname, lastname);
			throw new BusinessResourceException("DeletePersonError", "Error deleting person: "+firstname+" "+lastname, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}




}
