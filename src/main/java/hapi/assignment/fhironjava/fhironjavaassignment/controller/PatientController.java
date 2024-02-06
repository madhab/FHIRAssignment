package hapi.assignment.fhironjava.fhironjavaassignment.controller;

import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.ContactPoint;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Identifier;
import org.hl7.fhir.r5.model.Patient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.javafaker.Faker;

import ca.uhn.fhir.rest.api.MethodOutcome;

@RestController
public class PatientController extends BaseFHIRController {
        private final Faker faker = new Faker();

        @GetMapping("/Patient/{patientId}")
        private String getPatientById(@PathVariable("patientId") int patientId) {
                var patient = fhirClient
                                .read()
                                .resource(Patient.class)
                                .withId(String.valueOf(patientId))
                                .execute();

                return parser.encodeResourceToString(patient);
        }

        @PostMapping("/Patient")
        private int createPatient() {
                Patient patient = new Patient();

                Identifier identifier = patient.addIdentifier();
                identifier.setSystem("http://example.com/fhir/patient-identifier");
                identifier.setValue(faker.random().hex(8));

                patient.addName()
                                .setFamily(faker.name().lastName()).addGiven(faker.name().firstName());
                patient.setGender(faker.bool().bool() ? Enumerations.AdministrativeGender.MALE
                                : Enumerations.AdministrativeGender.FEMALE);
                patient.setBirthDate(faker.date().birthday());
                patient.addAddress()
                                .setText(faker.address().fullAddress());
                patient.addContact()
                                .addTelecom(new ContactPoint().setSystem(ContactPoint.ContactPointSystem.PHONE)
                                                .setValue(faker.phoneNumber().cellPhone()))
                                .addRelationship()
                                .setText("Emergency Contact")
                                .setText(faker.name().fullName());
                patient.addCommunication()
                                .setLanguage(new CodeableConcept().setText(faker.lorem().word()));

                MethodOutcome outcome = fhirClient.create().resource(patient).execute();

                return Integer.parseInt(outcome.getId().getIdPart());
        }
}