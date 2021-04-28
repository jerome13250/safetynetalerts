package com.safetynet.alertsapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.model.Person;
import com.safetynet.alertsapp.repository.FirestationRepository;
import com.safetynet.alertsapp.repository.MedicalrecordRepository;
import com.safetynet.alertsapp.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
class SafetynetalertsServiceTest {

	private final Logger logger = LoggerFactory.getLogger(SafetynetalertsServiceTest.class);
	ObjectMapper mapper = new ObjectMapper();

	List<Firestation> firestationInitialList;
	List<Firestation> firestationAdressesForStationNumber1;
	List<Firestation> firestationAdressesForStationNumber2;
	//MedicalRecord
	List<Medicalrecord> medicalrecordInitialList;
	Medicalrecord medicalrecordJohnDoe;
	Medicalrecord medicalrecordMikeDoe;
	Medicalrecord medicalrecordJackDoe;
	Medicalrecord medicalrecordJasonYoung;
	Medicalrecord medicalrecordMikeOld;
	//Person
	List<Person> personInitialList;
	List<Person> personListForAddress1;
	List<Person> personListForAddress2;
	List<Person> personListForAddress3;
	List<Person> personListForGotham;
	Person personJohnDoe;
	Person personMikeDoe;
	Person personJackDoe;
	Person personJasonYoung;
	Person personMikeOld;
	Person personClarkKent;
	Person NoMedicalRecordDoe;

	final LocalDate dateNow = LocalDate.now(); 
	LocalDate dateFor5YearsOld;
	LocalDate dateFor15YearsOld;
	LocalDate dateFor19YearsOld;
	LocalDate dateFor35YearsOld;
	LocalDate dateFor80YearsOld;

	@InjectMocks
	SafetynetalertsService SafetynetalertsServiceCUT;

	@Mock
	FirestationRepository firestationRepositoryMock;

	@Mock
	MedicalrecordRepository medicalrecordRepositoryMock;

	@Mock
	PersonRepository personRepositoryMock;

	@BeforeEach
	void initializeData() {
		// create Dates for specific age
		dateFor5YearsOld = dateNow.minusYears(5);
		dateFor15YearsOld = dateNow.minusYears(15);
		dateFor19YearsOld = dateNow.minusYears(19);
		dateFor35YearsOld = dateNow.minusYears(35);
		dateFor80YearsOld = dateNow.minusYears(80);

		firestationInitialList = new ArrayList<>(Arrays.asList(
				new Firestation("address1", 1),
				new Firestation("address2", 1), 
				new Firestation("address3", 2),
				new Firestation("address4", 3)));
		firestationAdressesForStationNumber1 = new ArrayList<>(Arrays.asList(
				new Firestation("address1", 1),
				new Firestation("address2", 1)));
		firestationAdressesForStationNumber2 = new ArrayList<>(Arrays.asList(
				new Firestation("address3", 2)));

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

		personJohnDoe = new Person("John", "Doe", "1-1111", 12345, "address1", "Gotham", "johndoe@mail.com");
		personMikeDoe = new Person("Mike", "Doe", "1-1111", 12345, "address1", "Gotham", "mikedoe@mail.com");
		personJackDoe = new Person("Jack", "Doe", "1-1111", 12345, "address1", "Gotham", "jackdoe@mail.com");
		NoMedicalRecordDoe = new Person("NoMedicalRecord", "Doe", "0-0000", 78965, "address1", "Gotham", "nomedicalrecorddoe@mail.com");
		personJasonYoung = new Person("Jason", "Young", "2-2222", 78965, "address2", "Gotham", "jasonyoung@mail.com");
		personMikeOld = new Person("Mike", "Old", "3-3333", 95175, "address3", "Los Angeles", "mikeold@mail.com");
		personClarkKent = new Person("Clark", "Kent", "4-4444", 99999, "address4", "Metropolis", "superman@mail.com");


		personInitialList = new ArrayList<>(
				Arrays.asList(personJohnDoe,personMikeDoe,personJackDoe,NoMedicalRecordDoe,personJasonYoung,personMikeOld,personClarkKent));
		personListForAddress1 = new ArrayList<>(
				Arrays.asList(personJohnDoe,personMikeDoe,personJackDoe,NoMedicalRecordDoe));
		personListForAddress2 = new ArrayList<>(
				Arrays.asList(personJasonYoung));
		personListForAddress3 = new ArrayList<>(
				Arrays.asList(personMikeOld));
		personListForGotham = new ArrayList<>(
				Arrays.asList(personJohnDoe,personMikeDoe,personJackDoe,personJasonYoung,NoMedicalRecordDoe));

	}

