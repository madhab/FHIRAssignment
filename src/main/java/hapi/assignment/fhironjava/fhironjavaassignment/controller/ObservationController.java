package hapi.assignment.fhironjava.fhironjavaassignment.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.Enumerations.ObservationStatus;
import org.hl7.fhir.r5.model.Enumerations.QuantityComparator;
import org.hl7.fhir.r5.model.Identifier;
import org.hl7.fhir.r5.model.Identifier.IdentifierUse;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Observation.ObservationReferenceRangeComponent;
import org.hl7.fhir.r5.model.Quantity;
import org.hl7.fhir.r5.model.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.uhn.fhir.rest.api.MethodOutcome;

@RestController
public class ObservationController extends BaseFHIRController {
        @GetMapping("/Observation/{observationId}")
        private String getPatientById(@PathVariable("observationId") int observationId) {
                var observation = fhirClient.read().resource(Observation.class).withId(String.valueOf(observationId)).execute();
                return parser.encodeResourceToString(observation);
        }

        @PostMapping("/Observation/{patientId}")
        private int createObservation(@PathVariable("patientId") int patientId) {
        	
        	TimeZone zone = TimeZone.getTimeZone("UTC");
        	zone.setRawOffset(60 * 60 * 1000);
        	TimeZone.setDefault(zone);
        	
        	Observation observation = new Observation();
        	
        	List<Identifier> theIdentifier = new ArrayList<Identifier>();
        	Identifier identifier = new Identifier();
        	identifier.setUse(IdentifierUse.OFFICIAL);
        	Coding identifierCoding = new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "4.0.0", "RI", "Resource identifier");
        	CodeableConcept identifierCodeableConcept = new CodeableConcept(identifierCoding);
        	identifierCodeableConcept.setText("Resource identifier");
        	identifierCodeableConcept.setId("6323");
        	identifier.setType(identifierCodeableConcept);
        	identifier.setId("6323");
        	theIdentifier.add(identifier);
        	observation.setIdentifier(theIdentifier);
        	
        	observation.setStatus(ObservationStatus.FINAL);
        	
        	Coding codeCoding = new Coding("https://loinc.org/", "15074-8", "Glucose [Moles/volume] in Blood");
        	CodeableConcept codeCodeableConcept = new CodeableConcept(codeCoding);
        	codeCodeableConcept.setText("Glucose [Moles/volume] in Blood");
        	observation.setCode(codeCodeableConcept);
        	
        	String patientReference = "Patient/270385";
        	Reference subjectReference = new Reference(patientReference);
        	observation.setSubject(subjectReference);
        	
        	Calendar effectiveFromCal = Calendar.getInstance();
        	effectiveFromCal.set(Calendar.YEAR, 2013);
        	effectiveFromCal.set(Calendar.MONTH, 03);
        	effectiveFromCal.set(Calendar.DATE, 02);
        	effectiveFromCal.set(Calendar.HOUR, 9);
        	effectiveFromCal.set(Calendar.MINUTE, 30);
        	effectiveFromCal.set(Calendar.SECOND, 10);
        	effectiveFromCal.set(Calendar.MILLISECOND, 0);
        	DateTimeType effectiveFrom = new DateTimeType(effectiveFromCal.getTime());
        	observation.setEffective(effectiveFrom);
        	
        	Calendar issuedCal = Calendar.getInstance();
        	issuedCal.set(Calendar.YEAR, 2013);
        	issuedCal.set(Calendar.MONTH, 03);
        	issuedCal.set(Calendar.DATE, 04);
        	issuedCal.set(Calendar.HOUR, 1);
        	issuedCal.set(Calendar.MINUTE, 30);
        	issuedCal.set(Calendar.SECOND, 10);
        	issuedCal.set(Calendar.MILLISECOND, 0);
        	observation.setIssued(issuedCal.getTime());
        	
        	List<Reference> thePerformer = new ArrayList<Reference>();
        	String performerReference = "Practitioner/853";
        	Reference practitionerReference = new Reference(performerReference);
        	thePerformer.add(practitionerReference);
        	observation.setPerformer(thePerformer);
        	
        	Quantity quantity = new Quantity();
        	quantity.setValue(6.3);
        	quantity.setUnit("mmol/l");
        	quantity.setComparator(QuantityComparator.AD);
        	quantity.setSystem("http://unitsofmeasure.org");
        	quantity.setCode("/l");
        	observation.setValue(quantity);
        	
        	List<CodeableConcept> theInterpretation = new ArrayList<CodeableConcept>();
        	CodeableConcept interpretationCodeableConcept = new CodeableConcept(new Coding("http://hl7.org/fhir/ValueSet/observation-interpretation", "H", "High")); 
        	interpretationCodeableConcept.setText("High");
        	theInterpretation.add(interpretationCodeableConcept);
        	observation.setInterpretation(theInterpretation);
        	
        	List<ObservationReferenceRangeComponent> theReferenceRange = new ArrayList<ObservationReferenceRangeComponent>();
        	ObservationReferenceRangeComponent observationReferenceRangeComponent = new ObservationReferenceRangeComponent();
        	Quantity quantityLow = new Quantity();
        	quantityLow.setValue(3.1);
        	quantityLow.setUnit("mmol/l");
        	quantityLow.setComparator(QuantityComparator.GREATER_OR_EQUAL);
        	quantityLow.setSystem("http://unitsofmeasure.org");
        	quantityLow.setCode("/l");
        	observationReferenceRangeComponent.setLow(quantityLow);
        	Quantity quantityHigh = new Quantity();
        	quantityHigh.setValue(6.2);
        	quantityHigh.setUnit("mmol/l");
        	quantityHigh.setComparator(QuantityComparator.LESS_OR_EQUAL);
        	quantityHigh.setSystem("http://unitsofmeasure.org");
        	quantityHigh.setCode("/l");
        	observationReferenceRangeComponent.setHigh(quantityHigh);
        	theReferenceRange.add(observationReferenceRangeComponent);
        	observation.setReferenceRange(theReferenceRange);
        	
        	System.out.println(parser.encodeResourceToString(observation));
        	
        	MethodOutcome outcome = fhirClient.create().resource(observation).execute();
        	return Integer.parseInt(outcome.getId().getIdPart());
            
        }
}