package com.safetynet.alertsapp.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alertsapp.model.Firestation;
import com.safetynet.alertsapp.model.Medicalrecord;
import com.safetynet.alertsapp.model.Person;
import com.safetynet.alertsapp.repository.IFirestationRepository;
import com.safetynet.alertsapp.repository.IMedicalrecordRepository;
import com.safetynet.alertsapp.repository.IPersonRepository;

@Service
public class SafetynetalertsServiceImpl implements ISafetynetalertsService {

	private final Logger logger = LoggerFactory.getLogger(SafetynetalertsServiceImpl.class);
	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	IFirestationRepository firestationRepository;

	@Autowired
	IMedicalrecordRepository medicalrecordRepository;

	@Autowired
	IPersonRepository personRepository;

	private Integer calculateAge(LocalDate birthdate) {
		Period p = Period.between(birthdate, LocalDate.now());
		return p.getYears();
	}

	@Override
	public JsonNode getPersonsByStationnumber(int stationNumber) {

		//This will contain the data :
		ObjectNode result = mapper.createObjectNode();
		ArrayNode persons = result.putArray("persons");
		int numberOfadults = 0;
		int numberOfChildren = 0;
 
		//Get all addresses with stationNumber:
		List<String> adressesByStationnumber = firestationRepository.getByStationnumber(stationNumber).stream().map(Firestation::getAddress).collect(Collectors.toList());
		for (Person p: personRepository.getAll() ) { 
			logger.debug("Person p: {}", p);
			if (adressesByStationnumber.contains(p.getAddress())) {
				//Create Node that contains only needed informations: Prénom, nom, adresse, numéro de téléphone.
				ObjectNode person = persons.addObject();
				person.put("firstName", p.getFirstName());
				person.put("lastName", p.getLastName());
				person.put("address", p.getAddress());
				person.put("phone", p.getPhone());

				//use loop to compute children/adults:
				Medicalrecord med = medicalrecordRepository.getByFirstnameAndLastName(
						p.getFirstName(),
						p.getLastName());
				//On gere le cas Medicalrecord==NULL en creant un Medicalrecord par defaut, cela evite un envoi 
				//d'erreur empechant toute requete GET si un seul enregistrement est incomplet.
				if (med==null) {
					med = new Medicalrecord(p.getFirstName(),p.getLastName(), LocalDate.now(),new ArrayList<>(), new ArrayList<>());
				}
				
				if (calculateAge(med.getBirthdate()) >=18 ) {
					numberOfadults++;
				}
				else {
					numberOfChildren++;
				}
			}
		}
		result.put("adults", numberOfadults);
		result.put("children", numberOfChildren);

		return result;
	}

	@Override
	public JsonNode getChildrenByAddressAndListOtherFamilyMembers(String address) {
		//This will contain the data :
		ArrayNode result = mapper.createArrayNode();

		//Get All children at a specific address:
		List<Person> personListForAddress = personRepository.getByAddress(address);
		for(Person p : personListForAddress) {
			Medicalrecord med = medicalrecordRepository.getByFirstnameAndLastName(
					p.getFirstName(),
					p.getLastName());
			//On gere le cas Medicalrecord==NULL en creant un Medicalrecord par defaut, cela evite un envoi 
			//d'erreur empechant toute requete GET si un seul enregistrement est incomplet.
			if (med==null) {
				med = new Medicalrecord(p.getFirstName(),p.getLastName(), LocalDate.now(),new ArrayList<>(), new ArrayList<>());
			}

			if(calculateAge(med.getBirthdate())<18) {
				ObjectNode person = result.addObject();
				person.put("firstName", p.getFirstName());
				person.put("lastName", p.getLastName());
				person.put("age", calculateAge(med.getBirthdate()));

				ArrayNode family = person.putArray("family");
				personListForAddress.stream().filter(p1-> !( //remove the child from list
						p1.getFirstName().equals(p.getFirstName()) &&
						p1.getLastName().equals(p.getLastName()))
						).forEach(p2->{
							ObjectNode familyMember = family.addObject();
							familyMember.put("firstName", p2.getFirstName());
							familyMember.put("lastName", p2.getLastName());
						});
			}
		}
		return result;
	}

	@Override
	public JsonNode getPhoneNumbersForStationNumber(int stationNumber) {

		//This will contain the phone numbers, HashSet to avoid doubles :
		Set<String> phoneNumbersSet = new HashSet<>();

		//Get all addresses with stationNumber:
		List<String> adressesByStationnumber = firestationRepository.getByStationnumber(stationNumber).stream().map(Firestation::getAddress).collect(Collectors.toList());
		logger.debug("adressesByStationnumber: {}",adressesByStationnumber);

		for (Person p: personRepository.getAll() ) {
			logger.trace("Person p: {}", p);
			if (adressesByStationnumber.contains(p.getAddress())) {
				phoneNumbersSet.add(p.getPhone());
			}
		}

		//hasSet to json:
		ArrayNode result = mapper.createArrayNode();
		phoneNumbersSet.stream().forEach(s-> result.addObject().put("phone",s));

		return result;
	}

