package com.santechture;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.santechture.converter.CodeTypeDeserializer;
import com.santechture.converter.CodeTypeSerializer;
import com.santechture.request.CodeType;

import java.util.Date;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MasterPriceListItem {
    @JsonProperty(
            value = "id",
            required = true
    )
    private long id;
    @JsonProperty(
            value = "masterListId",
            required = true
    )
    private long masterListId;
    @JsonProperty(
            value = "code",
            required = true
    )

    private String code;
    @JsonProperty(
            value = "type",
            required = true
    )
    private CodeType type;
    @JsonProperty(
            value = "price",
            required = true
    )
    private Double price;
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
            value = "anaesthesiaBaseUnits",
            required = true
    )

    private Double anaesthesiaBaseUnits;
    public MasterPriceListItem() {
    }
    public MasterPriceListItem(Long id, long masterListId, String code, CodeType type, Double price,
                               Date startDate, Date endDate, Double anaesthesiaBaseUnits) {
        this.id = id;
        this.masterListId = masterListId;
        this.code = code;
        this.type = type;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.anaesthesiaBaseUnits = anaesthesiaBaseUnits;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getMasterListId() {
        return masterListId;
    }
    public void setMasterListId(long masterListId) {
        this.masterListId = masterListId;
    }
    public long getMasterList() {
        return masterListId;
    }
    public void setMasterList(long masterListId) {
        this.masterListId = masterListId;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public CodeType getType() {
        return type;
    }
    public void setType(CodeType type) {
        this.type = type;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
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
    public Double getAnaesthesiaBaseUnits() {
        return anaesthesiaBaseUnits;
    }
    public void setAnaesthesiaBaseUnits(Double anaesthesiaBaseUnits) {
        this.anaesthesiaBaseUnits = anaesthesiaBaseUnits;
    }
}
