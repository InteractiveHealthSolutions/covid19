/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.test.context.TestPropertySource;

import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.DefinitionType;
import com.ihsinformatics.coronavirus.model.Donor;
import com.ihsinformatics.coronavirus.model.Element;
import com.ihsinformatics.coronavirus.model.FormData;
import com.ihsinformatics.coronavirus.model.FormType;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.LocationAttribute;
import com.ihsinformatics.coronavirus.model.LocationAttributeType;
import com.ihsinformatics.coronavirus.model.Participant;
import com.ihsinformatics.coronavirus.model.Person;
import com.ihsinformatics.coronavirus.model.PersonAttribute;
import com.ihsinformatics.coronavirus.model.PersonAttributeType;
import com.ihsinformatics.coronavirus.model.Privilege;
import com.ihsinformatics.coronavirus.model.Project;
import com.ihsinformatics.coronavirus.model.Role;
import com.ihsinformatics.coronavirus.model.User;
import com.ihsinformatics.coronavirus.model.UserAttribute;
import com.ihsinformatics.coronavirus.model.UserAttributeType;
import com.ihsinformatics.coronavirus.util.DataType;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@TestPropertySource(locations = "classpath:application.properties")
public class BaseTestData {

    protected static User admin, dumbledore, snape, tonks, umbridge, luna, fred, george, lily;

    protected static Privilege magic, charm, curse, release, arrest, kill;

    protected static FormType quidditchForm, challengeForm, trainingForm;

    protected static FormData harryData, hermioneData, ronData;

    protected static Set<Privilege> privileges = new HashSet<>();

    protected static Role headmaster;

    protected static UserAttributeType occupation, patronus, blood;

    protected static UserAttribute snapeBlood, tonksBlood, tonksPatronus;

    protected static DefinitionType locationType, country, house, broomStick;

    protected static Definition gryffindor, slytherine, hufflepuff, ravenclaw;

    protected static Definition comet, nimbus, firebolt;

    protected static Definition school, market;

    protected static Definition scotland, france, england;

    protected static Element schoolElement, houseElement, broomstickElement, captainElement, roleElement, numberElement,
	    heightElement, genderElement, dateJoinedElement, refereeElement, titlesElement;

    protected static Location hogwartz, diagonalley, burrow;

    protected static LocationAttributeType noOfStudents, noOfTeachers;

    protected static LocationAttribute noOfHogwartzStudents, noOfDiagonalleyTeachers, noOfHogwartzTeachers;

    protected static Person harry, ron, hermione;

    protected static PersonAttributeType height, socialStatus;

    protected static PersonAttribute ronHeight, ronSocialStatus;

    protected static Participant seeker, keeper, chaser;

    protected static Donor ministry;

    protected static Project triwizardTournament;

    protected Set<User> users = new HashSet<>();

    protected Set<Role> roles = new HashSet<>();

    protected Set<UserAttributeType> userAttributeTypes = new HashSet<>();

    protected Set<UserAttribute> userAttributes = new HashSet<>();

    protected Set<LocationAttributeType> locationAttributeTypes = new HashSet<>();

    protected List<LocationAttribute> locationAttributes = new ArrayList<>();

    protected Set<PersonAttributeType> personAttributeTypes = new HashSet<>();

    protected Set<PersonAttribute> personAttributes = new HashSet<>();

    public void reset() {
	admin = User.builder().userId(1).username("dumbledore").fullName("Administrator").build();
	admin.setPassword("jingle94");
	initData();
    }

    public void initData() {
	initPrivileges();
	initDefinitionTypes();
	initDefinitions();
	initUserAttributeTypes();
	initUserAttributes();
	initRoles();
	initUsers();
	initLocations();
	initLocationAttributeTypes();
	initLocationAttributes();
	initElements();
	initFormTypes();
	initFormData();
	initPeople();
	initPersonAttributeTypes();
	initPersonAttributes();
	initParticipants();
	initDonors();
	initProjects();
    }

