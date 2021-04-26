package com.safetynet.alertsapp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix="com.safetynet.alertsapp")
public class CustomProperties {

		private String jsonfile;
}
