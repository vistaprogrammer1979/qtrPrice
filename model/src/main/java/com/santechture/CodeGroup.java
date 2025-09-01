package com.santechture;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.santechture.request.CodeType;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CodeGroup {
    private CodeType type;
    private Integer id;
    private String name;
    private String code;
    @JsonProperty("isParent")
    private Boolean isParent;

    public CodeGroup() {
    }

    public CodeGroup(CodeType type, Integer id, String name, String code) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.code = code;
        this.isParent = false;
    }
    public CodeType getType() {
        return type;
    }
    public void setType(CodeType type) {
        this.type = type;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Boolean getIsParent() {
        return isParent;
    }
    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }
}
