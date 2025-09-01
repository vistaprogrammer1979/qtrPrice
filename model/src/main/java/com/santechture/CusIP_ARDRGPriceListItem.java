package com.santechture;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CusIP_ARDRGPriceListItem {
    @JsonProperty(value = "id", required = true)
    private Long id;
    @JsonProperty(value = "pricListId", required = true)
    private Long pricListId;
    @JsonProperty(value = "code", required = true)
    private String code;
    @JsonProperty(value = "lengthOfStayRoles")
    private String lengthOfStayRoles;
    @JsonProperty(value = "shortStayLessThan")
    private Integer shortStayLessThan;
    @JsonProperty(value = "longStayMoreThan")
    private Integer longStayMoreThan;
    @JsonProperty(value = "thresholdStay")
    private Integer thresholdStay;
    @JsonProperty(value = "normalStayPrice")
    private Double normalStayPrice;
    @JsonProperty(value = "ssoBasePrice")
    private Double ssoBasePrice;
    @JsonProperty(value = "ssoPerDiemPrice")
    private Double ssoPerDiemPrice;
    @JsonProperty(value = "sameDayPrice")
    private Double sameDayPrice;
    @JsonProperty(value = "shortStayPrice")
    private Double shortStayPrice;
    @JsonProperty(value = "longStayPrice")
    private Double longStayPrice;
    @JsonProperty(value = "longStayThresholdPrice")
    private Double longStayThresholdPrice;
    @JsonProperty(value = "deleted")
    private Boolean deleted;
}
