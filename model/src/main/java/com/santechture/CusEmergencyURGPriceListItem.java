package com.santechture;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CusEmergencyURGPriceListItem {
    @JsonProperty(value = "id", required = true)
    private Long id;
    @JsonProperty(value = "pricListId", required = true)
    private Long pricListId;
    @JsonProperty(value = "code", required = true)
    private String code;
    @JsonProperty(value = "price", required = true)
    private Double price;


}
