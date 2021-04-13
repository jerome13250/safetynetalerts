package com.safetynet.alertsapp.jsonfilemapper;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class uses Jackson to map from JSON file to Java Objects
 * <p>
 * The JSON file needs to respect the format defined in :
 * <a href="https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DA+Java+EN/P5+/data.json">safetynet alerts json data</a>
 * </p>
 * 
 * @author jerome
 *
 */
@Component
public class JsonFileMapper {

	private final Logger logger = LoggerFactory.getLogger(JsonFileMapper.class);

	@Autowired
	private ObjectMapper objectMapper; //this is for mocking purpose

	
	/**
	 * Method to map from JSON file with safetynet alerts format to Java Objects
	 * 
	 * @param <T> the java Object type that is linked to the objectNodeName (Person, Firestation,...)
	 * @param jsonURL the URL path to the JSON file.
	 * @param objectNodeNameString the first level of json file containing arrays of Objects.
	 * Example in our file : "persons" , "firestations", ...
	 *
	 * @return a List with the required Object type.
	 *   
	 */
	
	//TODO:use url instead
	public <T> List<T> map(File jsonSource, String objectNodeNameString, Class<T> classType) {

		logger.debug("JsonFileMapper loadJsonDataFromFile launched");
		
		List<T> objectList = new ArrayList<>();
		try {
			JsonNode jsonNode = objectMapper.readTree(jsonSource);

			//Get objects array under objectNodeName:
			JsonNode jsonNodeObjectName = jsonNode.get(objectNodeNameString); //returns null if objectNodeName not found
			if (jsonNodeObjectName != null) {
				String objectsJsonString = jsonNodeObjectName.toString();
				logger.debug("objectsJsonString={}",objectsJsonString);
				//need a second step ObjectMapper because first one needs mock but not this one:
				ObjectMapper objectMapperSecondStep = new ObjectMapper();
				//setting date format for deserializing:
				final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				objectMapperSecondStep.setDateFormat(df);
				
				for(JsonNode j : jsonNodeObjectName) {
					objectList.add(objectMapperSecondStep.readValue(j.toString(), classType));
				}
				
			}
			else {
				logger.debug("{} not found in json file.", objectNodeNameString);
			}
			
			//TODO: manage exceptions :
		} catch (Exception e) {
			logger.error("{} loadJsonDataFromFile has failed: {} , message: {}", objectNodeNameString, e, e.getMessage());
			e.printStackTrace();
		}

		return objectList;
	}
	
	
}
