package com.safetynet.alertsapp.repository;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safetynet.alertsapp.jsonfilemapper.JsonFileMapper;
import com.safetynet.alertsapp.model.Person;

@Repository
public class PersonRepository {

	private final Logger logger = LoggerFactory.getLogger(PersonRepository.class);
	private List<Person> personList;

	@Autowired
	private JsonFileMapper jsonFileMapper;

	//Cannot use constructor, must use @PostConstruct to access to jsonFileMapper :
	//when the constructor is called, the bean is not yet initialized - i.e. no dependencies are injected.
	//In the @PostConstruct method the bean is fully initialized so we can use the dependency jsonFileMapper.
	@PostConstruct
	protected void loadJsonDataFromFile() {
		logger.debug("Calling loadJsonDataFromFile");
		personList = jsonFileMapper.map(
				Paths.get("json/data.json").toFile(),
				"persons",
				Person.class);
	}

	protected void setPersonList(List<Person> personList) {
		this.personList = personList;
	}

	public List<Person> getAll(){
		return personList;
	}

	public List<Person> getByAddress(String address) {
		return personList.stream().filter(person->person.getAddress().equals(address)).collect(Collectors.toList());
	}

	public Person getByFirstnameLastname(String firstname, String lastname) {

		List<Person> result = personList.stream().filter(person->
		(person.getFirstName().equals(firstname) &&
				person.getLastName().equals(lastname))).collect(Collectors.toList());

		if (result.size()==1) {
			return result.get(0);
		}
		else {
			logger.error("Found {} persons for {} {}",result.size(), firstname, lastname );
			throw new IllegalStateException ("Found "+result.size()+" persons for " +
					" " + firstname+" "+ lastname + ", but was expecting 1 Person." );
		}	
	}

	public List<Person> getByLastname(String lastname) {

		return personList.stream().filter(person->
		person.getLastName().equals(lastname)).collect(Collectors.toList());
	}

	public void add(Person person) {
		personList.add(person);
	}

	public boolean update(Person person) {
		for (Person p : personList) {
			if (p.getFirstName().equals(person.getFirstName()) &&
					p.getLastName().equals(person.getLastName())) {
				p.setAddress(person.getAddress());
				p.setCity(person.getCity());
				p.setEmail(person.getEmail());
				p.setPhone(person.getPhone());
				p.setZip(person.getZip());
				return true; //firstname+lastname considered as primary key
			}
		}
		return false; //update failed, firstname+lastname not found
	}

	public boolean delete(String firstName, String lastName) {
		return personList.removeIf(person-> 
		( 
				person.getFirstName().equals(firstName) &&
				person.getLastName().equals(lastName)
				));
	}


}
