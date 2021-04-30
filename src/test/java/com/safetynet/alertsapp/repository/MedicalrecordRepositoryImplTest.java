package com.safetynet.alertsapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alertsapp.config.CustomProperties;
import com.safetynet.alertsapp.exception.BusinessResourceException;
import com.safetynet.alertsapp.jsonfilemapper.JsonFileMapperImpl;
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.model.Medicalrecord;

@ExtendWith(MockitoExtension.class)
class MedicalrecordRepositoryImplTest {

	static LocalDate date1984March6th = LocalDate.of(1984, 3, 6);
	static LocalDate date1990December15th = LocalDate.of(1990, 12, 15);
	static LocalDate date1928February28th = LocalDate.of(1928, 2, 28);

	@InjectMocks
	MedicalrecordRepositoryImpl medicalrecordRepositoryCUT;

	@Mock
	private JsonFileMapperImpl jsonFileMapperMock;

	@BeforeEach
	void initializeList() {
		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Medicalrecord> dataInitialList = new ArrayList<> (Arrays.asList(
				new Medicalrecord(
						"John",
						"Doe",
						date1984March6th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1"))
						),
				new Medicalrecord(
						"Mike",
						"Hill",
						date1990December15th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2", "fakeMedic3")),
						new ArrayList<>()
						),
				new Medicalrecord(
						"Jack",
						"Steel",
						date1928February28th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1","fakeAllergy2"))
						)
				));

		medicalrecordRepositoryCUT.setMedicalrecordList(dataInitialList);
	}

	@Test
	@DisplayName("2 objects Medicalrecord: LoadJsonDataFromFile")
	void testLoadJsonDataFromFile_2Medicalrecords()  throws Exception {
		//Arrange
		LocalDate date1999April6th = LocalDate.of(1999, 4, 16);
		LocalDate date1920December30th = LocalDate.of(1920, 12, 30);

		List<Medicalrecord> mockedList = new ArrayList<> (Arrays.asList( 				
				new Medicalrecord(
						"Jeremy",
						"Iron",
						date1999April6th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1"))
						),
				new Medicalrecord(
						"John",
						"Doe",
						date1920December30th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2", "fakeMedic3")),
						new ArrayList<>()
						)
				));
		when(jsonFileMapperMock.deserialize(
				"medicalrecords",
				Medicalrecord.class))
		.thenReturn(mockedList);

		List<Medicalrecord> expectedList = Arrays.asList(
				new Medicalrecord(
						"Jeremy",
						"Iron",
						date1999April6th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1"))
						),
				new Medicalrecord(
						"John",
						"Doe",
						date1920December30th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2", "fakeMedic3")),
						new ArrayList<>()
						));

		//Act
		medicalrecordRepositoryCUT.loadJsonDataFromFile();
		List<Medicalrecord> objectList = medicalrecordRepositoryCUT.getAll();

		//Assert
		assertEquals(2,objectList.size(),"Expected list size is 2");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList");
	}

	@Test
	@DisplayName("3 objects Medicalrecord + add one more")
	void testAdd_3medicalrecords_addOneMore()  throws Exception {
		//Arrange
		LocalDate date2021April1 = LocalDate.of(2021, 4, 1);

		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Medicalrecord> expectedList = new ArrayList<> (Arrays.asList(
				new Medicalrecord(
						"John",
						"Doe",
						date1984March6th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1"))
						),
				new Medicalrecord(
						"Mike",
						"Hill",
						date1990December15th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2", "fakeMedic3")),
						new ArrayList<>()
						),
				new Medicalrecord(
						"Jack",
						"Steel",
						date1928February28th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1","fakeAllergy2"))
						),
				new Medicalrecord(
						"Steven",
						"Copper",
						date2021April1,
						new ArrayList<> (Arrays.asList("fakeMedic25","fakeMedic26")),
						new ArrayList<> (Arrays.asList("fakeAllergy100","fakeAllergy101"))
						)
				));
		
		when(jsonFileMapperMock.serialize(any(String.class), any(Class.class), any(List.class))).thenReturn(true);

		//Act
		medicalrecordRepositoryCUT.add(
				new Medicalrecord(
						"Steven",
						"Copper",
						date2021April1,
						new ArrayList<> (Arrays.asList("fakeMedic25","fakeMedic26")),
						new ArrayList<> (Arrays.asList("fakeAllergy100","fakeAllergy101"))
						));
		List<Medicalrecord> objectList = medicalrecordRepositoryCUT.getAll();

		//Assert
		assertEquals(4,objectList.size(),"Expected list size is 4");
		assertEquals(expectedList,objectList,"Returned list must be initial List + added medicalrecord");
	}

