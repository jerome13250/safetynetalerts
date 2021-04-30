package com.safetynet.alertsapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.repository.IMedicalrecordRepository;

@Service
public class MedicalrecordServiceImpl implements IMedicalrecordService {

	private static final Logger logger = LoggerFactory.getLogger(MedicalrecordServiceImpl.class);

	@Autowired
	IMedicalrecordRepository medicalrecordRepository;

	@Override
	public Medicalrecord saveMedicalrecord(Medicalrecord medicalrecord) throws BusinessResourceException{
		try{
			Medicalrecord medicalrecordFromDB = medicalrecordRepository.getByFirstnameAndLastName(medicalrecord.getFirstName(), medicalrecord.getLastName());

			if(medicalrecordFromDB != null) {
				logger.debug("Medicalrecord already exist: {} {}",medicalrecord.getFirstName(),medicalrecord.getLastName());
				throw new BusinessResourceException("SaveMedicalrecordError", "Medicalrecord already exist: "+medicalrecord.getFirstName()+" "+medicalrecord.getLastName(), HttpStatus.CONFLICT);
			} 

			medicalrecordRepository.add(medicalrecord);
			return medicalrecordRepository.getByFirstnameAndLastName(medicalrecord.getFirstName(), medicalrecord.getLastName());
		}
		catch (BusinessResourceException e) {
			throw e;
		}
		catch(Exception ex){
			logger.debug("Technical error creating medicalrecord  {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());
			throw new BusinessResourceException("SaveOrUpdateUserError", "Technical error creating or updating medicalrecord: "+medicalrecord.getFirstName()+" "+medicalrecord.getLastName(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public Medicalrecord updateMedicalrecord(Medicalrecord medicalrecord) throws BusinessResourceException{
		try{
			Medicalrecord medicalrecordFromDB = medicalrecordRepository.getByFirstnameAndLastName(medicalrecord.getFirstName(), medicalrecord.getLastName());

			if(medicalrecordFromDB == null) {
				logger.debug("Medicalrecord does not exist: {} {}",medicalrecord.getFirstName(),medicalrecord.getLastName());
				throw new BusinessResourceException("UpdateMedicalrecordError", "Medicalrecord does not exist: "+medicalrecord.getFirstName()+" "+medicalrecord.getLastName(), HttpStatus.NOT_FOUND);
			} 
			
			medicalrecordRepository.update(medicalrecord);
			return medicalrecordRepository.getByFirstnameAndLastName(medicalrecord.getFirstName(), medicalrecord.getLastName());
		}
		catch (BusinessResourceException e) {
			throw e;
		}
		catch(Exception ex){
			logger.debug("Technical error updating medicalrecord {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());
			throw new BusinessResourceException("SaveOrUpdateUserError", "Technical error creating or updating medicalrecord: "+medicalrecord.getFirstName()+" "+medicalrecord.getLastName(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void deleteMedicalrecord(String firstname, String lastname) throws BusinessResourceException{
		try{
			boolean successDelete = medicalrecordRepository.delete(firstname, lastname);
			if (!successDelete) {
				logger.debug("Medicalrecord not found: {} {}",firstname,lastname);
				throw new BusinessResourceException("DeleteMedicalrecordError", "Error deleting medicalrecord: "+firstname+" "+lastname, HttpStatus.NOT_FOUND);
			}
		}catch (BusinessResourceException e) {
				throw e;
		}catch(Exception ex){
			logger.debug("Technical error deleting medicalrecord {} {}", firstname, lastname);
			throw new BusinessResourceException("DeleteMedicalrecordError", "Error deleting medicalrecord: "+firstname+" "+lastname, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

}
