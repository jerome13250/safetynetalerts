package com.safetynet.alertsapp.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.model.Person;
import com.safetynet.alertsapp.repository.FirestationRepository;
import com.safetynet.alertsapp.repository.MedicalrecordRepository;
import com.safetynet.alertsapp.repository.PersonRepository;

@Service
public class SafetynetalertsService {

	Calendar todayCal = Calendar.getInstance();
	
	@Autowired
	FirestationRepository firestationRepository;

	@Autowired
	MedicalrecordRepository medicalrecordRepository;

	@Autowired
	PersonRepository personRepository;

	public List<String> getAdressesByStationnumber(Integer stationNumber) {
		List<String> adressesByStationnumber = new ArrayList<>();
		for (Firestation i: firestationRepository.getAll() ) {
			if (i.getStation().equals(stationNumber)) {
				adressesByStationnumber.add(i.getAddress());
			}
		}

		return adressesByStationnumber;
	}

	/**
	 * Service that returns a Map object containing following keys: "persons", "numberOfadults", "numberOfchildren"
	 * 
	 * @param stationNumber
	 * @return containing key="persons" / value=List of persons 
	 * + key="numberOfadults" / value=numberOfAdults"
	 * + key="numberOfchildren" / value=numberOfchildren"
	 */
	public Map<String,Object> getPersonsByStationnumber(int stationNumber) {
		
		//This will contain all the data :
		Map<String,Object> reportPersons = new HashMap<>();
		
		//Get all the persons:
		List<String> adressesByStationnumber = getAdressesByStationnumber(stationNumber);
		List<Map<String,Object>> personsForStationnumber = new ArrayList<>();
		for (Person p: personRepository.getAll() ) {
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
				}
			}
		}
		reportPersons.put("numberOfadults", numberOfadults);
		reportPersons.put("numberOfChildren", numberOfChildren);
		
		return reportPersons;

	}
	
	//TODO : refactor all project to LocalDate
	private int calculateAge(Date birthdate) {
	
		return 0;
	}
	
	
	
	
	
	

}
