package com.santechture;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MasterPriceList {

    @JsonProperty(
            value = "id",
            required = true
    )

    private Long id;
    @JsonProperty(
            value = "name",
            required = true
    )

    private String name;
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
    public MasterPriceList() {
    }
    public MasterPriceList(Long id, String name, Date startDate, Date endDate, Integer regulator) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.regulator = regulator;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public void setRegulatorID(Integer regulator) {
        this.regulator = regulator;
    }
}
