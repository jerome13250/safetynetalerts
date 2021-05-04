package com.safetynet.alertsapp.repository;

import java.util.List;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Person;

public interface IPersonRepository {

	List<Person> getAll();

	List<Person> getByAddress(String address);

	List<Person> getByCity(String city);

	/**
	 * Find person for a specific firstname/lastname combination.
	 * @param firstname of the person required
	 * @param lastname of the person required
	 * @return the Person object that has firstname and lastname, <b>null if not found</b>.
	 * 
	 * @throws IllegalStateException if the data contains more than 1 entry with first+lastname 
	 * since they are used as primary keys
	 */
	Person getByFirstnameLastname(String firstname, String lastname) throws IllegalStateException;

	List<Person> getByLastname(String lastname);

	void add(Person person) throws BusinessResourceException;

	void update(Person person);

	void delete(String firstName, String lastName);

}