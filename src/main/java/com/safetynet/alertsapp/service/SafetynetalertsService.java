package com.safetynet.alertsapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.repository.FirestationRepository;
import com.safetynet.alertsapp.repository.MedicalrecordRepository;
import com.safetynet.alertsapp.repository.PersonRepository;

@Service
public class SafetynetalertsService {
	
	@Autowired
	FirestationRepository firestationRepository;
	
	@Autowired
	MedicalrecordRepository medicalrecordRepository;
	
	@Autowired
	PersonRepository personRepository;
	
	public List<Firestation> getFirestationByStationnumber(Integer stationNumber) {
		List<Firestation> firestationListFilteredByStationnumber = new ArrayList<>();
		for (Firestation i: firestationRepository.getAll() ) {
			if (i.getStation().equals(stationNumber)) {
				firestationListFilteredByStationnumber.add(i);
			}
		}
		
		return firestationListFilteredByStationnumber;
	}

}
