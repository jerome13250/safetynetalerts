package com.safetynet.alertsapp.model;

import java.util.List;

import lombok.Data;

@Data
public class Medicalrecord {
	
	private String firstName;
	private String lastName;
	private String birthdate;
	private List<String> medications;
	private List<String> allergies;

}
