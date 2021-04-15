package com.safetynet.alertsapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.model.Person;
import com.safetynet.alertsapp.repository.FirestationRepository;
import com.safetynet.alertsapp.repository.MedicalrecordRepository;
import com.safetynet.alertsapp.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class SafetynetalertsServiceTest {

	static LocalDate dateNow = LocalDate.now(); 
	static LocalDate dateFor5YearsOld;
	static LocalDate dateFor15YearsOld;
	static LocalDate dateFor19YearsOld;
	static LocalDate dateFor35YearsOld;
	static LocalDate dateFor80YearsOld;

	@InjectMocks
	SafetynetalertsService SafetynetalertsServiceCUT;

	@Mock
	FirestationRepository firestationRepositoryMock;

	@Mock
	MedicalrecordRepository medicalrecordRepositoryMock;

	@Mock
	PersonRepository personRepositoryMock;

	@BeforeAll
	static void initializeCalendarAndDate() {
		// create Dates for specific age
		dateFor5YearsOld = dateNow.minusYears(5);
		dateFor15YearsOld = dateNow.minusYears(15);
		dateFor19YearsOld = dateNow.minusYears(19);
		dateFor35YearsOld = dateNow.minusYears(35);
		dateFor80YearsOld = dateNow.minusYears(80);
	}

	@BeforeEach
	void parameterMock() {
		List<Firestation> firestationInitialList = new ArrayList<>(Arrays.asList(
				new Firestation("adress1", 1),
				new Firestation("adress2", 1), 
				new Firestation("adress3", 2)));
		List<Medicalrecord> medicalrecordInitialList = new ArrayList<>(Arrays.asList(
				new Medicalrecord("John", "Doe", dateFor5YearsOld,
						new ArrayList<>(Arrays.asList("fakeMedic1", "fakeMedic2")),
						new ArrayList<>(Arrays.asList("fakeAllergy1"))),
				new Medicalrecord("Mike", "Doe", dateFor15YearsOld,
						new ArrayList<>(Arrays.asList("fakeMedic1", "fakeMedic2", "fakeMedic3")),
						new ArrayList<>()),
				new Medicalrecord("Jack", "Doe", dateFor35YearsOld,
						new ArrayList<>(Arrays.asList("fakeMedic1", "fakeMedic2")),
						new ArrayList<>(Arrays.asList("fakeAllergy1", "fakeAllergy2"))),
				new Medicalrecord("Jason", "Young", dateFor19YearsOld, 
						new ArrayList<>(), 
						new ArrayList<>()),
				new Medicalrecord("Mike", "Old", dateFor80YearsOld,
						new ArrayList<>(Arrays.asList("fakeMedic1", "fakeMedic2", "fakeMedic3")),
						new ArrayList<>(Arrays.asList("fakeAllergy1", "fakeAllergy2")))));
		List<Person> personInitialList = new ArrayList<>(
				Arrays.asList(new Person("John", "Doe", "1-1111", 12345, "adress1", "Gotham", "johndoe@mail.com"),
						new Person("Mike", "Doe", "1-1111", 12345, "adress1", "Gotham", "mikedoe@mail.com"),
						new Person("Jack", "Doe", "1-1111", 12345, "adress1", "Gotham", "jackdoe@mail.com"),
						new Person("Jason", "Young", "2-2222", 78965, "adress2", "New-York", "jasonyoung@mail.com"),
						new Person("Mike", "Old", "3-3333", 95175, "adress3", "Los Angeles", "mikeold@mail.com"),
						new Person("Clark", "Kent", "4-4444", 99999, "adress4", "Metropolis", "superman@mail.com")));

		when(firestationRepositoryMock.getAll()).thenReturn(firestationInitialList);
		when(medicalrecordRepositoryMock.getAll()).thenReturn(medicalrecordInitialList);
		when(personRepositoryMock.getAll()).thenReturn(personInitialList);

	}

	/**
	 * Create a map from 2 Arrays, one contains keys, the other contains objects.
	 *
	 * @param keys contains the Map keys
	 * @param objects contains the Map values
	 * @return created with the 2 arrays
	 */
	private Map<String, Object> createMapFromArray(String[] keys , String[] objects) {
		Map<String, Object> m = new HashMap<>();
		for (int i = 0; i < keys.length; i++) {
			m.put(keys[i], objects[i]);
		}

		return m;
	}

	@Test
	@DisplayName("Get Persons By Stationnumber + Number of adults and children")
	void test_getPersonByStationnumberMap() {
		//Arrange
		Map<String,Object> expectedMap = new HashMap<>();

		//List of Hashmap that contains Person "class-like" (limited to some parameters)
		List<Map<String,Object>> persondataMapList = new ArrayList<>();

		//Persons au format Hashmap: Prénom, nom, adresse, numéro de téléphone.
		String[] keys = {"firstName","lastName","address","phone"};
		persondataMapList.add(createMapFromArray(keys,new String[] {"John","Doe","adress1","1-1111"}));
		persondataMapList.add(createMapFromArray(keys,new String[] {"Mike","Doe","adress1","1-1111"}));
		persondataMapList.add(createMapFromArray(keys,new String[] {"Jack","Doe","adress1","1-1111"}));
		persondataMapList.add(createMapFromArray(keys,new String[] {"Jason","Young","adress2","2-2222"}));
		expectedMap.put("persons", persondataMapList);
		
		//décompte du nombre d'adultes et du nombre d'enfants
		expectedMap.put("numberOfadults", 2);
		expectedMap.put("numberOfChildren", 2);
		
		//Act
		Map<String,Object> resultMap = SafetynetalertsServiceCUT.getPersonsByStationnumberMap(1);

		//Assert
		assertEquals(expectedMap,resultMap,"The 2 maps must have the dame data");
	}
	
	@Test
	@DisplayName("Get Persons By Stationnumber + Number of adults and children")
	void test_getPersonByStationnumberString() {
		//Arrange
		StringBuilder expected = new StringBuilder();
		expected.append("firstName: John / lastName: Doe / address: adress1 / phone: 1-1111<br>");
		expected.append("firstName: Mike / lastName: Doe / address: adress1 / phone: 1-1111<br>");
		expected.append("firstName: Jack / lastName: Doe / address: adress1 / phone: 1-1111<br>");
		expected.append("firstName: Jason / lastName: Young / address: adress2 / phone: 2-2222<br>");
		expected.append("numberOfAdults: 2<br>");
		expected.append("numberOfChildren: 2");
		
		//Act
		String result = SafetynetalertsServiceCUT.getPersonsByStationnumberString(1);

		//Assert
		assertEquals(expected.toString(),result,"The 2 String must have the dame data");
	}
	
	/**
	 * http://localhost:8080/childAlert?address=<address>
	 * Cette url doit retourner une liste d'enfants (tout individu âgé de 18 ans ou moins) habitant
	 *  à cette adresse.La liste doit comprendre le prénom et le nom de famille de chaque enfant,
	 *   son âge et une liste des autresmembres du foyer. 
	 *   S'il n'y a pas d'enfant, cette url peut renvoyer une chaîne vide.
	 */
	@Test
	@DisplayName("Get Children By Address + List of other family members")
	void test_getChildrenByAddressAndListOtherFamilyMembers() {
		//Arrange
		StringBuilder expected = new StringBuilder();
		//TODO:
		expected.append("John Doe, familyMembers: Mike Doe, Jack Doe<br>");
		expected.append("Mike Doe, familyMembers: John Doe, Jack Doe<br>");
		
		//Act
		String result = SafetynetalertsServiceCUT.getChildrenByAddressAndListOtherFamilyMembers("adress1");

		//Assert
		assertEquals(expected.toString(),result,"The 2 String must have the dame data");
	}

}
