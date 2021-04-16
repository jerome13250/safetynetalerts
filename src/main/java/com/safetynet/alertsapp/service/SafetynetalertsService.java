package com.safetynet.alertsapp.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.model.Person;
import com.safetynet.alertsapp.repository.FirestationRepository;
import com.safetynet.alertsapp.repository.MedicalrecordRepository;
import com.safetynet.alertsapp.repository.PersonRepository;

@Service
public class SafetynetalertsService {

	private final Logger logger = LoggerFactory.getLogger(SafetynetalertsService.class);

	@Autowired
	FirestationRepository firestationRepository;

	@Autowired
	MedicalrecordRepository medicalrecordRepository;

	@Autowired
	PersonRepository personRepository;

	private int calculateAge(LocalDate birthdate) {
		Period p = Period.between(birthdate, LocalDate.now());
		return p.getYears();
	}

	/**
	 * returns a Map object containing following keys: "persons", "numberOfadults", "numberOfchildren"
	 * 
	 * @param stationNumber
	 * @return containing key="persons" / value=List of persons 
	 * + key="numberOfadults" / value=numberOfAdults"
	 * + key="numberOfchildren" / value=numberOfchildren"
	 */
	public Map<String,Object> getPersonsByStationnumberMap(int stationNumber) {

		//This will contain all the data :
		Map<String,Object> reportPersons = new HashMap<>();

		//Get all addresses with stationNumber:
		List<String> adressesByStationnumber = firestationRepository.getByStationnumber(stationNumber).stream().map(f->f.getAddress()).collect(Collectors.toList());
		logger.debug("adressesByStationnumber: {}",adressesByStationnumber);
		List<Map<String,Object>> personsForStationnumber = new ArrayList<>();
		for (Person p: personRepository.getAll() ) {
			logger.debug("Person p: {}", p);
			if (adressesByStationnumber.contains(p.getAddress())) {
				//Create Hashmap that contains only needed informations: Prénom, nom, adresse, numéro de téléphone.
				Map<String,Object> persondataMap = new HashMap<>();
				persondataMap.put("firstName", p.getFirstName());
				persondataMap.put("lastName", p.getLastName());
				persondataMap.put("address", p.getAddress());
				persondataMap.put("phone", p.getPhone());
				personsForStationnumber.add(persondataMap);
			}
		}
		reportPersons.put("persons", personsForStationnumber);

		//Get numberOfadults and numberOfChildren:
		int numberOfadults = 0;
		int numberOfChildren = 0;
		for (Map<String,Object> persondataMap : personsForStationnumber) {
			for (Medicalrecord med : medicalrecordRepository.getAll()) {
				if( persondataMap.get("firstName").equals(med.getFirstName()) && 
						persondataMap.get("lastName").equals(med.getLastName()) ) {

					if (calculateAge(med.getBirthdate()) >=18 ) {
						numberOfadults++;
					}
					else {
						numberOfChildren++;
					}
					break; //jumping out of loop
				}
			}
		}
		reportPersons.put("numberOfadults", numberOfadults);
		reportPersons.put("numberOfChildren", numberOfChildren);

		return reportPersons;

	}

	/**
	 * returns a String object containing following informations on a specific firestation:
	 * <ul>
	 * <li>List of persons : firstname, lastname, address, phone</li
	 * <li>numberOfadults</li>
	 * <li>numberOfchildren</li>
	 * 
	 * @param stationNumber
	 * @return containing key="persons" / value=List of persons 
	 * + key="numberOfadults" / value=numberOfAdults"
	 * + key="numberOfchildren" / value=numberOfchildren"
	 */
	public String getPersonsByStationnumberString(int stationNumber) {

		//This will contain all the data :
		StringBuilder reportPersons = new StringBuilder();

		logger.debug("firestationRepository.getByStationnumber(stationNumber): {}",firestationRepository.getByStationnumber(stationNumber) );
		List<String> adressesByStationnumber = firestationRepository.getByStationnumber(stationNumber).stream().map(f->f.getAddress()).collect(Collectors.toList());
		logger.debug("adressesByStationnumber: {}",adressesByStationnumber );
		List<Person> personsForStationnumber = new ArrayList<>();
		for (Person p: personRepository.getAll() ) {
			if (adressesByStationnumber.contains(p.getAddress())) {
				//Prénom, nom, adresse, numéro de téléphone.
				reportPersons.append("firstName: " + p.getFirstName() + " / ");
				reportPersons.append("lastName: " + p.getLastName() + " / ");
				reportPersons.append("address: " + p.getAddress() + " / ");
				reportPersons.append("phone: " + p.getPhone() + "<br>");

				//Save the person list:
				personsForStationnumber.add(p);
			}
		}

		//Get numberOfadults and numberOfChildren:
		int numberOfadults = 0;
		int numberOfChildren = 0;
		for (Person p : personsForStationnumber) {
			for (Medicalrecord med : medicalrecordRepository.getAll()) {
				if( p.getFirstName().equals(med.getFirstName()) && 
						p.getLastName().equals(med.getLastName()) ) {

					if (calculateAge(med.getBirthdate()) >=18 ) {
						numberOfadults++;
					}
					else {
						numberOfChildren++;
					}
					break; //jumping out of loop
				}
			}
		}
		reportPersons.append("numberOfAdults: " + numberOfadults  + "<br>");
		reportPersons.append("numberOfChildren: " + numberOfChildren);

		return reportPersons.toString();

	}

