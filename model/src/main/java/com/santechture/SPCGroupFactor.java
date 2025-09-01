package com.santechture;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SPCGroupFactor {
    @JsonProperty(
            value = "ID",
            required = true
    )
    private Long ID;
    @JsonProperty(
            value = "priceListId",
            required = true
    )
    private Long priceListId;
    @JsonProperty(
            value = "groupID",
            required = true
    )
    private Long groupID;
    @JsonProperty(
            value = "factor",
            required = true
    )
    private Double factor;
    @JsonProperty(
            value = "startDate",
            required = true
    )
    private Date startDate;
    @JsonProperty(
            value = "endDate",
            required = true
    )
    private Date endDate;
    public SPCGroupFactor() {
    }
    public SPCGroupFactor(Long ID, Long priceListId, Long groupID, Double factor, Date startDate, Date endDate) {
        this.ID = ID;
        this.priceListId = priceListId;
        this.groupID = groupID;
        this.factor = factor;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Long getID() {
        return ID;
    }
    public void setID(Long ID) {
        this.ID = ID;
    }
    public Long getPriceListId() {
        return priceListId;
    }
    public void setPriceListId(Long priceListId) {
        this.priceListId = priceListId;
    }
    public Long getGroupID() {
        return groupID;
    }
    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }
    public Double getFactor() {
        return factor;
    }
    public void setFactor(Double factor) {
        this.factor = factor;
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
}
