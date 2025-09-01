package com.santechture;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.santechture.request.CodeType;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DRGExcludedCpts {
    private String code;
    private CodeType type;
    public DRGExcludedCpts() {
    }
    public DRGExcludedCpts(String code, CodeType type) {
        this.code = code;
        this.type = type;
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
}
