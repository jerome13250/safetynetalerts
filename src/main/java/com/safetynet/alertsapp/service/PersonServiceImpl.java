package com.safetynet.alertsapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Person;
import com.safetynet.alertsapp.repository.IPersonRepository;

@Service
public class PersonServiceImpl implements IPersonService {

	private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

	@Autowired
	IPersonRepository personRepository;

	@Override
	public Person savePerson(Person person) throws BusinessResourceException{
		try{
			personRepository.add(person);
			return personRepository.getByFirstnameLastname(person.getFirstName(), person.getLastName());
		}
		catch (BusinessResourceException e) {
			throw e;
		}
		catch(Exception ex){
			logger.debug("Technical error creating person {} {}", person.getFirstName(), person.getLastName());
			throw new BusinessResourceException("SavePersonError", "Technical error creating person: "+person.getFirstName()+" "+person.getLastName(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public Person updatePerson(Person person) throws BusinessResourceException{
		try{
			personRepository.update(person);
			return personRepository.getByFirstnameLastname(person.getFirstName(), person.getLastName());
		}
		catch (BusinessResourceException e) {
			throw e;
		}
		catch(Exception ex){
			logger.debug("Technical error updating person {} {}", person.getFirstName(), person.getLastName());
			throw new BusinessResourceException("UpdatePersonError", "Technical error updating person: "+person.getFirstName()+" "+person.getLastName(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void deletePerson(String firstname, String lastname) throws BusinessResourceException{
		try {
			personRepository.delete(firstname, lastname);
	
		}catch (BusinessResourceException e) {
				throw e;
		}catch(Exception ex){
			logger.debug("Technical error deleting person {} {}", firstname, lastname);
			throw new BusinessResourceException("DeletePersonError", "Technical error deleting person: "+firstname+" "+lastname, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

}
