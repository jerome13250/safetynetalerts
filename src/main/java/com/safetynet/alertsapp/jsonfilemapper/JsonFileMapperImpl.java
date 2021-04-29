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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alertsapp.config.CustomProperties;

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
public class JsonFileMapperImpl implements IJsonFileMapper {

	private final Logger logger = LoggerFactory.getLogger(JsonFileMapperImpl.class);

	@Autowired
	private ObjectMapper objectMapper; //this is for mocking purpose

	//Custom property to read the json file path in application.properties
	@Autowired
	private CustomProperties customProperties;
	
	/**
	 * Method to deserialize from JSON file (safetynet alerts format) to Java Objects
	 * 
	 * @param <T> the java Object type that is linked to the objectNodeName (Person, Firestation,...)
	 * @param jsonSource the path to the JSON file.
	 * @param objectNodeNameString the first level of json file containing arrays of Objects.
	 * @param classType the class type of T
	 * Example in our file : "persons" , "firestations", ...
	 *
	 * @return a List with the required Object type.
	 *   
	 */
	@Override
	public <T> List<T> deserialize(File jsonSource, String objectNodeNameString, Class<T> classType) {

		logger.debug("JsonFileMapper deserialize launched");
		
		List<T> objectList = new ArrayList<>();
		try {
			JsonNode jsonNodeRoot = objectMapper.readTree(customProperties.getJsonfile());

			//Get objects array under jsonNodeRoot:
			JsonNode jsonNodeObjectName = jsonNodeRoot.get(objectNodeNameString); //returns null if objectNodeName not found
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
			
		} catch (Exception e) {
			logger.error("{} loadJsonDataFromFile has failed: {} , message: {}", objectNodeNameString, e, e.getMessage());
			e.printStackTrace();
		}

		return objectList;
	}
	
	public <T> boolean serialize(File jsonSource, String objectNodeNameString, Class<T> classType, List<T> listToSave) {

		logger.debug("JsonFileMapper serialize launched");

		try {
			JsonNode jsonNodeRoot = objectMapper.readTree(customProperties.getJsonfile());
			ArrayNode arrayNodeObjectName;
			
			//Get object array under jsonNodeRoot:
			JsonNode jsonNodeObjectName = jsonNodeRoot.get(objectNodeNameString); //returns null if objectNodeName not found
			if (jsonNodeObjectName != null) {
				//To update an existing node, need to cast it to ObjectNode:
				arrayNodeObjectName = (ArrayNode) jsonNodeObjectName;
				arrayNodeObjectName.removeAll();// clean all
			}
			else {
				logger.debug("{} not found in json file.", objectNodeNameString);
				arrayNodeObjectName = ((ObjectNode)jsonNodeRoot).putArray(objectNodeNameString);
			}
			//need a second step ObjectMapper because first one needs mock but not this one:
			ObjectMapper objectMapperSecondStep = new ObjectMapper();
			//setting date format for deserializing:
			final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			objectMapperSecondStep.setDateFormat(df);
			
			for(T elementToSave: listToSave) {
				arrayNodeObjectName.add(objectMapperSecondStep.valueToTree(elementToSave));
				logger.debug("elementToSave={}",elementToSave);
			}
			
			//save data to file:
			objectMapper.writeValue(jsonSource, jsonNodeRoot);
			
		} catch (Exception e) {
			logger.error("{} loadJsonDataFromFile has failed: {} , message: {}", objectNodeNameString, e, e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
