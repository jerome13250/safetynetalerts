package com.safetynet.alertsapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.service.SafetynetalertsService;

@RestController
public class SafetynetalertsController {

	@Autowired
	private SafetynetalertsService safetynetalertsService;
	
	@GetMapping("/firestation")
	public List<Firestation> showWatchlistItemForm(@RequestParam(required=true) Integer stationNumber) {
		
		return safetynetalertsService.getFirestationByStationnumber(3);
		
	}
	
	
	
	
}
