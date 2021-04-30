package com.safetynet.alertsapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.repository.IFirestationRepository;

@Service
public class FirestationServiceImpl implements IFireStationService {

	private static final Logger logger = LoggerFactory.getLogger(FirestationServiceImpl.class);

	@Autowired
	IFirestationRepository firestationRepository;

	@Override
	public Firestation saveFirestation(Firestation firestation) throws BusinessResourceException{
		try{
			Integer firestationNumber = firestationRepository.getByAddress(firestation.getAddress());

			if(firestationNumber != null) {
				throw new BusinessResourceException("SaveFirestationError", "Firestation already exist: address="+firestation.getAddress()+" station="+firestation.getStation(), HttpStatus.CONFLICT);
			} 

			firestationRepository.add(firestation);
			return new Firestation(firestation.getAddress(),firestationRepository.getByAddress(firestation.getAddress()));
		}
		catch (BusinessResourceException e) {
			throw e;
		}
		catch(Exception ex){
			throw new BusinessResourceException("SaveOrUpdateUserError", "Technical error creating firestation: address="+firestation.getAddress()+" station="+firestation.getStation(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public Firestation updateFirestation(Firestation firestation) throws BusinessResourceException{
		try{
			Integer firestationNumber = firestationRepository.getByAddress(firestation.getAddress());

			if(firestationNumber == null) {
				throw new BusinessResourceException("UpdateFirestationError", "Firestation does not exist: address="+firestation.getAddress(), HttpStatus.NOT_FOUND);
			} 
			
			firestationRepository.update(firestation);
			Firestation result = new Firestation(firestation.getAddress(),firestationRepository.getByAddress(firestation.getAddress()));
			return result;
		}
		catch (BusinessResourceException e) {
			throw e;
		}
		catch(Exception ex){
			throw new BusinessResourceException("SaveOrUpdateUserError", "Technical error creating or updating firestation: "+firestation.getAddress()+" "+firestation.getStation(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void deleteFirestationByAddress(String address) throws BusinessResourceException{
		try{
			boolean successDelete = firestationRepository.deleteByAddress(address);
			if (!successDelete) {
				throw new BusinessResourceException("DeleteFirestationError", "Firestation unknown: address="+address, HttpStatus.NOT_FOUND);
			}
		}catch (BusinessResourceException e) {
				throw e;
		}catch(Exception ex){
			throw new BusinessResourceException("DeleteFirestationError", "Technical error deleting firestation: address="+address, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

	@Override
	public void deleteFirestationByStation(Integer station) throws BusinessResourceException{
		try{
			boolean successDelete = firestationRepository.deleteByStation(station);
			if (!successDelete) {
				throw new BusinessResourceException("DeleteFirestationError", "Firestation unknown: station="+station, HttpStatus.NOT_FOUND);
			}
		}catch (BusinessResourceException e) {
				throw e;
		}catch(Exception ex){
			throw new BusinessResourceException("DeleteFirestationError", "Technical error deleting firestation: station="+station, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

}
