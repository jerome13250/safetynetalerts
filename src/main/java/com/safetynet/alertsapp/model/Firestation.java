package com.safetynet.alertsapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Firestation {
	private String address;
	private Integer station;
	
	public boolean allAttributesAreSet() {
		if(null == address || null == station ) {
			return false;
		}
		return true;
	}
}