	/**
	 * Create a map from 2 Arrays, one contains keys, the other contains objects.
	 *
	 * @param keys contains the Map keys
	 * @param objects contains the Map values
	 * @return created with the 2 arrays
	 */
	private ObjectNode createObjectNodeFromArray(String[] keys , String[] values) {
		ObjectNode node = mapper.createObjectNode();
		for (int i = 0; i < keys.length; i++) {
			node.put(keys[i], values[i]);
		}
		return node;
	}

	@Test
	@DisplayName("Get Persons By Stationnumber + Number of adults and children")
	void test_getPersonByStationnumber() {
		//Arrange
		ObjectNode expected = mapper.createObjectNode();
		ArrayNode persons = expected.putArray("persons");
		//Persons au format: Prénom, nom, addresse, numéro de téléphone.
		String[] keys = {"firstName","lastName","address","phone"};
		persons.add(createObjectNodeFromArray(keys,new String[] {"John","Doe","address1","1-1111"}));
		persons.add(createObjectNodeFromArray(keys,new String[] {"Mike","Doe","address1","1-1111"}));
		persons.add(createObjectNodeFromArray(keys,new String[] {"Jack","Doe","address1","1-1111"}));
		persons.add(createObjectNodeFromArray(keys,new String[] {"NoMedicalRecord","Doe","address1","0-0000"}));
		persons.add(createObjectNodeFromArray(keys,new String[] {"Jason","Young","address2","2-2222"}));

		//décompte du nombre d'adultes et du nombre d'enfants
		expected.put("adults", 2);
		expected.put("children", 3); //NoMedicalRecord doe has no medicalrecord so age is considered as 0

		when(firestationRepositoryMock.getByStationnumber(1)).thenReturn(firestationAdressesForStationNumber1);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John","Doe")).thenReturn(medicalrecordJohnDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jack","Doe")).thenReturn(medicalrecordJackDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Mike","Doe")).thenReturn(medicalrecordMikeDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("NoMedicalRecord","Doe")).thenReturn(null);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jason","Young")).thenReturn(medicalrecordJasonYoung);

		when(personRepositoryMock.getAll()).thenReturn(personInitialList);

		//Act
		JsonNode result = SafetynetalertsServiceCUT.getPersonsByStationnumber(1);

		//Assert
		assertEquals(expected,result,"The 2 json must have the dame data");
	}

	@Test
	@DisplayName("Get Children By Address + List of other family members")
	void test_getChildrenByAddressAndListOtherFamilyMembers() {
		//Arrange
		ArrayNode expected = mapper.createArrayNode();
		ObjectNode person1 = expected.addObject();
		person1.put("firstName", "John").put("lastName", "Doe").put("age", 5);
		ArrayNode family1 = person1.putArray("family");
		family1.addObject().put("firstName", "Mike").put("lastName", "Doe");
		family1.addObject().put("firstName", "Jack").put("lastName", "Doe");
		family1.addObject().put("firstName", "NoMedicalRecord").put("lastName", "Doe");
		ObjectNode person2 = expected.addObject();
		person2.put("firstName", "Mike").put("lastName", "Doe").put("age", 15);
		ArrayNode family2 = person2.putArray("family");
		family2.addObject().put("firstName", "John").put("lastName", "Doe");
		family2.addObject().put("firstName", "Jack").put("lastName", "Doe");
		family2.addObject().put("firstName", "NoMedicalRecord").put("lastName", "Doe");
		ObjectNode person3 = expected.addObject(); //No medical record => age=0
		person3.put("firstName", "NoMedicalRecord").put("lastName", "Doe").put("age", 0);
		ArrayNode family3 = person3.putArray("family");
		family3.addObject().put("firstName", "John").put("lastName", "Doe");
		family3.addObject().put("firstName", "Mike").put("lastName", "Doe");
		family3.addObject().put("firstName", "Jack").put("lastName", "Doe");

		when(personRepositoryMock.getByAddress("address1")).thenReturn(personListForAddress1);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John","Doe")).thenReturn(medicalrecordJohnDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jack","Doe")).thenReturn(medicalrecordJackDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Mike","Doe")).thenReturn(medicalrecordMikeDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("NoMedicalRecord","Doe")).thenReturn(null);

		//Act
		JsonNode result = SafetynetalertsServiceCUT.getChildrenByAddressAndListOtherFamilyMembers("address1");

		//Assert
		assertEquals(expected,result,"The 2 String must have the dame data");
	}

	@Test
	@DisplayName("Get phone numbers for a specific firestation number")
	void test_getPhoneNumbersForStationNumber() {
		//Arrange
		ArrayNode expected = mapper.createArrayNode();
		expected.addObject().put("phone", "1-1111");
		expected.addObject().put("phone", "0-0000");
		expected.addObject().put("phone", "2-2222");

		when(firestationRepositoryMock.getByStationnumber(1)).thenReturn(firestationAdressesForStationNumber1);
		when(personRepositoryMock.getAll()).thenReturn(personInitialList);

		//Act
		JsonNode result = SafetynetalertsServiceCUT.getPhoneNumbersForStationNumber(1);

		//Assert
		assertEquals(expected,result,"The 2 Json must have the dame data");
	}

	/**
	 * http://localhost:8080/fire?address=<address>Cette url doit retourner la liste des habitants vivant
	 *  à l’addresse donnée ainsi que le numéro de la casernede pompiers la desservant.
	 *  La liste doit inclure le nom, le numéro de téléphone, l'âge et les antécédents
	 *  médicaux (médicaments, posologie et allergies) de chaque personne.
	 */
	@Test
	@DisplayName("Get persons, medical and firestation informations for a specific address")
	void test_getPersonsFirestationAndMedicalRecordByAddress() {
		//Arrange
		ArrayNode expected = mapper.createArrayNode();
		//John Doe
		ObjectNode p1 = expected.addObject().put("firstname","John").put("lastname","Doe").put("phone","1-1111").put("age",5).put("firestation",1);
		ArrayNode med1 = p1.putArray("medications");
		med1.addObject().put("medication","fakeMedic1");
		med1.addObject().put("medication","fakeMedic2");
		ArrayNode allergy1 = p1.putArray("allergies");
		allergy1.addObject().put("allergy","fakeAllergy1");
		//Mike Doe
		ObjectNode p2 = expected.addObject().put("firstname","Mike").put("lastname","Doe").put("phone","1-1111").put("age",15).put("firestation",1);
		ArrayNode med2 = p2.putArray("medications");
		med2.addObject().put("medication","fakeMedic1");
		med2.addObject().put("medication","fakeMedic2");
		med2.addObject().put("medication","fakeMedic3");
		ArrayNode allergy2 = p2.putArray("allergies");
		//Jack Doe
		ObjectNode p3 = expected.addObject().put("firstname","Jack").put("lastname","Doe").put("phone","1-1111").put("age",35).put("firestation",1);
		ArrayNode med3 = p3.putArray("medications");
		med3.addObject().put("medication","fakeMedic1");
		med3.addObject().put("medication","fakeMedic2");
		ArrayNode allergy3 = p3.putArray("allergies");
		allergy3.addObject().put("allergy","fakeAllergy1");
		allergy3.addObject().put("allergy","fakeAllergy2");
		//NoMedicalRecord Doe
		ObjectNode p4 = expected.addObject().put("firstname","NoMedicalRecord").put("lastname","Doe").put("phone","0-0000").put("age",0).put("firestation",1);
		ArrayNode med4 = p4.putArray("medications");
		ArrayNode allergy4 = p4.putArray("allergies");


		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John","Doe")).thenReturn(medicalrecordJohnDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jack","Doe")).thenReturn(medicalrecordJackDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Mike","Doe")).thenReturn(medicalrecordMikeDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("NoMedicalRecord","Doe")).thenReturn(null);
		when(firestationRepositoryMock.getByAddress("address1")).thenReturn(1);
		when(personRepositoryMock.getByAddress("address1")).thenReturn(personListForAddress1);

		//Act
		JsonNode result = SafetynetalertsServiceCUT.getPersonsFirestationAndMedicalRecordByAddress("address1");

		//Assert
		assertEquals(expected,result,"The 2 String must have the same data");
	}

	/**
	 * http://localhost:8080/flood/stations?stations=<a list of station_numbers>Cette url doit retourner une liste
	 *  de tous les foyers desservis par la caserne. Cette liste doit regrouper les personnes par addresse. Elle doit
	 *   aussi inclure le nom, le numéro de téléphone et l'âge des habitants, etfaire figurer leurs antécédents médicaux
	 *    (médicaments, posologie et allergies) à côté de chaque nom.
	 */
	@Test
	@DisplayName("Get addresses, list of persons per station_number")
	void test_getAddressesListOfPersonsPerStationNumberList() {
		//Arrange
		ArrayNode expected = mapper.createArrayNode();
		//Firestation 1
		ObjectNode firestation1 = expected.addObject();
		firestation1.put("stationnumber", 1);
		ArrayNode addressesList1 = firestation1.putArray("addresses");
		//Firestation 1 / address 1
		ObjectNode address1ForList1 = addressesList1.addObject();
		address1ForList1.put("address", "address1");
		ArrayNode personList1 = address1ForList1.putArray("persons");
		//John Doe
		ObjectNode address1p1 = personList1.addObject().put("firstname","John").put("lastname","Doe").put("phone","1-1111").put("age",5);
		ArrayNode address1med1 = address1p1.putArray("medications");
		address1med1.addObject().put("medication","fakeMedic1");
		address1med1.addObject().put("medication","fakeMedic2");
		ArrayNode address1allergy1 = address1p1.putArray("allergies");
		address1allergy1.addObject().put("allergy","fakeAllergy1");
		//Mike Doe
		ObjectNode address1p2 = personList1.addObject().put("firstname","Mike").put("lastname","Doe").put("phone","1-1111").put("age",15);
		ArrayNode address1med2 = address1p2.putArray("medications");
		address1med2.addObject().put("medication","fakeMedic1");
		address1med2.addObject().put("medication","fakeMedic2");
		address1med2.addObject().put("medication","fakeMedic3");
		ArrayNode address1allergy2 = address1p2.putArray("allergies");
		//Jack Doe
		ObjectNode address1p3 = personList1.addObject().put("firstname","Jack").put("lastname","Doe").put("phone","1-1111").put("age",35);
		ArrayNode address1med3 = address1p3.putArray("medications");
		address1med3.addObject().put("medication","fakeMedic1");
		address1med3.addObject().put("medication","fakeMedic2");
		ArrayNode address1allergy3 = address1p3.putArray("allergies");
		address1allergy3.addObject().put("allergy","fakeAllergy1");
		address1allergy3.addObject().put("allergy","fakeAllergy2");
		//NoMedicalRecord Doe
		ObjectNode address1p4 = personList1.addObject().put("firstname","NoMedicalRecord").put("lastname","Doe").put("phone","0-0000").put("age",0);
		ArrayNode address1med4 = address1p4.putArray("medications");
		ArrayNode address1allergy4 = address1p4.putArray("allergies");

		//Firestation 1 / address 2
		ObjectNode address2ForList1 = addressesList1.addObject();
		address2ForList1.put("address", "address2");
		ArrayNode personList2 = address2ForList1.putArray("persons");
		//Jason Young
		ObjectNode address2p1 = personList2.addObject().put("firstname","Jason").put("lastname","Young").put("phone","2-2222").put("age",19);
		ArrayNode address2med1 = address2p1.putArray("medications");
		ArrayNode address2allergy1 = address2p1.putArray("allergies");

		//Firestation 2
		ObjectNode firestation2 = expected.addObject();
		firestation2.put("stationnumber", 2);
		ArrayNode addressesList2 = firestation2.putArray("addresses");
		//Firestation 2 / address 3
		ObjectNode address3ForList2 = addressesList2.addObject();
		address3ForList2.put("address", "address3");
		ArrayNode personList3 = address3ForList2.putArray("persons");
		//Mike Old
		ObjectNode address3p1 = personList3.addObject().put("firstname","Mike").put("lastname","Old").put("phone","3-3333").put("age",80);
		ArrayNode address3med1 = address3p1.putArray("medications");
		address3med1.addObject().put("medication","fakeMedic1");
		address3med1.addObject().put("medication","fakeMedic2");
		address3med1.addObject().put("medication","fakeMedic3");
		ArrayNode address3allergy1 = address3p1.putArray("allergies");
		address3allergy1.addObject().put("allergy","fakeAllergy1");
		address3allergy1.addObject().put("allergy","fakeAllergy2");

		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John","Doe")).thenReturn(medicalrecordJohnDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jack","Doe")).thenReturn(medicalrecordJackDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Mike","Doe")).thenReturn(medicalrecordMikeDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("NoMedicalRecord","Doe")).thenReturn(null);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jason","Young")).thenReturn(medicalrecordJasonYoung);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Mike","Old")).thenReturn(medicalrecordMikeOld);

		when(firestationRepositoryMock.getByStationnumber(1)).thenReturn(firestationAdressesForStationNumber1);
		when(firestationRepositoryMock.getByStationnumber(2)).thenReturn(firestationAdressesForStationNumber2);

		when(personRepositoryMock.getByAddress("address1")).thenReturn(personListForAddress1);
		when(personRepositoryMock.getByAddress("address2")).thenReturn(personListForAddress2);
		when(personRepositoryMock.getByAddress("address3")).thenReturn(personListForAddress3);

		//Act
		List<Integer> listFirestations = Arrays.asList(1,2);
		JsonNode result = SafetynetalertsServiceCUT.getAddressesListOfPersonsPerStationNumberList(listFirestations);

		//Assert
		assertEquals(expected,result,"The 2 String must have the dame data");
	}

	/*
	 * http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
	 * Cette url doit retourner le nom, l'addresse, l'âge, l'addresse mail et les antécédents médicaux
	 *  (médicaments,posologie, allergies) de chaque habitant. Si plusieurs personnes portent le même
	 *   nom, elles doivent toutes apparaître.
	 */
	@Test
	@DisplayName("Get a person info, display persons with same name")
	void test_getPersonInfoByFirstNameAndLastName() {

		//Arrange
		ArrayNode expected = mapper.createArrayNode();
		//John Doe
		ObjectNode p1 = expected.addObject().put("firstname","John").put("lastname","Doe").put("address", "address1")
				.put("email","johndoe@mail.com").put("age",5);
		ArrayNode med1 = p1.putArray("medications");
		med1.addObject().put("medication","fakeMedic1");
		med1.addObject().put("medication","fakeMedic2");
		ArrayNode allergy1 = p1.putArray("allergies");
		allergy1.addObject().put("allergy","fakeAllergy1");
		//Mike Doe
		ObjectNode address1p2 = expected.addObject().put("firstname","Mike").put("lastname","Doe").put("address", "address1")
				.put("email","mikedoe@mail.com").put("age",15);
		ArrayNode med2 = address1p2.putArray("medications");
		med2.addObject().put("medication","fakeMedic1");
		med2.addObject().put("medication","fakeMedic2");
		med2.addObject().put("medication","fakeMedic3");
		ArrayNode allergy2 = address1p2.putArray("allergies");
		//Jack Doe
		ObjectNode p3 = expected.addObject().put("firstname","Jack").put("lastname","Doe").put("address", "address1")
				.put("email","jackdoe@mail.com").put("age",35);
		ArrayNode med3 = p3.putArray("medications");
		med3.addObject().put("medication","fakeMedic1");
		med3.addObject().put("medication","fakeMedic2");
		ArrayNode allergy3 = p3.putArray("allergies");
		allergy3.addObject().put("allergy","fakeAllergy1");
		allergy3.addObject().put("allergy","fakeAllergy2");
		//NoMedicalRecord Doe
		ObjectNode p4 = expected.addObject().put("firstname","NoMedicalRecord").put("lastname","Doe").put("address", "address1")
				.put("email","nomedicalrecorddoe@mail.com").put("age",0);
		ArrayNode med4 = p4.putArray("medications");
		ArrayNode allergy4 = p4.putArray("allergies");


		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("John","Doe")).thenReturn(medicalrecordJohnDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Jack","Doe")).thenReturn(medicalrecordJackDoe);
		when(medicalrecordRepositoryMock.getByFirstnameAndLastName("Mike","Doe")).thenReturn(medicalrecordMikeDoe);
		when(personRepositoryMock.getByFirstnameLastname("John", "Doe")).thenReturn(personJohnDoe);
		when(personRepositoryMock.getByLastname("Doe")).thenReturn(personListForAddress1);

		//Act
		JsonNode result = SafetynetalertsServiceCUT.getPersonInfoByFirstNameAndLastName("John", "Doe");

		//Assert
		assertEquals(expected,result,"The 2 String must have the dame data");

	}

	/**
	 * http://localhost:8080/communityEmail?city=<city>
	 * Cette url doit retourner les addresses mail de tous les habitants de la ville.
	 */
	@Test
	@DisplayName("Get all phones number for a city")
	void test_getPhonesInCity() {
		//Arrange
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode expected = mapper.createArrayNode();
		ObjectNode phone1 = expected.addObject();
		phone1.put("phone","1-1111");
		ObjectNode phone0 = expected.addObject();
		phone0.put("phone","0-0000");
		ObjectNode phone2 = expected.addObject();
		phone2.put("phone","2-2222");

		when(personRepositoryMock.getByCity("Gotham")).thenReturn(personListForGotham);

		//Act
		JsonNode result = SafetynetalertsServiceCUT.getPhonesInCity("Gotham");

		//Assert
		assertEquals(expected,result,"The 2 JSON must have the same data");
	}
	
	@Test
	@DisplayName("test getAllFirestation")
	void test_getAllFirestation() {
		//Arrange
		List<Firestation> expected = new ArrayList<>(Arrays.asList(
				new Firestation("address1", 1),
				new Firestation("address2", 1), 
				new Firestation("address3", 2),
				new Firestation("address4", 3)));

		when(firestationRepositoryMock.getAll()).thenReturn(firestationInitialList);

		//Act
		List<Firestation> result = SafetynetalertsServiceCUT.getAllFirestation();

		//Assert
		assertEquals(expected,result,"The 2 List must have the same data");
	}
	
	@Test
	@DisplayName("test getAllPerson")
	void test_getAllPerson() {
		//Arrange
		List<Person> expected = new ArrayList<>(
				Arrays.asList(personJohnDoe,personMikeDoe,personJackDoe,NoMedicalRecordDoe,personJasonYoung,personMikeOld,personClarkKent));
		when(personRepositoryMock.getAll()).thenReturn(personInitialList);

		//Act
		List<Person> result = SafetynetalertsServiceCUT.getAllPerson();

		//Assert
		assertEquals(expected,result,"The 2 List must have the same data");
	}
	
	@Test
	@DisplayName("test getAllMedicalrecord")
	void test_getAllMedicalrecord() {
		//Arrange
		List<Medicalrecord> expected = new ArrayList<>(
				Arrays.asList(medicalrecordJohnDoe,medicalrecordMikeDoe,medicalrecordJackDoe,medicalrecordJasonYoung,medicalrecordMikeOld));
		when(medicalrecordRepositoryMock.getAll()).thenReturn(medicalrecordInitialList);

		//Act
		List<Medicalrecord> result = SafetynetalertsServiceCUT.getAllMedicalrecord();

		//Assert
		assertEquals(expected,result,"The 2 List must have the same data");
	}
	
	
}






