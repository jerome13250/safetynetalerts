package com.safetynet.alertsapp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.jsonfilemapper.IJsonFileMapper;
import com.safetynet.alertsapp.model.Firestation;

/**
 * @author jerome
 *
 */
@Repository
public class FirestationRepositoryImpl implements IFirestationRepository {

	private final Logger logger = LoggerFactory.getLogger(FirestationRepositoryImpl.class);
	private List <Firestation> firestationList;

	@Autowired
	private IJsonFileMapper jsonFileMapper;

	//Cannot use constructor, must use @PostConstruct to access to jsonFileMapper :
	//when the constructor is called, the bean is not yet initialized - i.e. no dependencies are injected.
	//In the @PostConstruct method the bean is fully initialized so we can use the dependency jsonFileMapper.
	@PostConstruct
	protected void loadJsonDataFromFile() {
		logger.debug("Calling @PostConstruct loadJsonDataFromFile()");
		firestationList = jsonFileMapper.deserialize(
				"firestations",
				Firestation.class);
	}

	protected void setFirestationList(List<Firestation> firestationList) {
		this.firestationList = firestationList;
	}

	@Override
	public List<Firestation> getAll(){
		return firestationList;
	}

	@Override
	public List<Firestation> getByStationnumber(Integer stationNumber) {
		List<Firestation> firestationByStationnumber = new ArrayList<>();
		for (Firestation i: firestationList ) {
			if (i.getStation().equals(stationNumber)) {
				firestationByStationnumber.add(i);
			}
		}
		return firestationByStationnumber;
	}

	@Override
	public Integer getByAddress(String address) throws IllegalStateException {
		List<Firestation> result = firestationList.stream().filter(f->f.getAddress().equals(address)).collect(Collectors.toList());

		if (result.size()==1) {
			return result.get(0).getStation();
		}
		else if (result.isEmpty()) { //not found is not an error
			return null;
		}
		else {//this is to test case doubles : error
			logger.debug("Found {} firestations for address={} , but was expecting 1.",result.size(), address );
			throw new IllegalStateException("Found "+result.size()+" persons for " +
					" " + address + ", but was expecting 1 Firestation.");
		}	
	}

	private void serialize() {
		jsonFileMapper.serialize("firestations", Firestation.class, firestationList);
	}

	@Override
	public void add(Firestation firestation) {

		if(!firestation.allAttributesAreSet()) {//donnees incompletes
			throw new BusinessResourceException("AddFirestationError", "Unable to add firestation, informations are incomplete: "+ firestation.toString(), HttpStatus.EXPECTATION_FAILED);
		}
		
		Integer firestationNumber = getByAddress(firestation.getAddress());
		if(firestationNumber != null) {
			throw new BusinessResourceException("SaveFirestationError", "Firestation already exists: address="+firestation.getAddress()+" station="+firestation.getStation(), HttpStatus.CONFLICT);
		} 

		firestationList.add(firestation);
		serialize();
	}

	@Override
	public void update(Firestation firestation) {
		boolean resultUpdate = false;
		
		if(!firestation.allAttributesAreSet()) {
			throw new BusinessResourceException("UpdateFirestationError", "Firestation informations are incomplete: "+ firestation.toString(), HttpStatus.EXPECTATION_FAILED);
		}
		
		for (Firestation f : firestationList) {
			if (f.getAddress().equals(firestation.getAddress())) {
				f.setStation(firestation.getStation());
				resultUpdate = true;
				break;
			}
		}
		
		if(!resultUpdate) {
			throw new BusinessResourceException("UpdateFirestationError", "Firestation does not exist: address="+firestation.getAddress(), HttpStatus.NOT_FOUND);
		} 
		serialize();
	}

	@Override
	public void deleteByAddress(String address) {
		boolean resultUpdate = false;
		resultUpdate = firestationList.removeIf(firestation-> firestation.getAddress().equals(address));
		
		if(!resultUpdate) {
			throw new BusinessResourceException("DeleteFirestationError", "Firestation does not exist: address="+address, HttpStatus.NOT_FOUND);
		}
		serialize();
	}

	@Override
	public void deleteByStation(Integer station) {
		boolean resultUpdate = false;
		resultUpdate = firestationList.removeIf(firestation-> firestation.getStation().equals(station));
		
		if(!resultUpdate) {
			throw new BusinessResourceException("DeleteFirestationError", "Firestation does not exist: station="+station, HttpStatus.NOT_FOUND);
		}
		serialize();
	}
}


