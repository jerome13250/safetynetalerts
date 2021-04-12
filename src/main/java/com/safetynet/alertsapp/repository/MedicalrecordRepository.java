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
import com.safetynet.alertsapp.model.Medicalrecord;

@Repository
public class MedicalrecordRepository {
	
	private final Logger logger = LoggerFactory.getLogger(MedicalrecordRepository.class);
	private List<Medicalrecord> medicalrecordList;
	
	@Autowired
	private JsonFileMapper jsonFileMapper;
		
	//One time loading data from json file after Spring boot start:
	@PostConstruct
	private void loadJsonDataFromFile() {
		medicalrecordList = jsonFileMapper.map(
				Paths.get("json/data.json").toFile(),
				"medicalrecords",
				new TypeReference<List<Medicalrecord>>(){});
	}
	
	

}
