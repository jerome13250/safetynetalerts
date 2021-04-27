package com.safetynet.alertsapp.actuator;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration class that is needed to create the HttpTraceRepository bean for
 *  actuator httptrace from Spring 2.2
 * 
 * @author jerome
 *
 */
@Configuration
public class HttpTraceActuatorConfiguration {

 @Bean
 public HttpTraceRepository httpTraceRepository() {
     return new InMemoryHttpTraceRepository();
 }

}