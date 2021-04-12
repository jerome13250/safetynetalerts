package com.safetynet.alertsapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

	private String firstName;
	private String lastName;
	private String phone;
	private Integer zip;
	private String address;
	private String city;
	private String email;
	
}