package com.santechture;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugPrice {
    @JsonProperty(
            value = "packageSize",
            required = true
    )
    private float packageSize;
    @JsonProperty(
            value = "code",
            required = true
    )
    private String code;
    @JsonProperty(
            value = "package_Price_to_Public",
            required = true
    )
    private Double package_Price_to_Public;
    @JsonProperty(
            value = "unit_Price_to_Public",
            required = true
    )
    private Double unit_Price_to_Public;
    @JsonProperty(
            value = "startDate",
            required = true
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date startDate;
    @JsonProperty(
            value = "endDate",
            required = true
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date endDate;
    @JsonProperty(
            value = "regulator",
            required = true
    )
    private Integer regulator;
    @JsonProperty(
            value = "dosage_Form",
            required = true
    )
    private String dosage_Form;
    public DrugPrice() {
    }
    public DrugPrice(String code, Double package_Price_to_Public, Float packageSize) {
        this.code = code;
        this.package_Price_to_Public = package_Price_to_Public;
        this.packageSize = packageSize;
    }
    public DrugPrice(String code, Double package_Price_to_Public, Double unit_Price_to_Public, Date startDate, Date endDate, Integer regulator, String dosage_Form) {
        this.code = code;
        this.package_Price_to_Public = package_Price_to_Public;
        this.unit_Price_to_Public = unit_Price_to_Public;
        this.startDate = startDate;
        this.endDate = endDate;
        this.regulator = regulator;
        this.dosage_Form = dosage_Form;
        this.packageSize = packageSize;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Double getPackage_Price_to_Public() {
        return package_Price_to_Public;
    }
    public void setPackage_Price_to_Public(Double package_Price_to_Public) {
        this.package_Price_to_Public = package_Price_to_Public;
    }
    public Double getUnit_Price_to_Public() {
        return unit_Price_to_Public;
    }
    public void setUnit_Price_to_Public(Double unit_Price_to_Public) {
        this.unit_Price_to_Public = unit_Price_to_Public;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Integer getRegulator() {
        return regulator;
    }
    public void setRegulator(Integer regulator) {
        this.regulator = regulator;
    }
    public String getDosage_Form() {
        return dosage_Form;
    }
    public void setPackageSize(float packageSize) {
        this.packageSize = packageSize;
    }
    public float getPackageSize() {
        return packageSize;
    }
    public void setDosage_Form(String dosage_Form) {
        this.dosage_Form = dosage_Form;
    }
}
