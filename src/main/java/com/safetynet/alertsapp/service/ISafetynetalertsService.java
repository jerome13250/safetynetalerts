package com.safetynet.alertsapp.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.model.Person;

public interface ISafetynetalertsService {

	/**
	 * returns informations for a station number: "persons", "numberOfadults", "numberOfchildren"
	 * <p><b>NOTE</b>: If a medicalRecord is missing for a person, we use a default medicalrecord with today as birthdate, hence he
	 * is considered as child by default</p>
	 * 
	 * @param stationNumber the stationnumber required
	 * @return JSON node containing key="persons" / value=List of persons 
	 * + key="numberOfadults" / value=numberOfAdults"
	 * + key="numberOfchildren" / value=numberOfchildren"
	 */
	JsonNode getPersonsByStationnumber(int stationNumber);

	/**
	 * returns the children list (firstname, name, age) living at a specified address.
	 * For each child, provides a list of other family members. 
	 * If no child lives at this address, return an empty String.
	 * @param address the required address
	 * @return containing all the informations
	 */
	JsonNode getChildrenByAddressAndListOtherFamilyMembers(String address);

	/**
	 * Finds the phone number of people under a specific firestation number 
	 * @param stationNumber the required stationNumber
	 * @return the list of phone numbers
	 */
	JsonNode getPhoneNumbersForStationNumber(int stationNumber);

	/**
	 * Finds the list of persons living at a specific address with following informations:
	 * <ul>
	 * <li>firstname</li>
	 * <li>name</li>
	 * <li>phone</li>
	 * <li>age</li>
	 * <li>firestation</li>
	 * <li>medications</li>
	 * <li>allergies</li>
	 * </ul>
	 * 
	 * @param address of persons
	 * @return the string with all required informations
	 */
	JsonNode getPersonsFirestationAndMedicalRecordByAddress(String address);

	/**
	 * Finds persons grouped by firestation number and address with following infos:
	 * <ul>
	 * <li>firstname</li>
	 * <li>name</li>
	 * <li>phone</li>
	 * <li>age</li>
	 * <li>medications</li>
	 * <li>allergies</li>
	 * </ul>
	 *  
	 * @param firestationNumberList list of all firestations required
	 * @return the string with all required informations
	 */
	JsonNode getAddressesListOfPersonsPerStationNumberList(List<Integer> firestationNumberList);

	JsonNode getPersonInfoByFirstNameAndLastName(String firstname, String lastname);

	/**
	 * Search and return all phones for a city
	 * @param city the city required
	 * @return with all phones
	 */
	JsonNode getPhonesInCity(String city);

	List<Firestation> getAllFirestation();

	List<Medicalrecord> getAllMedicalrecord();

	List<Person> getAllPerson();

}