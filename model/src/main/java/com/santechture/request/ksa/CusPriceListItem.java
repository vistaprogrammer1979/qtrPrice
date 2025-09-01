package com.santechture.request.ksa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.santechture.request.Status;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CusPriceListItem {
    @JsonProperty(value = "id", required = true)
    private Long id;
    @JsonProperty(value = "pricListId", required = true)
    private Long pricListId;
    @JsonProperty(value = "code", required = true)
    private String code;
    @JsonProperty(value = "type", required = true)
    private CodeType type;
    @JsonProperty(value = "price", required = true)
    private Double price;
    @JsonProperty(value = "discount", required = true)
    private Double discount;
    @JsonProperty(value = "startDate", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date startDate;
    @JsonProperty(value = "endDate", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date endDate;
    private boolean needApproval;
    private Double noneSaudiVat;
    private boolean covered;
    private Double saudiVat;
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getSaudiVat() {
        return saudiVat;
    }

    public void setSaudiVat(Double saudiVat) {
        this.saudiVat = saudiVat;
    }

    public CusPriceListItem() {
    }

    public CusPriceListItem(Long id, Long pricListId, String code, CodeType type, Double price, Double discount, Date startDate, Date endDate) {
        this.id = id;
        this.pricListId = pricListId;
        this.code = code;
        this.type = type;
        this.price = price;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Double getNoneSaudiVat() {
        return noneSaudiVat;
    }

    public void setNoneSaudiVat(Double noneSaudiVat) {
        this.noneSaudiVat = noneSaudiVat;
    }

    public boolean isNeedApproval() {
        return needApproval;
    }

    public void setNeedApproval(boolean needApproval) {
        this.needApproval = needApproval;
    }

    public boolean isCovered() {
        return covered;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Long getPricListId() {
        return pricListId;
    }

    public void setPricListId(Long pricListId) {
        this.pricListId = pricListId;
    }
}
