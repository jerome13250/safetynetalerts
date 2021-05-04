package com.safetynet.alertsapp.repository;

import java.util.List;

import com.safetynet.alertsapp.model.Firestation;

public interface IFirestationRepository {

	List<Firestation> getAll();

	List<Firestation> getByStationnumber(Integer stationNumber);

	/**
	 * Search and return the firestation number for a specific address
	 * @param address the adress required
	 * @return firestation number for a specific address, <b>null if address is not found.</b>
	 * @throws IllegalStateException if an address has more than 1 firestation number.
	 */
	Integer getByAddress(String address) throws IllegalStateException;

	void add(Firestation firestation);

	void update(Firestation firestation);

	void deleteByAddress(String address);

	void deleteByStation(Integer station);

}