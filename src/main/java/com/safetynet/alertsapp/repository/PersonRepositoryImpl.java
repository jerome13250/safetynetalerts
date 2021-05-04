package com.safetynet.alertsapp.repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.jsonfilemapper.IJsonFileMapper;
import com.safetynet.alertsapp.model.Person;

@Repository
public class PersonRepositoryImpl implements IPersonRepository {

	private final Logger logger = LoggerFactory.getLogger(PersonRepositoryImpl.class);
	private List<Person> personList;

	@Autowired
	private IJsonFileMapper jsonFileMapper;

	//Cannot call loadJsonDataFromFile in constructor, must use @PostConstruct to access to jsonFileMapper :
	//when the constructor is called, the bean is not yet initialized - i.e. no dependencies are injected.
	//In the @PostConstruct method the bean is fully initialized so we can use the dependency jsonFileMapper.
	@PostConstruct
	protected void loadJsonDataFromFile() {
		logger.debug("Calling loadJsonDataFromFile");
		personList = jsonFileMapper.deserialize(
				"persons",
				Person.class);
	}

	protected void setPersonList(List<Person> personList) {
		this.personList = personList;
	}

	@Override
	public List<Person> getAll(){
		return personList;
	}

	@Override
	public List<Person> getByAddress(String address) {
		return personList.stream().filter(person->person.getAddress().equals(address)).collect(Collectors.toList());
	}

	@Override
	public List<Person> getByCity(String city) {
		return personList.stream().filter(person->person.getCity().equals(city)).collect(Collectors.toList());
	}

	/**
	 * Find person for a specific firstname/lastname combination.
	 * @param firstname of the person required
	 * @param lastname of the person required
	 * @return the Person object that has firstname and lastname, <b>null if not found</b>.
	 * 
	 * @throws IllegalStateException if the data contains more than 1 entry with first+lastname 
	 * since they are used as primary keys
	 */
	@Override
	public Person getByFirstnameLastname(String firstname, String lastname) throws IllegalStateException {

		List<Person> result = personList.stream().filter(person->
		(person.getFirstName().equals(firstname) &&
				person.getLastName().equals(lastname))).collect(Collectors.toList());

		if (result.size()==1) {
			return result.get(0);
		}
		else if (result.isEmpty()) { //not found is not an error
			return null;
		}
		else {//this is to test case doubles : error
			logger.debug("Found {} persons for {} {} , but was expecting 1.",result.size(), firstname, lastname );
			throw new IllegalStateException("Found "+result.size()+" persons for " +
					" " + firstname+" "+ lastname + ", but was expecting 1 Person.");
		}	
	}

	@Override
	public List<Person> getByLastname(String lastname) {
		return personList.stream().filter(person->
		person.getLastName().equals(lastname)).collect(Collectors.toList());
	}

	private void serialize() {
		jsonFileMapper.serialize("persons", Person.class, personList);
	}
	
	@Override
	public void add(Person person) throws BusinessResourceException {
		if(!person.allAttributesAreSet()) {//donnees incompletes
			throw new BusinessResourceException("SavePersonError", "Person informations are incomplete: "+ person.toString(), HttpStatus.EXPECTATION_FAILED);
		}
		
		Person personFromDB = getByFirstnameLastname(person.getFirstName(), person.getLastName());
		if(personFromDB != null) {
			logger.debug("Person already exist: {} {}",person.getFirstName(),person.getLastName());
			throw new BusinessResourceException("SavePersonError", "Person already exists: "+person.getFirstName()+" "+person.getLastName(), HttpStatus.CONFLICT);
		} 
		
		personList.add(person);
		serialize();
	}

	@Override
	public void update(Person person) {
		boolean successUpdate = false;
		
		for (Person p : personList) {
			if (p.getFirstName().equals(person.getFirstName()) && //firstname+lastname considered as primary key
					p.getLastName().equals(person.getLastName())) {
				p.setAddress(person.getAddress());
				p.setCity(person.getCity());
				p.setEmail(person.getEmail());
				p.setPhone(person.getPhone());
				p.setZip(person.getZip());
				successUpdate = true;
				break;
			}
		}
		
		if(!successUpdate) {
			logger.debug("Person does not exist: {} {}",person.getFirstName(),person.getLastName());
			throw new BusinessResourceException("UpdatePersonError", "Person does not exist: "+person.getFirstName()+" "+person.getLastName(), HttpStatus.NOT_FOUND);
		} 
		
		serialize();
	}


	@Override
	public void delete(String firstName, String lastName) {
		boolean successDelete = personList.removeIf(person-> 
		( 
				person.getFirstName().equals(firstName) &&
				person.getLastName().equals(lastName)
				));
		if(!successDelete) {
			logger.debug("Person does not exist: {} {}",firstName,lastName);
			throw new BusinessResourceException("DeletePersonError", "Person does not exist: "+firstName+" "+lastName, HttpStatus.NOT_FOUND);
		}
	
		serialize();
	}


}
