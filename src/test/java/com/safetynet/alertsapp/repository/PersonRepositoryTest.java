package com.safetynet.alertsapp.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alertsapp.jsonfilemapper.JsonFileMapper;

@ExtendWith(MockitoExtension.class)
public class PersonRepositoryTest {

	@InjectMocks
	PersonRepository personRepositoryCUT;

	@Mock
	private JsonFileMapper jsonFileMapperMock;
	
	
	
}
