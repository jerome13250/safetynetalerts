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
			medicalrecordRepository.add(medicalrecord);
			return medicalrecordRepository.getByFirstnameAndLastName(medicalrecord.getFirstName(), medicalrecord.getLastName());
		}
		catch (BusinessResourceException e) {
			throw e;
		}
		catch(Exception ex){
			logger.debug("Technical error creating medicalrecord  {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());
			throw new BusinessResourceException("SaveMedicalrecordError", "Technical error creating medicalrecord: "+medicalrecord.getFirstName()+" "+medicalrecord.getLastName(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public Medicalrecord updateMedicalrecord(Medicalrecord medicalrecord) throws BusinessResourceException{
		try{
			medicalrecordRepository.update(medicalrecord);
			return medicalrecordRepository.getByFirstnameAndLastName(medicalrecord.getFirstName(), medicalrecord.getLastName());
		}
		catch (BusinessResourceException e) {
			throw e;
		}
		catch(Exception ex){
			logger.debug("Technical error updating medicalrecord {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());
			throw new BusinessResourceException("UpdateMedicalrecordError", "Technical error updating medicalrecord: "+medicalrecord.getFirstName()+" "+medicalrecord.getLastName(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void deleteMedicalrecord(String firstname, String lastname) throws BusinessResourceException{
		try{
			medicalrecordRepository.delete(firstname, lastname);
		}catch (BusinessResourceException e) {
				throw e;
		}catch(Exception ex){
			logger.debug("Technical error deleting medicalrecord {} {}", firstname, lastname);
			throw new BusinessResourceException("DeleteMedicalrecordError", "Technical error deleting medicalrecord: "+firstname+" "+lastname, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

}
