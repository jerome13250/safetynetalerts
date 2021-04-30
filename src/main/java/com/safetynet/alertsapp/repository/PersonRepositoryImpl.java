package com.safetynet.alertsapp.repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.safetynet.alertsapp.config.CustomProperties;
import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.jsonfilemapper.IJsonFileMapper;
import com.safetynet.alertsapp.model.Person;

@Repository
public class PersonRepositoryImpl implements IPersonRepository {

	private final Logger logger = LoggerFactory.getLogger(PersonRepositoryImpl.class);
	private List<Person> personList;

	@Autowired
	private IJsonFileMapper jsonFileMapper;

	//Custom property to read the json file path in application.properties
	@Autowired
	private CustomProperties props;

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

	private boolean serialize() {
		return jsonFileMapper.serialize("persons", Person.class, personList);
	}
	
	@Override
	public boolean add(Person person) throws BusinessResourceException {
		if(null == person.getFirstName() || null == person.getLastName() || null == person.getAddress() ||
				null == person.getCity() || null == person.getEmail() || null == person.getPhone() ||
				null == person.getZip() ) {//donnees incompletes
			throw new BusinessResourceException("IncompletePerson", "Person informations are incomplete: "+ person.toString(), HttpStatus.EXPECTATION_FAILED);
		}
		boolean ramData = personList.add(person); //RAM
		boolean persistanceData = serialize(); //FILE

		return (ramData && persistanceData);
	}

	@Override
	public boolean update(Person person) {
		if(null == person.getFirstName() || null == person.getLastName() || null == person.getAddress() ||
				null == person.getCity() || null == person.getEmail() || null == person.getPhone() ||
				null == person.getZip() ) {//donnees incompletes
			throw new BusinessResourceException("IncompletePerson", "Person informations are incomplete: "+ person.toString(), HttpStatus.EXPECTATION_FAILED);
		}
		for (Person p : personList) {
			if (p.getFirstName().equals(person.getFirstName()) && //firstname+lastname considered as primary key
					p.getLastName().equals(person.getLastName())) {
				p.setAddress(person.getAddress());
				p.setCity(person.getCity());
				p.setEmail(person.getEmail());
				p.setPhone(person.getPhone());
				p.setZip(person.getZip());
				return serialize(); 
			}
		}
		return false; //update failed, firstname+lastname not found
	}

	/*This would be for PATCH partial update
	public boolean patch(Person person) {
		for (Person p : personList) {
			if (p.getFirstName().equals(person.getFirstName()) &&
					p.getLastName().equals(person.getLastName())) {
				if(person.getAddress()!=null) {p.setAddress(person.getAddress());}
				if(person.getCity()!=null) {p.setCity(person.getCity());}
				if(person.getEmail()!=null) {p.setEmail(person.getEmail());}
				if(person.getPhone()!=null) {p.setPhone(person.getPhone());}
				if(person.getZip()!=null) {p.setZip(person.getZip());}
				return true; //firstname+lastname considered as primary key
			}
		}
		return false; //update failed, firstname+lastname not found
	}
	 */

	@Override
	public boolean delete(String firstName, String lastName) {
		boolean ramData =  personList.removeIf(person-> 
		( 
				person.getFirstName().equals(firstName) &&
				person.getLastName().equals(lastName)
				));
		
		boolean persistanceData = serialize(); //FILE
		return (ramData && persistanceData);
		
	}


}
