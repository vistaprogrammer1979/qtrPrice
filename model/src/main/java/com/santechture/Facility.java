package com.santechture;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Facility implements Serializable{
    private String license;
    private FacilityType type;
    private Regulator regulator;
    private Boolean active;
    public Facility() {
    }
    public Facility(String license) {
        this.license = license;
    }
    public Facility(String license, FacilityType type, Regulator regulator, Boolean active) {
        this.license = license;
        this.type = type;
        this.regulator = regulator;
        this.active = active;
    }
    public String getLicense() {
        return license;
    }
    public void setLicense(String license) {
        this.license = license;
    }
    public FacilityType getType() {
        return type;
    }
    public void setType(FacilityType type) {
        this.type = type;
    }
    public Regulator getRegulator() {
        return regulator;
    }
    public void setRegulator(Regulator regulator) {
        this.regulator = regulator;
    }
    public Boolean isActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
}
