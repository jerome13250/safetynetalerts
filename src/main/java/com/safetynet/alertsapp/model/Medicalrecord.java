package com.safetynet.alertsapp.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Medicalrecord {
	
	private String firstName;
	private String lastName;
	private Date birthdate;
	private List<String> medications;
	private List<String> allergies;

}
