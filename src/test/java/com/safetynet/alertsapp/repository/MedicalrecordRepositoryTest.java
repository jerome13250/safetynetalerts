package com.safetynet.alertsapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alertsapp.jsonfilemapper.JsonFileMapper;
import com.safetynet.alertsapp.model.Medicalrecord;

@ExtendWith(MockitoExtension.class)
class MedicalrecordRepositoryTest {

	static Calendar cal = Calendar.getInstance(); //get current system Date
	static Date date1984March6th;
	static Date date1990December15th;
	static Date date1928February28th;


	@InjectMocks
	MedicalrecordRepository medicalrecordRepositoryCUT;

	@Mock
	private JsonFileMapper jsonFileMapperMock;

	@BeforeAll
	static void initializeCalendarAndDate() {
		//clean calendar to set Date = 0
		cal.set(1970, Calendar.JANUARY, 0, 0, 0, 0); //Year, month, day, hour, min , seconds
		cal.set(14, 0); //MILLISECOND field id = 14
		//create Dates
		cal.set(1984, Calendar.MARCH, 6); //Year, month, day
		date1984March6th = cal.getTime();
		cal.set(1990, Calendar.DECEMBER, 15); //Year, month, day
		date1990December15th = cal.getTime();
		cal.set(1928, Calendar.FEBRUARY, 28); //Year, month, day
		date1928February28th = cal.getTime();
	}

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
		cal.set(1999, Calendar.APRIL, 6); //Year, month, day
		Date date1999April6th = cal.getTime();
		cal.set(1920, Calendar.DECEMBER, 30); //Year, month, day
		Date date1920December30th = cal.getTime();

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
		when(jsonFileMapperMock.map(
				Paths.get("json/data.json").toFile(),
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
		cal.set(2021, Calendar.APRIL, 1);
		Date date2021April1 = cal.getTime();

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
	@DisplayName("3 objects Medicalrecord, update one")
	void testUpdate_3medicalrecords_updateOne()  throws Exception {
		//Arrange
		cal.set(1998, Calendar.SEPTEMBER, 11); //Year, month, day
		Date date1998September11th = cal.getTime();

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
	@DisplayName("3 objects Medicalrecord, try update inexistant one")
	void testUpdate_3medicalrecords_tryUpdateInexistantOne()  throws Exception {
		//Arrange
		cal.set(2050, Calendar.JANUARY, 1); //Year, month, day
		Date date2050January1st = cal.getTime();

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
	
}
