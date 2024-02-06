package hapi.assignment.fhironjava.fhironjavaassignment.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;

public class BaseFHIRController {
    protected final IGenericClient fhirClient;
    protected final IParser parser;

    public BaseFHIRController() {
        var ctx = FhirContext.forR5();
        ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        
        fhirClient = ctx.newRestfulGenericClient("http://hapi.fhir.org/baseR5");
        parser = ctx.newJsonParser().setPrettyPrint(true);
    }
}