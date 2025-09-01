package com.santechture;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CusPriceList {
    private Long id;
    private Date startDate;
    private Date endDate;
    private Boolean approved;
    private Boolean deleted;

    private String name;
    private Integer facilityId;
    private Integer facilityGroupId;
    private Integer regulatorId;
    private String priceListType;
    private List<CusPriceListItem> items;
}