	/**
	 * returns the children list (firstname, name, age) living at a specified address.
	 * For each child, provides a list of other family members. 
	 * If no child lives at this address, return an empty String.
	 * @param address
	 * @return containing all the informations
	 */
	public String getChildrenByAddressAndListOtherFamilyMembers(String address) {
		//This will contain all the data :
		StringBuilder reportChildAndOtherFamilyMembers = new StringBuilder();
		
		//Get All children at a specific address:
		List<Person> personListForAddress = personRepository.getByAddress(address);
		
		for(Person p : personListForAddress) {
			for (Medicalrecord med : medicalrecordRepository.getAll()) {
				if(p.getFirstName().equals(med.getFirstName()) && 
						p.getLastName().equals(med.getLastName()) &&
								(calculateAge(med.getBirthdate())<18)  ) {
					
					String child = p.getFirstName() + " " + p.getLastName() + " age=" + calculateAge(med.getBirthdate());
					logger.debug("child: {}",child );
					reportChildAndOtherFamilyMembers.append(child);
					
					String otherMembersFamily = 
					personListForAddress.stream().filter(person->(
							!person.getFirstName().equals(p.getFirstName()) ||
									!person.getLastName().equals(p.getLastName())))
					.map(person->person.getFirstName() + " " + person.getLastName())
					.collect(Collectors.joining (","));
					logger.debug("otherMembersFamily: {}",otherMembersFamily );
					
					reportChildAndOtherFamilyMembers.append(", familyMembers: " + otherMembersFamily);
					reportChildAndOtherFamilyMembers.append("<br>"); //next line for browser display
				
					break; //jumping out of loop
				}
			}
		}
		return reportChildAndOtherFamilyMembers.toString();
	}
	
	/**
	 * Finds the phone number of people under a specific firestation number 
	 * @param stationNumber
	 * @return the list of phone numbers
	 */

	public String getPhoneNumbersForStationNumber(int stationNumber) {
		
		//This will contain the phone numbers, HashSet to avoid doubles :
		Set<String> phoneNumbersSet = new HashSet<>();
		
		//Get all addresses with stationNumber:
		List<String> adressesByStationnumber = firestationRepository.getByStationnumber(stationNumber).stream().map(f->f.getAddress()).collect(Collectors.toList());
		logger.debug("adressesByStationnumber: {}",adressesByStationnumber);
		
		for (Person p: personRepository.getAll() ) {
			logger.trace("Person p: {}", p);
			if (adressesByStationnumber.contains(p.getAddress())) {
				phoneNumbersSet.add(p.getPhone());
			}
		}
		return String.join("<br>", phoneNumbersSet);
	}
	
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

	public String getPersonsFirestationAndMedicalRecordByAddress(String address) {
	
		StringBuilder result = new StringBuilder();
		
		List<Person> personList = personRepository.getByAddress(address);
		int firestationNumber = firestationRepository.getByAddress(address);
		//TODO : if -1 throw BusinessException
		List<Medicalrecord> medicalrecordList = medicalrecordRepository.getAll();
		
		for(Person p: personList) {
			for(Medicalrecord med : medicalrecordList) {
				if(p.getFirstName().equals(med.getFirstName()) &&
						p.getLastName().equals(med.getLastName())) {
					//"Jack Doe phone=1-1111 age=35 firestation=1 medications=fakeMedic1,fakeMedic2, allergies=fakeAllergy1,fakeAllergy2"
					result.append(p.getFirstName() + " " + p.getLastName() + " phone=" + p.getPhone());
					result.append(" age=" + calculateAge(med.getBirthdate()));
					result.append(" firestation=" + firestationNumber);
					result.append(" medications=" + med.getMedications().toString());
					result.append(" allergies=" + med.getAllergies().toString());
					result.append("<br>");
					break; //jumping out of loop
				}
			}
		}
		
		
		return result.toString();
	}






}
