package com.safetynet.alertsapp.repository;

import java.nio.file.Paths;
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
import com.safetynet.alertsapp.model.Medicalrecord;

@Repository
public class MedicalrecordRepositoryImpl implements IMedicalrecordRepository {

	private final Logger logger = LoggerFactory.getLogger(MedicalrecordRepositoryImpl.class);
	private List<Medicalrecord> medicalrecordList;

	@Autowired
	private IJsonFileMapper jsonFileMapper;

	//Custom property to read the json file path in application.properties
	@Autowired
	private CustomProperties props;
	
	//Cannot use constructor, must use @PostConstruct to access to jsonFileMapper :
	//when the constructor is called, the bean is not yet initialized - i.e. no dependencies are injected.
	//In the @PostConstruct method the bean is fully initialized so we can use the dependency jsonFileMapper.
	@PostConstruct
	protected void loadJsonDataFromFile() {
		logger.debug("Calling loadJsonDataFromFile");
		medicalrecordList = jsonFileMapper.deserialize(
				Paths.get(props.getJsonfile()).toFile(),
				"medicalrecords",
				Medicalrecord.class);
	}

	protected void setMedicalrecordList(List<Medicalrecord> medicalrecordList) {
		this.medicalrecordList = medicalrecordList;
	}

	@Override
	public List<Medicalrecord> getAll(){
		return medicalrecordList;
	}

	@Override
	public Medicalrecord getByFirstnameAndLastName(String firstname, String lastname){

		List<Medicalrecord> result = 
				medicalrecordList.stream().filter(med -> 
				(med.getFirstName().equals(firstname) && med.getLastName().equals(lastname)))
				.collect(Collectors.toList());

		if (result.size()==1) {
			return result.get(0);
		}
		else if (result.isEmpty()) {
			return null;
		}
		else {
			logger.error("Found {} Medicalrecords for {} {}",result.size(), firstname, lastname );
			throw new IllegalStateException ("Found "+result.size()+" Medicalrecords for "+result.size()+
					" "+ firstname+" "+ lastname + ", but was expecting 1 Medicalrecord." );
		}	
	}

	@Override
	public boolean add(Medicalrecord medicalrecord) {
		if(null == medicalrecord.getFirstName() || null == medicalrecord.getLastName() || null == medicalrecord.getBirthdate() ||
				null == medicalrecord.getMedications() || null == medicalrecord.getAllergies()  ) {//donnees incompletes
			throw new BusinessResourceException("IncompleteMedicalrecord", "Medicalrecord informations are incomplete: "+ medicalrecord.toString(), HttpStatus.EXPECTATION_FAILED);
		}
		return medicalrecordList.add(medicalrecord);
	}

	@Override
	public boolean update(Medicalrecord medicalrecord) {
		if(null == medicalrecord.getFirstName() || null == medicalrecord.getLastName() || null == medicalrecord.getBirthdate() ||
				null == medicalrecord.getMedications() || null == medicalrecord.getAllergies()  ) {//donnees incompletes
			throw new BusinessResourceException("IncompleteMedicalrecord", "Medicalrecord informations are incomplete: "+ medicalrecord.toString(), HttpStatus.EXPECTATION_FAILED);
		}
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

	@Override
	public boolean delete(String firstName, String lastName) {
		return medicalrecordList.removeIf(medicalrecord-> 
		( 
				medicalrecord.getFirstName().equals(firstName) &&
				medicalrecord.getLastName().equals(lastName)
				));
	}



}