    public void initDefinitions() {
	school = Definition.builder().definitionType(locationType).definitionName("School").shortName("SCHOOL").build();
	market = Definition.builder().definitionType(locationType).definitionName("Market").shortName("MARKET").build();
	scotland = Definition.builder().definitionType(country).definitionName("Scotland").shortName("SC").build();
	france = Definition.builder().definitionType(country).definitionName("France").shortName("FR").build();
	england = Definition.builder().definitionType(country).definitionName("England").shortName("EN").build();
	comet = Definition.builder().definitionType(broomStick).definitionName("Comet").shortName("COMET").build();
	nimbus = Definition.builder().definitionType(broomStick).definitionName("Nimbus").shortName("N2000").build();
	firebolt = Definition.builder().definitionType(broomStick).definitionName("Firebolt").shortName("FB").build();
	gryffindor = Definition.builder().definitionType(house).definitionName("Gryffindor").shortName("GRF").build();
	slytherine = Definition.builder().definitionType(house).definitionName("Slytherine").shortName("SLY").build();
	hufflepuff = Definition.builder().definitionType(house).definitionName("Hufflepuff").shortName("HFL").build();
	ravenclaw = Definition.builder().definitionType(house).definitionName("Ravenclaw").shortName("RCW").build();
    }

    public void initDefinitionTypes() {
	locationType = DefinitionType.builder().typeName("Location Type").shortName("LOC_TYPE").build();
	country = DefinitionType.builder().typeName("Country").shortName("COUNTRY").build();
	house = DefinitionType.builder().typeName("House").shortName("HOUSE").build();
	broomStick = DefinitionType.builder().typeName("Broom Stick").shortName("BROOM").build();
    }

    public void initDonors() {
	ministry = Donor.builder().donorName("Ministry of Magic").shortName("MoM").build();
    }

    public void initElements() {
	schoolElement = Element.builder().dataType(DataType.LOCATION).elementName("School Name").shortName("SCHOOL")
		.build();
	houseElement = Element.builder().dataType(DataType.DEFINITION).elementName("House").shortName("HOUSE").build();
	broomstickElement = Element.builder().dataType(DataType.DEFINITION).elementName("Broom Stick Model")
		.shortName("BROOM").build();
	captainElement = Element.builder().dataType(DataType.STRING).elementName("Captain Name").shortName("CAPTAIN")
		.build();
	roleElement = Element.builder().dataType(DataType.DEFINITION).elementName("Team Role").shortName("ROLE")
		.build();
	numberElement = Element.builder().dataType(DataType.INTEGER).elementName("Dress Number").shortName("NO")
		.build();
	heightElement = Element.builder().dataType(DataType.FLOAT).elementName("Height").shortName("HEIGHT").build();
	genderElement = Element.builder().dataType(DataType.CHARACTER).elementName("Gender").shortName("GENDER")
		.build();
	dateJoinedElement = Element.builder().dataType(DataType.DATE).elementName("Date Joined").shortName("JOIN_DATE")
		.build();
	refereeElement = Element.builder().dataType(DataType.USER).elementName("Referred By").shortName("REFEREE")
		.build();
	titlesElement = Element.builder().dataType(DataType.JSON).elementName("Titles").shortName("TITLES").build();
    }

    public void initFormData() {
	harryData = FormData.builder().formType(quidditchForm).formDate(new Date()).referenceId("QP-2000").build();
	Map<String, Object> dataMap = new HashMap<>();
	dataMap.put("house", gryffindor.getUuid());
	dataMap.put("broom", firebolt.getUuid());
	dataMap.put("role", "Seeker");
	harryData.setDataMap(dataMap);
	hermioneData = FormData.builder().formType(quidditchForm).formDate(new Date()).referenceId("QP-2004").build();
	ronData = FormData.builder().formType(quidditchForm).formDate(new Date()).referenceId("QP-1905").build();
	dataMap.put("house", gryffindor.getUuid());
	dataMap.put("broom", nimbus.getUuid());
	dataMap.put("role", "Keeper");
	ronData.setDataMap(dataMap);
    }