	@Override
	public JsonNode getPersonsFirestationAndMedicalRecordByAddress(String address) {
			//This will contain the data :
			ArrayNode result = mapper.createArrayNode();

			List<Person> personList = personRepository.getByAddress(address);
			Integer firestationNumber = firestationRepository.getByAddress(address);
			
			for(Person p: personList) {
				Medicalrecord med = medicalrecordRepository.getByFirstnameAndLastName(
						p.getFirstName(),
						p.getLastName());
				//On gere le cas Medicalrecord==NULL en creant un Medicalrecord par defaut, cela evite un envoi 
				//d'erreur empechant toute requete GET si un seul enregistrement est incomplet.
				if (med==null) {
					med = new Medicalrecord(p.getFirstName(),p.getLastName(), LocalDate.now(),new ArrayList<>(), new ArrayList<>());
				}

				//"Jack Doe phone=1-1111 age=35 firestation=1 medications=fakeMedic1,fakeMedic2, allergies=fakeAllergy1,fakeAllergy2"
				ObjectNode obj = result.addObject().put("firstname", p.getFirstName()).put("lastname", p.getLastName()).put("phone", p.getPhone())
						.put("age", calculateAge(med.getBirthdate())).put("firestation", firestationNumber);
				ArrayNode medications = obj.putArray("medications");
				med.getMedications().stream().forEach(m->medications.addObject().put("medication",m));
				ArrayNode allergies = obj.putArray("allergies");
				med.getAllergies().stream().forEach(a->allergies.addObject().put("allergy",a));
			}

			return result;
	}

	@Override
	public JsonNode getAddressesListOfPersonsPerStationNumberList(List<Integer> firestationNumberList) {
		//This will contain the data :
		ArrayNode result = mapper.createArrayNode();

		for(Integer i : firestationNumberList) {
			ObjectNode firestationObjectNode = result.addObject();
			firestationObjectNode.put("stationnumber", i);
			ArrayNode addressesArray = firestationObjectNode.putArray("addresses");

			List<Firestation> firestationList = firestationRepository.getByStationnumber(i);
			for(Firestation f : firestationList) {
				ObjectNode addressNode = addressesArray.addObject();
				addressNode.put("address", f.getAddress());
				List<Person> personList = personRepository.getByAddress(f.getAddress());
				ArrayNode personArray = addressNode.putArray("persons");
				for (Person p : personList) {
					Medicalrecord med = medicalrecordRepository.getByFirstnameAndLastName(
							p.getFirstName(),
							p.getLastName());
					//On gere le cas Medicalrecord==NULL en creant un Medicalrecord par defaut, cela evite un envoi 
					//d'erreur empechant toute requete GET si un seul enregistrement est incomplet.
					if (med==null) {
						med = new Medicalrecord(p.getFirstName(),p.getLastName(), LocalDate.now(),new ArrayList<>(), new ArrayList<>());
					}
					
					//"Jack Doe phone=1-1111 age=35 firestation=1 medications=fakeMedic1,fakeMedic2, allergies=fakeAllergy1,fakeAllergy2"
					ObjectNode personNode = personArray.addObject().put("firstname",p.getFirstName()).put("lastname",p.getLastName())
							.put("phone",p.getPhone()).put("age",calculateAge(med.getBirthdate()));
					ArrayNode medications = personNode.putArray("medications");
					med.getMedications().forEach(m->medications.addObject().put("medication",m));
					ArrayNode allergies = personNode.putArray("allergies");
					med.getAllergies().forEach(a->allergies.addObject().put("allergy",a));
				}
			}
		}
		return result;
	}

	@Override
	public JsonNode getPersonInfoByFirstNameAndLastName(String firstname, String lastname) {

		//This will contain the data :
		ArrayNode result = mapper.createArrayNode();

		Person person = personRepository.getByFirstnameLastname(firstname, lastname);
		if (person!=null) {
			for (Person p: personRepository.getByLastname(person.getLastName())) {
				Medicalrecord med = medicalrecordRepository.getByFirstnameAndLastName(
						p.getFirstName(),
						p.getLastName());
				//On gere le cas Medicalrecord==NULL en creant un Medicalrecord par defaut, cela evite un envoi 
				//d'erreur empechant toute requete GET si un seul enregistrement est incomplet.
				if (med==null) {
					med = new Medicalrecord(p.getFirstName(),p.getLastName(), LocalDate.now(),new ArrayList<>(), new ArrayList<>());
				}
				
				ObjectNode personNode = result.addObject().put("firstname",p.getFirstName()).put("lastname",p.getLastName())
						.put("address",p.getAddress()).put("email",p.getEmail()).put("age",calculateAge(med.getBirthdate()));
				ArrayNode medicationsArray = personNode.putArray("medications");
				med.getMedications().forEach(m->medicationsArray.addObject().put("medication",m));
				ArrayNode allergiesArray = personNode.putArray("allergies");
				med.getAllergies().forEach(a->allergiesArray.addObject().put("allergy",a));

			}
		}
		return result;
	}

	@Override
	public JsonNode getPhonesInCity(String city) {

		//to remove doubles we use HashSet:
		Set<String> phoneSet = new HashSet<>();
		for(Person p: personRepository.getByCity(city)) {
			phoneSet.add(p.getPhone());
		}

		//generate JSON:
		ArrayNode phonesArray = mapper.createArrayNode();
		for(String s: phoneSet) {
			ObjectNode phone = mapper.createObjectNode().put("phone", s);
			phonesArray.add(phone);
		}
		return phonesArray;
	}

	@Override
	public List<Firestation> getAllFirestation() {
		return firestationRepository.getAll();
	}

	@Override
	public List<Medicalrecord> getAllMedicalrecord() {
		return medicalrecordRepository.getAll();
	}
	
	@Override
	public List<Person> getAllPerson() {
		return personRepository.getAll();
	}

}
