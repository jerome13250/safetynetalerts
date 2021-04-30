package com.safetynet.alertsapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix="com.safetynet.alertsapp")
public class CustomProperties {

		private String jsonfile;
		private Boolean persistance;
}
