package com.safetynet.alertsapp.service;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Person;

public interface IPersonService {

	Person savePerson(Person person) throws BusinessResourceException;

	Person updatePerson(Person person) throws BusinessResourceException;

	void deletePerson(String firstname, String lastname);

}