package com.safetynet.alertsapp.repository;

import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.safetynet.alertsapp.jsonfilemapper.JsonFileMapper;
import com.safetynet.alertsapp.model.Medicalrecord;

@Repository
public class MedicalrecordRepository {

	private final Logger logger = LoggerFactory.getLogger(MedicalrecordRepository.class);
	private List<Medicalrecord> medicalrecordList;

	@Autowired
	private JsonFileMapper jsonFileMapper;

	//Cannot use constructor, must use @PostConstruct to access to jsonFileMapper :
	//when the constructor is called, the bean is not yet initialized - i.e. no dependencies are injected.
	//In the @PostConstruct method the bean is fully initialized so we can use the dependency jsonFileMapper.
	@PostConstruct
	private void loadJsonDataFromFile() {
		medicalrecordList = jsonFileMapper.map(
				Paths.get("json/data.json").toFile(),
				"medicalrecords",
				new TypeReference<List<Medicalrecord>>(){});
	}

	public List<Medicalrecord> getAll(){
		return medicalrecordList;
	}

	public void add(Medicalrecord medicalrecord) {
		medicalrecordList.add(medicalrecord);
	}

	public boolean update(Medicalrecord medicalrecord) {
		for (Medicalrecord f : medicalrecordList) {
			if (f.getFirstName().equals(medicalrecord.getFirstName()) &&
					f.getLastName().equals(medicalrecord.getLastName())) {
				f.setBirthdate(medicalrecord.getBirthdate());
				f.setMedications(medicalrecord.getMedications());
				f.setAllergies(medicalrecord.getAllergies());
				return true; //firstname+lastname considered as primary key
			}
		}
		return false; //update failed, firstname+lastname not found
	}

	public boolean delete(String firstName, String lastName) {
		return medicalrecordList.removeIf(medicalrecord-> 
			( 
				medicalrecord.getFirstName().equals(firstName) &&
				medicalrecord.getLastName().equals(lastName)
			));
	}

}
