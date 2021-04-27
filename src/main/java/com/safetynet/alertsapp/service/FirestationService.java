package com.safetynet.alertsapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.repository.FirestationRepository;

@Service
public class FirestationService {

	private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

	@Autowired
	FirestationRepository firestationRepository;

	public Firestation saveFirestation(Firestation firestation) throws BusinessResourceException{
		try{
			Integer firestationNumber = firestationRepository.getByAddress(firestation.getAddress());

			if(firestationNumber != null) {
				logger.error("Firestation already exist for address={}, station={}",firestation.getAddress(),firestationNumber);
				throw new BusinessResourceException("SaveFirestationError", "Firestation already exist: address="+firestation.getAddress()+" station="+firestation.getStation(), HttpStatus.CONFLICT);
			} 

			firestationRepository.add(firestation);
			return new Firestation(firestation.getAddress(),firestationRepository.getByAddress(firestation.getAddress()));
		}
		catch (BusinessResourceException e) {
			throw e;
		}
		catch(Exception ex){
			logger.error("Technical error creating firestation {} {}", firestation.getAddress(), firestation.getStation());
			throw new BusinessResourceException("SaveOrUpdateUserError", "Technical error creating firestation: address="+firestation.getAddress()+" station="+firestation.getStation(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public Firestation updateFirestation(Firestation firestation) throws BusinessResourceException{
		try{
			Integer firestationNumber = firestationRepository.getByAddress(firestation.getAddress());

			if(firestationNumber == null) {
				logger.error("Firestation does not exist: address={} ",firestation.getAddress());
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
			logger.error("Technical error updating firestation {} {}", firestation.getAddress(), firestation.getStation());
			throw new BusinessResourceException("SaveOrUpdateUserError", "Technical error creating or updating firestation: "+firestation.getAddress()+" "+firestation.getStation(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public void deleteFirestationByAddress(String address) {
		try{
			boolean successDelete = firestationRepository.deleteByAddress(address);
			if (!successDelete) {
				logger.error("Firestation not found: address={}",address);
				throw new BusinessResourceException("DeleteFirestationError", "Error deleting firestation: address="+address, HttpStatus.NOT_FOUND);
			}
		}catch (BusinessResourceException e) {
				throw e;
		}catch(Exception ex){
			logger.error("Error deleting firestation: address={}",address);
			throw new BusinessResourceException("DeleteFirestationError", "Error deleting firestation: address="+address, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

	public void deleteFirestationByStation(Integer station) {
		try{
			boolean successDelete = firestationRepository.deleteByStation(station);
			if (!successDelete) {
				logger.error("Firestation not found: station={}",station);
				throw new BusinessResourceException("DeleteFirestationError", "Error deleting firestation: station="+station, HttpStatus.NOT_FOUND);
			}
		}catch (BusinessResourceException e) {
				throw e;
		}catch(Exception ex){
			logger.error("Error deleting firestation: station={}",station);
			throw new BusinessResourceException("DeleteFirestationError", "Error deleting firestation: station="+station, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

}
