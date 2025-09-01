package com.santechture;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedicinesPriceListItem {
    @JsonProperty(value = "id", required = true)
    private Long id;
    private String tradeCode;
    private String tradeName;
    private String ingredientStrength;
    private String dosageForm;
    private Double packagePriceToPublic;
    private String manufacturer;
    private String registeredOwner;
    private Double unitPrice;
    private Date expiryDate;
    private Date effectiveDate;
    private Long granularUnit;
    private String source;
    private String scientificCode;
    private String scientificName;
    private String routeOfAdmin;
    private String status;
    private String createdBy;
    private Date creationDate;
    private Boolean isDeleted;
    private java.sql.Timestamp modifyDate;
    private String modifyBy;

}
