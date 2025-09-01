/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.santechture;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 * @author wfakhra
 */
@NoArgsConstructor

public class RCMFacilityCodeMapping {

    private String activityCode;
  //  private int type;
    private Double price;
    private String  providerActivityCode;
    private Integer type;
    private String   facilityLisence;

    public RCMFacilityCodeMapping(String activityCode, Double price, String providerActivityCode, Integer type, String facilityLisence) {
        this.activityCode = activityCode;
        this.price = price;
        this.providerActivityCode = providerActivityCode;
        this.type = type;
        this.facilityLisence = facilityLisence;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProviderActivityCode() {
        return providerActivityCode;
    }

    public void setProviderActivityCode(String providerActivityCode) {
        this.providerActivityCode = providerActivityCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFacilityLisence() {
        return facilityLisence;
    }

    public void setFacilityLisence(String facilityLisence) {
        this.facilityLisence = facilityLisence;
    }

   }
