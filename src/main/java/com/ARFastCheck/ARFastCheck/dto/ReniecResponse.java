package com.ARFastCheck.ARFastCheck.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReniecResponse {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("first_last_name")
    private String firstLastName;

    @JsonProperty("second_last_name")
    private String secondLastName;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("document_number")
    private String documentNumber;

    // getters y setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String v) { this.firstName = v; }

    public String getFirstLastName() { return firstLastName; }
    public void setFirstLastName(String v) { this.firstLastName = v; }

    public String getSecondLastName() { return secondLastName; }
    public void setSecondLastName(String v) { this.secondLastName = v; }

    public String getFullName() { return fullName; }
    public void setFullName(String v) { this.fullName = v; }

    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String v) { this.documentNumber = v; }
}
