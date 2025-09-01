package com.santechture;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Clinician {
    @JsonProperty(
            value = "license",
            required = true
    )
    private String license;
    @JsonProperty(
            value = "facility_license",
            required = true
    )
    private String facility_license;
    @JsonProperty(
            value = "validFrom",
            required = true
    )
    private Date validFrom;
    @JsonProperty(
            value = "validTo",
            required = true
    )
    private Date validTo;
    @JsonProperty(
            value = "status",
            required = true
    )
    public String status;
    @JsonProperty(
            value = "profession",
            required = true
    )
    public String profession;
    @JsonProperty(
            value = "category",
            required = true
    )
    public String category;
    @JsonProperty(
            value = "facility_type",
            required = true
    )
    public String facility_type;
    @JsonProperty(
            value = "regulator",
            required = true
    )
    public Integer regulator;
    public Clinician() {
    }
    public Clinician(String license, String facility_license, Date validFrom, Date validTo, String status, String profession, String category, String facility_type, Integer regulator) {
        this.license = license;
        this.facility_license = facility_license;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.status = status;
        this.profession = profession;
        this.category = category;
        this.facility_type = facility_type;
        this.regulator = regulator;
    }
    public String getFacility_license() {
        return facility_license;
    }
    public void setFacility_license(String facility_license) {
        this.facility_license = facility_license;
    }
    public String getLicense() {
        return license;
    }
    public void setLicense(String license) {
        this.license = license;
    }
    public Date getValidFrom() {
        return validFrom;
    }
    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }
    public Date getValidTo() {
        return validTo;
    }
    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getProfession() {
        return profession;
    }
    public void setProfession(String profession) {
        this.profession = profession;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getFacility_type() {
        return facility_type;
    }
    public void setFacility_type(String facility_type) {
        this.facility_type = facility_type;
    }
    public Integer getRegulator() {
        return regulator;
    }
    public void setRegulator(Integer regulator) {
        this.regulator = regulator;
    }
}
