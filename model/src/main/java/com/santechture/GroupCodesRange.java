package com.santechture;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupCodesRange {
    private String codeFrom;
    private String codeTo;
    public GroupCodesRange(String codeFrom, String codeTo) {
        this.codeFrom = codeFrom;
        this.codeTo = codeTo;
    }
    public String getCodeFrom() {
        return codeFrom;
    }
    public void setCodeFrom(String codeFrom) {
        this.codeFrom = codeFrom;
    }
    public String getCodeTo() {
        return codeTo;
    }
    public void setCodeTo(String codeTo) {
        this.codeTo = codeTo;
    }
    public boolean containsCode(String sCode) {
        if (sCode.equalsIgnoreCase(codeFrom)) {
            return true;
        }
        if (codeTo != null && codeTo.length() > 0) {
            if (sCode.compareToIgnoreCase(codeFrom) >= 0 && sCode.compareToIgnoreCase(codeTo) <= 0) {
                return true;
            }
        }
        return false;
    }
}