    public void initFormTypes() {
	quidditchForm = FormType.builder().formName("Quidditch Participation").shortName("QP Form").build();
	challengeForm = FormType.builder().formName("Challenge Participation Form").shortName("CHALLENGE").build();
	trainingForm = FormType.builder().formName("Training Registration Form").shortName("TRAINING").build();
    }

    public void initLocationAttributes() {
	noOfHogwartzStudents = LocationAttribute.builder().location(hogwartz).attributeType(noOfStudents)
		.attributeValue("1000").build();
	noOfDiagonalleyTeachers = LocationAttribute.builder().location(diagonalley).attributeType(noOfTeachers)
		.attributeValue("20").build();
	noOfHogwartzTeachers = LocationAttribute.builder().location(hogwartz).attributeType(noOfTeachers)
		.attributeValue("50").build();
	locationAttributes.addAll(Arrays.asList(noOfHogwartzStudents, noOfDiagonalleyTeachers, noOfHogwartzTeachers));
    }

    public void initLocationAttributeTypes() {
	noOfStudents = LocationAttributeType.builder().attributeName("Students Enrolled").dataType(DataType.INTEGER)
		.shortName("NO_STUDENTS").isRequired(Boolean.FALSE).build();
	noOfTeachers = LocationAttributeType.builder().attributeName("Teachers Registered").dataType(DataType.INTEGER)
		.shortName("NO_TEACHERS").isRequired(Boolean.FALSE).build();
	locationAttributeTypes.addAll(Arrays.asList(noOfStudents, noOfTeachers));
    }

    public void initLocations() {
	hogwartz = Location.builder().locationName("Hogwarts School of Witchcraft and Wizardry").shortName("HSWW")
		.category(school).country(scotland.getDefinitionName()).primaryContact("+447911123456")
		.attributes(new ArrayList<>()).build();
	diagonalley = Location.builder().locationName("Diagon Alley").shortName("DALLEY").category(market)
		.country(england.getDefinitionName()).primaryContact("+447911654321").attributes(new ArrayList<>())
		.build();
	burrow = Location.builder().locationName("The Burrow").shortName("BURROW").category(market)
		.cityVillage("Ottery St Catchpole").stateProvince("Devon").country(england.getDefinitionName())
		.primaryContact("+447911987654").secondaryContact("+447911123456").attributes(new ArrayList<>())
		.build();
    }

    public void initParticipants() {
	seeker = Participant.builder().location(hogwartz).identifier("SEEKER").build();
	seeker.setPerson(harry);
	keeper = Participant.builder().person(ron).location(hogwartz).identifier("KEEPER").build();
	keeper.setPerson(ron);
	chaser = Participant.builder().person(hermione).location(diagonalley).identifier("CHASER").build();
	chaser.setPerson(hermione);
    }

    public void initPeople() {
	harry = Person.builder().firstName("Harry").lastName("Potter").gender("MALE").dob(new Date())
		.primaryContact("03452345345").country(england.getDefinitionName()).build();
	ron = Person.builder().firstName("Ronald").lastName("Weasley").gender("MALE").dob(new Date())
		.primaryContact("03211233210").secondaryContact("03452345345").country(england.getDefinitionName())
		.build();
	hermione = Person.builder().firstName("Hermione").lastName("Granger").gender("FEMALE").dob(new Date())
		.primaryContact("03451234567").country(scotland.getDefinitionName()).build();
    }

    public void initPersonAttributes() {
	ronHeight = PersonAttribute.builder().person(ron).attributeType(height).attributeValue("5.6").build();
	ronSocialStatus = PersonAttribute.builder().person(ron).attributeType(socialStatus).attributeValue("Married")
		.build();
    }