	@Test
	@DisplayName("Test add incomplete Medicalrecord")
	void testAdd_incompleteMedicalrecord()  throws Exception {
		//Arrange
		List<Medicalrecord> expectedList = new ArrayList<> (Arrays.asList(
				new Medicalrecord("John","Doe",date1984March6th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1"))),
				new Medicalrecord("Mike","Hill",date1990December15th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2", "fakeMedic3")),
						new ArrayList<>()),
				new Medicalrecord("Jack","Steel",date1928February28th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1","fakeAllergy2")))));

		Medicalrecord incompleteMedicalrecord1 = new Medicalrecord();
		Medicalrecord incompleteMedicalrecord2 = new Medicalrecord(null,"Doe",date1984March6th,new ArrayList<>(),new ArrayList<>());
		Medicalrecord incompleteMedicalrecord3 = new Medicalrecord("John",null,date1984March6th,new ArrayList<>(),new ArrayList<>());
		Medicalrecord incompleteMedicalrecord4 = new Medicalrecord("John","Doe",null,new ArrayList<>(),new ArrayList<>());
		Medicalrecord incompleteMedicalrecord5 = new Medicalrecord("John","Doe",date1984March6th,null,new ArrayList<>());
		Medicalrecord incompleteMedicalrecord6 = new Medicalrecord("John","Doe",date1984March6th,new ArrayList<>(),null);
		//Act-Assert
		assertThrows(BusinessResourceException.class, ()->medicalrecordRepositoryCUT.add(incompleteMedicalrecord1));
		assertThrows(BusinessResourceException.class, ()->medicalrecordRepositoryCUT.add(incompleteMedicalrecord2));
		assertThrows(BusinessResourceException.class, ()->medicalrecordRepositoryCUT.add(incompleteMedicalrecord3));
		assertThrows(BusinessResourceException.class, ()->medicalrecordRepositoryCUT.add(incompleteMedicalrecord4));
		assertThrows(BusinessResourceException.class, ()->medicalrecordRepositoryCUT.add(incompleteMedicalrecord5));
		assertThrows(BusinessResourceException.class, ()->medicalrecordRepositoryCUT.add(incompleteMedicalrecord6));
		
		assertEquals(expectedList,medicalrecordRepositoryCUT.getAll(),"Returned list must be same as initial List");
		
	}
	
	@Test
	@DisplayName("3 objects Medicalrecord, update one")
	void testUpdate_3medicalrecords_updateOne()  throws Exception {
		//Arrange
		LocalDate date1998September11th = LocalDate.of(1998, 9, 11);

		//Arrays.asList() alone does not support any structural modification (i.e. removing or adding elements):
		List<Medicalrecord> expectedList = new ArrayList<> (Arrays.asList(
				new Medicalrecord(
						"John",
						"Doe",
						date1984March6th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1"))
						),
				new Medicalrecord(
						"Mike",
						"Hill",
						date1990December15th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2", "fakeMedic3")),
						new ArrayList<>()
						),
				new Medicalrecord(
						"Jack",
						"Steel",
						date1998September11th,
						new ArrayList<> (Arrays.asList("fakeMedic128")),
						new ArrayList<> (Arrays.asList("fakeAllergy1000","fakeAllergy1001","fakeAllergy1002"))
						)
				));
		when(jsonFileMapperMock.serialize(any(String.class), any(Class.class), any(List.class))).thenReturn(true);

		//Act
		boolean result = medicalrecordRepositoryCUT.update(
				new Medicalrecord(
						"Jack",
						"Steel",
						date1998September11th,
						new ArrayList<> (Arrays.asList("fakeMedic128")),
						new ArrayList<> (Arrays.asList("fakeAllergy1000","fakeAllergy1001","fakeAllergy1002"))
						));
		List<Medicalrecord> objectList = medicalrecordRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertTrue(result,"Expected result to be successful : true");
		assertEquals(expectedList,objectList,"Returned list must be same as expected List with 1 record modified");
	}

	@Test
	@DisplayName("Test update incomplete Medicalrecord")
	void testUpdate_incompleteMedicalrecord()  throws Exception {
		//Arrange
		List<Medicalrecord> expectedList = new ArrayList<> (Arrays.asList(
				new Medicalrecord("John","Doe",date1984March6th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1"))),
				new Medicalrecord("Mike","Hill",date1990December15th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2", "fakeMedic3")),
						new ArrayList<>()),
				new Medicalrecord("Jack","Steel",date1928February28th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1","fakeAllergy2")))));

		Medicalrecord incompleteMedicalrecord1 = new Medicalrecord();
		Medicalrecord incompleteMedicalrecord2 = new Medicalrecord(null,"Doe",date1984March6th,new ArrayList<>(),new ArrayList<>());
		Medicalrecord incompleteMedicalrecord3 = new Medicalrecord("John",null,date1984March6th,new ArrayList<>(),new ArrayList<>());
		Medicalrecord incompleteMedicalrecord4 = new Medicalrecord("John","Doe",null,new ArrayList<>(),new ArrayList<>());
		Medicalrecord incompleteMedicalrecord5 = new Medicalrecord("John","Doe",date1984March6th,null,new ArrayList<>());
		Medicalrecord incompleteMedicalrecord6 = new Medicalrecord("John","Doe",date1984March6th,new ArrayList<>(),null);
		//Act-Assert
		assertThrows(BusinessResourceException.class, ()->medicalrecordRepositoryCUT.update(incompleteMedicalrecord1));
		assertThrows(BusinessResourceException.class, ()->medicalrecordRepositoryCUT.update(incompleteMedicalrecord2));
		assertThrows(BusinessResourceException.class, ()->medicalrecordRepositoryCUT.update(incompleteMedicalrecord3));
		assertThrows(BusinessResourceException.class, ()->medicalrecordRepositoryCUT.update(incompleteMedicalrecord4));
		assertThrows(BusinessResourceException.class, ()->medicalrecordRepositoryCUT.update(incompleteMedicalrecord5));
		assertThrows(BusinessResourceException.class, ()->medicalrecordRepositoryCUT.update(incompleteMedicalrecord6));
		
		assertEquals(expectedList,medicalrecordRepositoryCUT.getAll(),"Returned list must be same as initial List");
		
	}
	
	@Test
	@DisplayName("3 objects Medicalrecord, try update inexistant one")
	void testUpdate_3medicalrecords_tryUpdateInexistantOne()  throws Exception {
		//Arrange
		LocalDate date2050January1st = LocalDate.of(2050, 1, 1);

		List<Medicalrecord> expectedList = new ArrayList<> (Arrays.asList(
				new Medicalrecord(
						"John",
						"Doe",
						date1984March6th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1"))
						),
				new Medicalrecord(
						"Mike",
						"Hill",
						date1990December15th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2", "fakeMedic3")),
						new ArrayList<>()
						),
				new Medicalrecord(
						"Jack",
						"Steel",
						date1928February28th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1","fakeAllergy2"))
						)
				));

		//Act
		boolean result = medicalrecordRepositoryCUT.update(
				new Medicalrecord(
						"Jack",
						"Plastic",
						date2050January1st,
						new ArrayList<> (Arrays.asList("")),
						new ArrayList<> (Arrays.asList("fakeAllergy9999"))
						));
		List<Medicalrecord> objectList = medicalrecordRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertFalse(result,"Expected result to be failure : false");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList");
	}

	@Test
	@DisplayName("3 objects Medicalrecord, delete one")
	void testDelete_3medicalrecords_deleteOne()  throws Exception {
		//Arrange

		List<Medicalrecord> expectedList = new ArrayList<> (Arrays.asList(
				new Medicalrecord(
						"John",
						"Doe",
						date1984March6th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1"))
						),
				new Medicalrecord(
						"Jack",
						"Steel",
						date1928February28th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1","fakeAllergy2"))
						)
				));
		when(jsonFileMapperMock.serialize(any(String.class), any(Class.class), any(List.class))).thenReturn(true);
		
		//Act
		boolean result = medicalrecordRepositoryCUT.delete("Mike","Hill");
		List<Medicalrecord> objectList = medicalrecordRepositoryCUT.getAll();

		//Assert
		assertEquals(2,objectList.size(),"Expected list size is 2");
		assertTrue(result,"Expected result to be successful : true");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList, with 1 record removed");
	}

	@Test
	@DisplayName("3 objects Medicalrecord, try delete inexistant one")
	void testDelete_3medicalrecords_tryDeleteInexistantOne()  throws Exception {
		//Arrange
		List<Medicalrecord> expectedList = new ArrayList<> (Arrays.asList(
				new Medicalrecord(
						"John",
						"Doe",
						date1984March6th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1"))
						),
				new Medicalrecord(
						"Mike",
						"Hill",
						date1990December15th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2", "fakeMedic3")),
						new ArrayList<>()
						),
				new Medicalrecord(
						"Jack",
						"Steel",
						date1928February28th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1","fakeAllergy2"))
						)
				));

		//Act
		boolean result = medicalrecordRepositoryCUT.delete("Mike","Mountain");
		List<Medicalrecord> objectList = medicalrecordRepositoryCUT.getAll();

		//Assert
		assertEquals(3,objectList.size(),"Expected list size is 3");
		assertFalse(result,"Expected result to be failed : false");
		assertEquals(expectedList,objectList,"Returned list must be same as mockedList, no record removed");
	}
	
	@Test
	@DisplayName("Test GetByFirstnameAndLastName")
	void testGetByFirstnameAndLastName() throws Exception {
		//Arrange
		Medicalrecord expected = new Medicalrecord("John","Doe",date1984March6th,
			new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
			new ArrayList<> (Arrays.asList("fakeAllergy1"))
		);

		//Act
		Medicalrecord result = medicalrecordRepositoryCUT.getByFirstnameAndLastName("John", "Doe");

		//Assert
		assertEquals(expected,result,"Returned Medical record must be same as expected");
	}
	
	@Test
	@DisplayName("Test GetByFirstnameAndLastName, no medical record found, must return null")
	void testGetByFirstnameAndLastName_IllegalStateExceptionNotFound() throws Exception {
		//Arrange
		//Act
		Medicalrecord result = medicalrecordRepositoryCUT.getByFirstnameAndLastName("John", "Unknown");
		//Assert
		assertNull(result,"no medical record found, must return null");
	}
	
	@Test
	@DisplayName("Test GetByFirstnameAndLastName, IllegalStateException: more than 1 medical record found")
	void testGetByFirstnameAndLastName_IllegalStateExceptionMreThanOne() throws Exception {
		//Arrange
		List<Medicalrecord> dataInitialList = new ArrayList<> (Arrays.asList(
				new Medicalrecord("John","Doe",	date1984March6th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1"))
						),
				new Medicalrecord("John","Doe",	date1984March6th,
						new ArrayList<>(),
						new ArrayList<>()
						),
				new Medicalrecord("Mike", "Hill", date1990December15th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2", "fakeMedic3")),
						new ArrayList<>()
						),
				new Medicalrecord("Jack", "Steel", date1928February28th,
						new ArrayList<> (Arrays.asList("fakeMedic1","fakeMedic2")),
						new ArrayList<> (Arrays.asList("fakeAllergy1","fakeAllergy2"))
						)
				));
		medicalrecordRepositoryCUT.setMedicalrecordList(dataInitialList);
		
		//Act + Assert
		assertThrows(IllegalStateException.class, () -> medicalrecordRepositoryCUT.getByFirstnameAndLastName("John", "Doe"));
	}
	
}
