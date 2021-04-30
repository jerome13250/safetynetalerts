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

	/**
	 * Search and return the firestation number for a specific address
	 * @param address the adress required
	 * @return firestation number for a specific address, <b>null if address is not found.</b>
	 * @throws IllegalStateException if an address has more than 1 firestation number.
	 */
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

	private boolean serialize() {
		return jsonFileMapper.serialize("firestations", Firestation.class, firestationList);
	}

	@Override
	public boolean add(Firestation firestation) {
		if(null == firestation.getAddress() || null == firestation.getStation() ) {//donnees incompletes
			throw new BusinessResourceException("IncompleteFirestation", "Firestation informations are incomplete: "+ firestation.toString(), HttpStatus.EXPECTATION_FAILED);
		}

		boolean ramData = firestationList.add(firestation); //RAM
		boolean persistanceData = serialize(); //FILE

		return (ramData && persistanceData);
	}

	@Override
	public boolean update(Firestation firestation) {
		if(null == firestation.getAddress() || null == firestation.getStation() ) {//donnees incompletes
			throw new BusinessResourceException("IncompleteFirestation", "Firestation informations are incomplete: "+ firestation.toString(), HttpStatus.EXPECTATION_FAILED);
		}

		for (Firestation f : firestationList) {
			if (f.getAddress().equals(firestation.getAddress())) {
				f.setStation(firestation.getStation());
				boolean persistanceData = serialize();
				return persistanceData;
			}
		}
		return false; //update failed, address not found
	}

	@Override
	public boolean deleteByAddress(String address) {
		boolean ramData =  firestationList.removeIf(firestation-> firestation.getAddress().equals(address));
		boolean persistanceData = serialize();
		return (ramData && persistanceData);
	}

	@Override
	public boolean deleteByStation(Integer station) {
		boolean ramData =  firestationList.removeIf(firestation-> firestation.getStation().equals(station));
		boolean persistanceData = serialize();
		return (ramData && persistanceData);
	}
}


