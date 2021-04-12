package com.safetynet.alertsapp.repository;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alertsapp.jsonfilemapper.JsonFileMapper;
import com.safetynet.alertsapp.model.Firestation;

/**
 * @author jerome
 *
 */
@Repository
public class FirestationRepository {

	private final Logger logger = LoggerFactory.getLogger(FirestationRepository.class);
	private List <Firestation> firestationList;

	@Autowired
	private JsonFileMapper jsonFileMapper;
	
	//Cannot use constructor, must use @PostConstruct to access to jsonFileMapper :
	//when the constructor is called, the bean is not yet initialized - i.e. no dependencies are injected.
	//In the @PostConstruct method the bean is fully initialized so we can use the dependency jsonFileMapper.
	@PostConstruct
	public void loadJsonDataFromFile() {
		firestationList = jsonFileMapper.map(
				Paths.get("json/data.json").toFile(),
				"firestations",
				new TypeReference<List<Firestation>>(){});
	}

	public List<Firestation> getAll(){
		return firestationList;
	}

	public void add(Firestation firestation) {
		firestationList.add(firestation);
	}

	public void update(Firestation firestation) {
		for (Firestation f : firestationList) {
			if (f.getAddress().equals(firestation.getAddress())) {
				f.setStation(firestation.getStation());
			}
		}
		firestationList.add(firestation);
	}

	public boolean delete(String address) {
		return firestationList.removeIf(firestation-> firestation.getAddress().equals(address));
	}



}
