package com.safetynet.alertsapp.jsonfilemapper;

import java.io.File;
import java.util.List;

public interface IJsonFileMapper {

	/**
	 * Method to deserialize from data source with safetynet alerts format to Java Objects
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
	<T> List<T> deserialize(File jsonSource, String objectNodeNameString, Class<T> classType);

	/**
	 * Method to serialize safetynet alerts objects to data destination.
	 * 
	 * @param <T> the java Object type that is linked to the objectNodeName (Person, Firestation,...)
	 * @param jsonSource the path to the JSON file.
	 * @param objectNodeNameString the first level of json file containing arrays of Objects.
	 * @param classType the class type of T
	 * Example in our file : "persons" , "firestations", ...
	 * @param listToSave List of java objects to save.
	 * 
	 * @return true if operation successes, false otherwise.
	 *   
	 */
	<T> boolean serialize(File jsonSource, String objectNodeNameString, Class<T> classType, List<T> listToSave);
	
}