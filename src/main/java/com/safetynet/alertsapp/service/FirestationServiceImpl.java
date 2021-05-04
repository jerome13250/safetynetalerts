package com.safetynet.alertsapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.repository.IFirestationRepository;

@Service
public class FirestationServiceImpl implements IFireStationService {


	@Autowired
	IFirestationRepository firestationRepository;

	@Override
	public Firestation saveFirestation(Firestation firestation) throws BusinessResourceException{
		try{
			firestationRepository.add(firestation);
			return new Firestation(firestation.getAddress(),firestationRepository.getByAddress(firestation.getAddress()));
		}
		catch (BusinessResourceException e) {
			throw e;
		}
		catch(Exception ex){
			throw new BusinessResourceException("SaveFirestationError", "Technical error creating firestation: address="+firestation.getAddress()+" station="+firestation.getStation(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public Firestation updateFirestation(Firestation firestation) throws BusinessResourceException{
		try{
			firestationRepository.update(firestation);
			return new Firestation(firestation.getAddress(),firestationRepository.getByAddress(firestation.getAddress()));
		}
		catch (BusinessResourceException e) {
			throw e;
		}
		catch(Exception ex){
			throw new BusinessResourceException("UpdateFirestationError", "Technical error updating firestation: "+firestation.getAddress()+" "+firestation.getStation(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void deleteFirestationByAddress(String address) throws BusinessResourceException{
		try{
			firestationRepository.deleteByAddress(address);
		}catch (BusinessResourceException e) {
				throw e;
		}catch(Exception ex){
			throw new BusinessResourceException("DeleteFirestationError", "Technical error deleting firestation: address="+address, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

	@Override
	public void deleteFirestationByStation(Integer station) throws BusinessResourceException{
		try{
			firestationRepository.deleteByStation(station);
		}catch (BusinessResourceException e) {
				throw e;
		}catch(Exception ex){
			throw new BusinessResourceException("DeleteFirestationError", "Technical error deleting firestation: station="+station, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

}
