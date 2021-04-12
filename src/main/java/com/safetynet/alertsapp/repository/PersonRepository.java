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
import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.model.Person;

@Repository
public class PersonRepository {
	
	private final Logger logger = LoggerFactory.getLogger(PersonRepository.class);
	private List<Person> personList;
	
	@Autowired
	private JsonFileMapper jsonFileMapper;
		
	//One time loading data from json file after Spring boot start:
	@PostConstruct
	private void loadJsonDataFromFile() {
		personList = jsonFileMapper.map(
				Paths.get("json/data.json").toFile(),
				"persons",
				new TypeReference<List<Person>>(){});
	}
}
