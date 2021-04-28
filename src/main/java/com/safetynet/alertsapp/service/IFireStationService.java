package com.safetynet.alertsapp.service;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Firestation;

public interface IFireStationService {

	Firestation saveFirestation(Firestation firestation) throws BusinessResourceException;

	Firestation updateFirestation(Firestation firestation) throws BusinessResourceException;

	void deleteFirestationByAddress(String address);

	void deleteFirestationByStation(Integer station);

}