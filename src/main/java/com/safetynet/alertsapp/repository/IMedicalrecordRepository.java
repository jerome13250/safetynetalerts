package com.safetynet.alertsapp.repository;

import java.util.List;

import com.safetynet.alertsapp.model.Medicalrecord;

public interface IMedicalrecordRepository {

	List<Medicalrecord> getAll();

	Medicalrecord getByFirstnameAndLastName(String firstname, String lastname);

	void add(Medicalrecord medicalrecord);

	void update(Medicalrecord medicalrecord);

	void delete(String firstName, String lastName);

}