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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.model.Person;
import com.safetynet.alertsapp.repository.FirestationRepository;
import com.safetynet.alertsapp.repository.MedicalrecordRepository;
import com.safetynet.alertsapp.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
class SafetynetalertsServiceTest {

	private final Logger logger = LoggerFactory.getLogger(SafetynetalertsServiceTest.class);

	static List<Firestation> firestationInitialList;
	static List<Firestation> firestationAdressesForStationNumber1;
	static List<Firestation> firestationAdressesForStationNumber2;
	//MedicalRecord
	static List<Medicalrecord> medicalrecordInitialList;
	static Medicalrecord medicalrecordJohnDoe;
	static Medicalrecord medicalrecordMikeDoe;
	static Medicalrecord medicalrecordJackDoe;
	static Medicalrecord medicalrecordJasonYoung;
	static Medicalrecord medicalrecordMikeOld;
	//Person
	static List<Person> personInitialList;
	static List<Person> personListForAddress1;
	static List<Person> personListForAddress2;
	static List<Person> personListForAddress3;
	static Person personJohnDoe;

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
	static void initializeData() {
		// create Dates for specific age
		dateFor5YearsOld = dateNow.minusYears(5);
		dateFor15YearsOld = dateNow.minusYears(15);
		dateFor19YearsOld = dateNow.minusYears(19);
		dateFor35YearsOld = dateNow.minusYears(35);
		dateFor80YearsOld = dateNow.minusYears(80);

		firestationInitialList = new ArrayList<>(Arrays.asList(
				new Firestation("adress1", 1),
				new Firestation("adress2", 1), 
				new Firestation("adress3", 2),
				new Firestation("adress4", 3)));
		firestationAdressesForStationNumber1 = new ArrayList<>(Arrays.asList(
				new Firestation("adress1", 1),
				new Firestation("adress2", 1)));
		firestationAdressesForStationNumber2 = new ArrayList<>(Arrays.asList(
				new Firestation("adress3", 2)));

		medicalrecordJohnDoe = new Medicalrecord("John", "Doe", dateFor5YearsOld,
				new ArrayList<>(Arrays.asList("fakeMedic1", "fakeMedic2")),
				new ArrayList<>(Arrays.asList("fakeAllergy1")));
		medicalrecordMikeDoe = new Medicalrecord("Mike", "Doe", dateFor15YearsOld,
				new ArrayList<>(Arrays.asList("fakeMedic1", "fakeMedic2", "fakeMedic3")),
				new ArrayList<>());
		medicalrecordJackDoe = new Medicalrecord("Jack", "Doe", dateFor35YearsOld,
				new ArrayList<>(Arrays.asList("fakeMedic1", "fakeMedic2")),
				new ArrayList<>(Arrays.asList("fakeAllergy1", "fakeAllergy2")));
		medicalrecordJasonYoung = new Medicalrecord("Jason", "Young", dateFor19YearsOld, 
				new ArrayList<>(), 
				new ArrayList<>());
		medicalrecordMikeOld = new Medicalrecord("Mike", "Old", dateFor80YearsOld,
				new ArrayList<>(Arrays.asList("fakeMedic1", "fakeMedic2", "fakeMedic3")),
				new ArrayList<>(Arrays.asList("fakeAllergy1", "fakeAllergy2")));
		medicalrecordInitialList = new ArrayList<>(
				Arrays.asList(medicalrecordJohnDoe,medicalrecordMikeDoe,medicalrecordJackDoe,medicalrecordJasonYoung,medicalrecordMikeOld));


		personInitialList = new ArrayList<>(
				Arrays.asList(new Person("John", "Doe", "1-1111", 12345, "adress1", "Gotham", "johndoe@mail.com"),
						new Person("Mike", "Doe", "1-1111", 12345, "adress1", "Gotham", "mikedoe@mail.com"),
						new Person("Jack", "Doe", "1-1111", 12345, "adress1", "Gotham", "jackdoe@mail.com"),
						new Person("Jason", "Young", "2-2222", 78965, "adress2", "New-York", "jasonyoung@mail.com"),
						new Person("Mike", "Old", "3-3333", 95175, "adress3", "Los Angeles", "mikeold@mail.com"),
						new Person("Clark", "Kent", "4-4444", 99999, "adress4", "Metropolis", "superman@mail.com")));
		personListForAddress1 = new ArrayList<>(
				Arrays.asList(new Person("John", "Doe", "1-1111", 12345, "adress1", "Gotham", "johndoe@mail.com"),
						new Person("Mike", "Doe", "1-1111", 12345, "adress1", "Gotham", "mikedoe@mail.com"),
						new Person("Jack", "Doe", "1-1111", 12345, "adress1", "Gotham", "jackdoe@mail.com")));
		personListForAddress2 = new ArrayList<>(
				Arrays.asList(new Person("Jason", "Young", "2-2222", 78965, "adress2", "New-York", "jasonyoung@mail.com")));
		personListForAddress3 = new ArrayList<>(
				Arrays.asList(new Person("Mike", "Old", "3-3333", 95175, "adress3", "Los Angeles", "mikeold@mail.com")));
		personJohnDoe = new Person("John", "Doe", "1-1111", 12345, "adress1", "Gotham", "johndoe@mail.com");
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
	@DisplayName("Get Persons By Stationnumber + Number of adults and children, MAP Version")
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

		when(firestationRepositoryMock.getByStationnumber(1)).thenReturn(firestationAdressesForStationNumber1);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John","Doe")).thenReturn(medicalrecordJohnDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jack","Doe")).thenReturn(medicalrecordJackDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Mike","Doe")).thenReturn(medicalrecordMikeDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jason","Young")).thenReturn(medicalrecordJasonYoung);
		when(personRepositoryMock.getAll()).thenReturn(personInitialList);

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

		when(firestationRepositoryMock.getByStationnumber(1)).thenReturn(firestationAdressesForStationNumber1);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John","Doe")).thenReturn(medicalrecordJohnDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jack","Doe")).thenReturn(medicalrecordJackDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Mike","Doe")).thenReturn(medicalrecordMikeDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jason","Young")).thenReturn(medicalrecordJasonYoung);
		when(personRepositoryMock.getAll()).thenReturn(personInitialList);

		//Act
		String result = SafetynetalertsServiceCUT.getPersonsByStationnumberString(1);

		//Assert
		assertEquals(expected.toString(),result,"The 2 String must have the dame data");
	}

	@Test
	@DisplayName("Get Children By Address + List of other family members")
	void test_getChildrenByAddressAndListOtherFamilyMembers() {
		//Arrange
		StringBuilder expected = new StringBuilder();
		expected.append("John Doe age=5, familyMembers: Mike Doe,Jack Doe<br>");
		expected.append("Mike Doe age=15, familyMembers: John Doe,Jack Doe<br>");

		when(personRepositoryMock.getByAddress("adress1")).thenReturn(personListForAddress1);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John","Doe")).thenReturn(medicalrecordJohnDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jack","Doe")).thenReturn(medicalrecordJackDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Mike","Doe")).thenReturn(medicalrecordMikeDoe);

		//Act
		String result = SafetynetalertsServiceCUT.getChildrenByAddressAndListOtherFamilyMembers("adress1");

		//Assert
		assertEquals(expected.toString(),result,"The 2 String must have the dame data");
	}

	@Test
	@DisplayName("Get phone numbers for a specific firestation number")
	void test_getPhoneNumbersForStationNumber() {
		//Arrange
		StringBuilder expected = new StringBuilder();
		expected.append("1-1111<br>");
		expected.append("2-2222");

		when(firestationRepositoryMock.getByStationnumber(1)).thenReturn(firestationAdressesForStationNumber1);
		when(personRepositoryMock.getAll()).thenReturn(personInitialList);

		//Act
		String result = SafetynetalertsServiceCUT.getPhoneNumbersForStationNumber(1);

		//Assert
		assertEquals(expected.toString(),result,"The 2 String must have the dame data");
	}

	/**
	 * http://localhost:8080/fire?address=<address>Cette url doit retourner la liste des habitants vivant
	 *  à l’adresse donnée ainsi que le numéro de la casernede pompiers la desservant.
	 *  La liste doit inclure le nom, le numéro de téléphone, l'âge et les antécédents
	 *  médicaux (médicaments, posologie et allergies) de chaque personne.
	 */
	@Test
	@DisplayName("Get persons, medical and firestation informations for a specific address")
	void test_getPersonsFirestationAndMedicalRecordByAddress() {
		//Arrange
		StringBuilder expected = new StringBuilder();
		expected.append("John Doe phone=1-1111 age=5 firestation=1 medications=[fakeMedic1, fakeMedic2] allergies=[fakeAllergy1]<br>");
		expected.append("Mike Doe phone=1-1111 age=15 firestation=1 medications=[fakeMedic1, fakeMedic2, fakeMedic3] allergies=[]<br>");
		expected.append("Jack Doe phone=1-1111 age=35 firestation=1 medications=[fakeMedic1, fakeMedic2] allergies=[fakeAllergy1, fakeAllergy2]<br>");

		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John","Doe")).thenReturn(medicalrecordJohnDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jack","Doe")).thenReturn(medicalrecordJackDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Mike","Doe")).thenReturn(medicalrecordMikeDoe);
		when(firestationRepositoryMock.getByAddress("adress1")).thenReturn(1);
		when(personRepositoryMock.getByAddress("adress1")).thenReturn(personListForAddress1);

		//Act
		String result = SafetynetalertsServiceCUT.getPersonsFirestationAndMedicalRecordByAddress("adress1");

		//Assert
		assertEquals(expected.toString(),result,"The 2 String must have the dame data");
	}

	/**
	 * http://localhost:8080/flood/stations?stations=<a list of station_numbers>Cette url doit retourner une liste
	 *  de tous les foyers desservis par la caserne. Cette liste doit regrouper les personnes par adresse. Elle doit
	 *   aussi inclure le nom, le numéro de téléphone et l'âge des habitants, etfaire figurer leurs antécédents médicaux
	 *    (médicaments, posologie et allergies) à côté de chaque nom.
	 */
	@Test
	@DisplayName("Get addresses, list of persons per station_number")
	void test_getPersonsAndMedicalRecordByStationNumberAndAddresses() {
		//Arrange
		StringBuilder expected = new StringBuilder();
		expected.append("FIRESTATION NUMBER: 1<br>");
		expected.append("address: adress1<br>");
		expected.append("John Doe phone=1-1111 age=5 medications=[fakeMedic1, fakeMedic2] allergies=[fakeAllergy1]<br>");
		expected.append("Mike Doe phone=1-1111 age=15 medications=[fakeMedic1, fakeMedic2, fakeMedic3] allergies=[]<br>");
		expected.append("Jack Doe phone=1-1111 age=35 medications=[fakeMedic1, fakeMedic2] allergies=[fakeAllergy1, fakeAllergy2]<br>");
		expected.append("address: adress2<br>");
		expected.append("Jason Young phone=2-2222 age=19 medications=[] allergies=[]<br>");
		expected.append("FIRESTATION NUMBER: 2<br>");
		expected.append("address: adress3<br>");
		expected.append("Mike Old phone=3-3333 age=80 medications=[fakeMedic1, fakeMedic2, fakeMedic3] allergies=[fakeAllergy1, fakeAllergy2]<br>");

		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John","Doe")).thenReturn(medicalrecordJohnDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jack","Doe")).thenReturn(medicalrecordJackDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Mike","Doe")).thenReturn(medicalrecordMikeDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jason","Young")).thenReturn(medicalrecordJasonYoung);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Mike","Old")).thenReturn(medicalrecordMikeOld);

		when(firestationRepositoryMock.getByStationnumber(1)).thenReturn(firestationAdressesForStationNumber1);
		when(firestationRepositoryMock.getByStationnumber(2)).thenReturn(firestationAdressesForStationNumber2);

		when(personRepositoryMock.getByAddress("adress1")).thenReturn(personListForAddress1);
		when(personRepositoryMock.getByAddress("adress2")).thenReturn(personListForAddress2);
		when(personRepositoryMock.getByAddress("adress3")).thenReturn(personListForAddress3);

		//Act
		List<Integer> listFirestations = Arrays.asList(1,2);
		String result = SafetynetalertsServiceCUT.getAddressesListOfPersonsPerStationNumberList(listFirestations);

		//Assert
		assertEquals(expected.toString(),result,"The 2 String must have the dame data");
	}

	/*
	 * http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
	 * Cette url doit retourner le nom, l'adresse, l'âge, l'adresse mail et les antécédents médicaux
	 *  (médicaments,posologie, allergies) de chaque habitant. Si plusieurs personnes portent le même
	 *   nom, elles doivent toutes apparaître.
	 */
	void test_getPersonInfoByFirstNameAndLastName() {

		//Arrange
		StringBuilder expected = new StringBuilder();
		expected.append("John Doe phone=1-1111 age=5 medications=[fakeMedic1, fakeMedic2] allergies=[fakeAllergy1]<br>");
		expected.append("Mike Doe phone=1-1111 age=15 medications=[fakeMedic1, fakeMedic2, fakeMedic3] allergies=[]<br>");
		expected.append("Jack Doe phone=1-1111 age=35 medications=[fakeMedic1, fakeMedic2] allergies=[fakeAllergy1, fakeAllergy2]<br>");

		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John","Doe")).thenReturn(medicalrecordJohnDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jack","Doe")).thenReturn(medicalrecordJackDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Mike","Doe")).thenReturn(medicalrecordMikeDoe);

		when(personRepositoryMock.getByFirstnameLastname("John", "Doe")).thenReturn(personJohnDoe);
		when(personRepositoryMock.getByLastname("Doe")).thenReturn(personListForAddress1);

		//Act
		String result = SafetynetalertsServiceCUT.getPersonInfoByFirstNameAndLastName("John", "Doe");

		//Assert
		assertEquals(expected.toString(),result,"The 2 String must have the dame data");

	}
}






