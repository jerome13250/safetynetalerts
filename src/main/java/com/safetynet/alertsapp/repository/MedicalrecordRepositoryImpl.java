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
import com.safetynet.alertsapp.model.Medicalrecord;

@Repository
public class MedicalrecordRepositoryImpl implements IMedicalrecordRepository {

	private final Logger logger = LoggerFactory.getLogger(MedicalrecordRepositoryImpl.class);
	private List<Medicalrecord> medicalrecordList;

	@Autowired
	private IJsonFileMapper jsonFileMapper;
	
	//Cannot use constructor, must use @PostConstruct to access to jsonFileMapper :
	//when the constructor is called, the bean is not yet initialized - i.e. no dependencies are injected.
	//In the @PostConstruct method the bean is fully initialized so we can use the dependency jsonFileMapper.
	@PostConstruct
	protected void loadJsonDataFromFile() {
		logger.debug("Calling loadJsonDataFromFile");
		medicalrecordList = jsonFileMapper.deserialize(
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
			logger.debug("Found {} Medicalrecords for {} {}",result.size(), firstname, lastname );
			throw new IllegalStateException ("Found "+result.size()+" Medicalrecords for "+result.size()+
					" "+ firstname+" "+ lastname + ", but was expecting 1 Medicalrecord." );
		}	
	}

	private void serialize() {
		jsonFileMapper.serialize("medicalrecords", Medicalrecord.class, medicalrecordList);
	}
	
	@Override
	public boolean add(Medicalrecord medicalrecord) {
		boolean result = false;
		
		if(!medicalrecord.allAttributesAreSet()) {//donnees incompletes
			throw new BusinessResourceException("IncompleteMedicalrecord", "Medicalrecord informations are incomplete: "+ medicalrecord.toString(), HttpStatus.EXPECTATION_FAILED);
		}
		
		Medicalrecord med= getByFirstnameAndLastName(medicalrecord.getFirstName(), medicalrecord.getLastName());
		if(med != null) {
			throw new BusinessResourceException("UpdateMedicalrecordError", "Medicalrecord already exists: firstName="+medicalrecord.getFirstName() +
					" lastname=" + medicalrecord.getLastName(), HttpStatus.NOT_FOUND);
		}
		
		result = medicalrecordList.add(medicalrecord);
		serialize();
		return result;
	}

	@Override
	public boolean update(Medicalrecord medicalrecord) {
		boolean result = false;
		
		if(!medicalrecord.allAttributesAreSet()) {//donnees incompletes
			throw new BusinessResourceException("IncompleteMedicalrecord", "Medicalrecord informations are incomplete: "+ medicalrecord.toString(), HttpStatus.EXPECTATION_FAILED);
		}
		
		Medicalrecord med= getByFirstnameAndLastName(medicalrecord.getFirstName(), medicalrecord.getLastName());
		if(med == null) {
			throw new BusinessResourceException("UpdateMedicalrecordError", "Medicalrecord does not exist: firstName="+medicalrecord.getFirstName() +
					" lastname=" + medicalrecord.getLastName(), HttpStatus.NOT_FOUND);
		}
		
		for (Medicalrecord m : medicalrecordList) {
			if (m.getFirstName().equals(medicalrecord.getFirstName()) && //firstname+lastname considered as primary key
					m.getLastName().equals(medicalrecord.getLastName())) {
				m.setBirthdate(medicalrecord.getBirthdate());
				m.setMedications(medicalrecord.getMedications());
				m.setAllergies(medicalrecord.getAllergies());
				result = true;
				break;
			}
		}
		serialize();
		return result; 
	}

	@Override
	public boolean delete(String firstName, String lastName) {
		boolean result = false;
		
		Medicalrecord medicalrecord = getByFirstnameAndLastName(firstName, lastName);
		if(medicalrecord == null) {
			throw new BusinessResourceException("DeleteMedicalrecordError", "Medicalrecord does not exist: firstName="+firstName +
					" lastname=" + lastName, HttpStatus.NOT_FOUND);
		}
		
		result = medicalrecordList.removeIf(m-> 
		( 
				m.getFirstName().equals(firstName) &&
				m.getLastName().equals(lastName)
				));
		serialize();
		return result;
	}

}
