package com.santechture.request.ksa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SPCCodeFactor {
    @JsonProperty(
            value = "ID",
            required = true
    )
    private Long ID;
    @JsonProperty(
            value = "priceListId",
            required = true
    )
    private Integer priceListId;
    @JsonProperty(
            value = "type",
            required = true
    )
    private CodeType type;
    @JsonProperty(
            value = "code",
            required = true
    )
    private String code;
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
    public SPCCodeFactor() {
    }
    public SPCCodeFactor(Long ID, Integer priceListId, CodeType type, String code, Double factor, Date startDate, Date endDate) {
        this.ID = ID;
        this.priceListId = priceListId;
        this.type = type;
        this.code = code;
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
    public Integer getPriceListId() {
        return priceListId;
    }
    public void setPriceListId(Integer priceListId) {
        this.priceListId = priceListId;
    }
    public CodeType getType() {
        return type;
    }
    public void setType(CodeType type) {
        this.type = type;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
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
