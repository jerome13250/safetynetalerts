package com.safetynet.alertsapp.repository;

import java.util.List;

import com.safetynet.alertsapp.model.Medicalrecord;

public interface IMedicalrecordRepository {

	List<Medicalrecord> getAll();

	Medicalrecord getByFirstnameAndLastName(String firstname, String lastname);

	boolean add(Medicalrecord medicalrecord);

	boolean update(Medicalrecord medicalrecord);

	boolean delete(String firstName, String lastName);

}