package com.santechture;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.santechture.request.CodeType;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DHA_DRG_HighCost {
    private String code;
    private String description;
    private CodeType type;
    public DHA_DRG_HighCost() {
    }
    public DHA_DRG_HighCost(String code, CodeType type,String description) {
        this.code = code;
        this.type = type;
        this.description=description;
    }
    public DHA_DRG_HighCost(String code, CodeType type ) {
        this.code = code;
        this.type = type;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public CodeType getType() {
        return type;
    }
    public void setType(CodeType type) {
        this.type = type;
    }
}
