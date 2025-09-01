package com.santechture;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.santechture.request.CodeType;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DHA_DRG_COST_PER_ACTIVITY {
    private String code;
    private int BaseUnitsAnesthesia;
    private Double  cost;
    private CodeType type;
    public DHA_DRG_COST_PER_ACTIVITY() {
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public int getBaseUnitsAnesthesia() {
        return BaseUnitsAnesthesia;
    }
    public void setBaseUnitsAnesthesia(int BaseUnitsAnesthesia) {
        this.BaseUnitsAnesthesia = BaseUnitsAnesthesia;
    }
    public Double getCost() {
        return cost;
    }
    public void setCost(Double cost) {
        this.cost = cost;
    }
    public CodeType getType() {
        return type;
    }
    public void setType(CodeType type) {
        this.type = type;
    }
    public DHA_DRG_COST_PER_ACTIVITY(String code,CodeType type, int BaseUnitsAnesthesia, Double cost  ) {
        this.code = code;
        this.BaseUnitsAnesthesia = BaseUnitsAnesthesia;
        this.cost = cost;
        this.type = type;
    }
}
