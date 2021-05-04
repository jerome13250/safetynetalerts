package com.safetynet.alertsapp.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.safetynet.alertsapp.service.ISafetynetalertsService;

@Component
public class HealthCheck implements HealthIndicator {

	@Autowired
	ISafetynetalertsService safetynetalertsService;
	
	@Override
	public Health health() {
		if (safetynetalertsService.getAllFirestation().isEmpty() || 
				safetynetalertsService.getAllMedicalrecord().isEmpty() ||
				safetynetalertsService.getAllPerson().isEmpty()) {
			return Health.down().withDetail("Cause", "Data has not been loaded from json file").build();
		}
		return Health.up().build();
	}
}

