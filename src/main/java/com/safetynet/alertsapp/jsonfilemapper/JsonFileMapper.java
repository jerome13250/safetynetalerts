package com.safetynet.alertsapp.jsonfilemapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
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
	 * @param jsonFile the File object path to the JSON file.
	 * @param objectNodeNameString the first level of json file containing arrays of Objects.
	 * Example in our file : "persons" , "firestations", ...
	 * @param typeReference TypeReference required by Jackson, note that it is REQUIRED to create this TypeReference
	 * before entering our generic method. If we create this TypeReference in the method, jackson mapping won't create
	 * our java List of Objects but instead a List of LinkedHashMap. This happens because we have a generic method, 
	 * when we use directly the ObjectMapper.readValue the problem is not present. 
	 *
	 * @return a List with the required Object type.
	 *   
	 */
	public <T> List<T> map(File jsonFile, String objectNodeNameString, TypeReference<List<T>> typeReference) {

		logger.debug("JsonFileMapper loadJsonDataFromFile launched");
		List<T> objectList = null;
		try {
			JsonNode jsonNode = objectMapper.readTree(jsonFile);

			//Get objects array under objectNodeName:
			JsonNode jsonNodeObjectName = jsonNode.get(objectNodeNameString); //returns null if objectNodeName not found
			if (jsonNodeObjectName != null) {
				String objectsJsonString = jsonNodeObjectName.toString();
				logger.debug("objectsJsonString={}",objectsJsonString);
				//need a second step ObjectMapper because first one needs mock but not this one:
				ObjectMapper objectMapperSecondStep = new ObjectMapper();
				objectList = objectMapperSecondStep.readValue(objectsJsonString, typeReference);
			}
			else {
				logger.debug("{} not found in json file.", objectNodeNameString);
			}
			
			//TODO: manage exceptions :
		} catch (Exception e) {
			logger.error("{} loadJsonDataFromFile has failed: {} , message: {}", objectNodeNameString, e.toString(), e.getMessage());
			e.printStackTrace();
		}

		if (objectList==null) {
			return new ArrayList<>();
		}
		return objectList;
	}
}
