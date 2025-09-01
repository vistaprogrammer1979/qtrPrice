package com.santechture;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObseleteDiagnosis {
    @JsonProperty(value ="Type", required = true)
    protected String type;
    @JsonProperty(value ="Code", required = true)
    protected String code;
    public String getType() {
        return type;
    }
    public void setType(String value) {
        this.type = value;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String value) {
        this.code = value;
    }
}
