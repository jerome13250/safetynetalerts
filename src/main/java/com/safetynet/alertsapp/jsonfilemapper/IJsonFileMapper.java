package com.safetynet.alertsapp.jsonfilemapper;

import java.io.File;
import java.util.List;

public interface IJsonFileMapper {

	/**
	 * Method to map from JSON file with safetynet alerts format to Java Objects
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

	<T> List<T> map(File jsonSource, String objectNodeNameString, Class<T> classType);

}