    public void initPersonAttributeTypes() {
	height = PersonAttributeType.builder().dataType(DataType.FLOAT).attributeName("Height").shortName("HT")
		.validationRegex("range=1-19").build();
	socialStatus = PersonAttributeType.builder().dataType(DataType.STRING).attributeName("Social Status")
		.shortName("STATUS").build();
	personAttributeTypes.addAll(Arrays.asList(height, socialStatus));
    }

    public void initPrivileges() {
	magic = Privilege.builder().privilegeName("USE MAGIC").build();
	charm = Privilege.builder().privilegeName("USE CHARM").build();
	curse = Privilege.builder().privilegeName("USE CURSE").build();
	release = Privilege.builder().privilegeName("RELEASE").build();
	arrest = Privilege.builder().privilegeName("ARREST").build();
	kill = Privilege.builder().privilegeName("KILL").build();
	privileges.addAll(Arrays.asList(magic, charm, curse, release, arrest, kill));
    }

    public void initProjects() {
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.YEAR, 1994);
	Date begin = calendar.getTime();
	calendar.set(Calendar.YEAR, 1995);
	Date end = calendar.getTime();
	triwizardTournament = Project.builder().donor(ministry).projectName("Triwizard Tournament")
		.shortName("MOM-TT-1994").dateGrantBegin(begin).dateGrantEnd(end).build();
    }

    public void initRoles() {
	headmaster = Role.builder().roleName("Headmaster").rolePrivileges(privileges).build();
	headmaster.getRolePrivileges().removeAll(Arrays.asList(kill, arrest, release));
	roles.addAll(Arrays.asList(headmaster));
    }

    public void initUserAttributes() {
	snapeBlood = UserAttribute.builder().user(snape).attributeType(blood).attributeValue("Half Blood").build();
	tonksBlood = UserAttribute.builder().user(tonks).attributeType(blood).attributeValue("Half Blood").build();
	tonksPatronus = UserAttribute.builder().user(tonks).attributeType(patronus).attributeValue("Wolf").build();
	userAttributes.addAll(Arrays.asList(snapeBlood, tonksBlood, tonksPatronus));
    }

    public void initUserAttributeTypes() {
	occupation = UserAttributeType.builder().attributeName("Occupation").shortName("OCCUPATION")
		.dataType(DataType.STRING).isRequired(Boolean.TRUE).build();
	patronus = UserAttributeType.builder().attributeName("Patronus").shortName("PATRONUS").dataType(DataType.STRING)
		.isRequired(Boolean.FALSE).build();
	blood = UserAttributeType.builder().attributeName("Blood Status").shortName("BLOOD_STATUS")
		.dataType(DataType.STRING).isRequired(Boolean.TRUE).build();
	userAttributeTypes.addAll(Arrays.asList(occupation, patronus, blood));
    }

    public void initUsers() {
	dumbledore = User.builder().username("albus.dumbledore").fullName("Albus Dumbledore").build();
	dumbledore.setPassword("Expelliarmus");
	snape = User.builder().username("severus.snape").fullName("Severus Snape").build();
	snape.setPassword("Sectumsempra");
	tonks = User.builder().username("nymphadora.tonks").fullName("Nymphadora Tonks").build();
	tonks.setPassword("Stupify");
	umbridge = User.builder().username("dolores.umbridge").fullName("Dolores Jane Umbridge")
		.attributes(new ArrayList<>()).build();
	luna = User.builder().username("luna.lovegood").fullName("Luna Lovegood").attributes(new ArrayList<>()).build();
	fred = User.builder().username("fred.weasley").fullName("Fred Weasley").attributes(new ArrayList<>()).build();
	george = User.builder().username("george.weasley").fullName("George Weasley").attributes(new ArrayList<>())
		.build();
	lily = User.builder().username("lily.potter").fullName("Lilly Potter").build();
	for (User u : Arrays.asList(umbridge, luna, fred, george, lily)) {
	    u.setPassword("none");
	}

    }
}
