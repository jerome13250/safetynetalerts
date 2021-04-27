package com.safetynet.alertsapp.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.safetynet.alertsapp.service.FirestationService;
import com.safetynet.alertsapp.service.MedicalrecordService;
import com.safetynet.alertsapp.service.PersonService;
import com.safetynet.alertsapp.service.SafetynetalertsService;

@Component
public class HealthCheck implements HealthIndicator {

	@Autowired
	SafetynetalertsService safetynetalertsService;
	
	@Override
	public Health health() {
		
		if (safetynetalertsService.getAllFirestation().size()==0 || 
				safetynetalertsService.getAllMedicalrecord().size()==0 ||
				safetynetalertsService.getAllPerson().size()==0) {
			return Health.down().withDetail("Cause", "Data has not been loaded from json file").build();
		}

		return Health.up().build();
	}

}
