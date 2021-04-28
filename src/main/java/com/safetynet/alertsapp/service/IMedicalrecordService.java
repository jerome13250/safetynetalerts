package com.safetynet.alertsapp.service;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Medicalrecord;

public interface IMedicalrecordService {

	Medicalrecord saveMedicalrecord(Medicalrecord medicalrecord) throws BusinessResourceException;

	Medicalrecord updateMedicalrecord(Medicalrecord medicalrecord) throws BusinessResourceException;

	void deleteMedicalrecord(String firstname, String lastname);

}