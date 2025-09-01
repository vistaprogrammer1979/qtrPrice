package com.santechture.request.ksa;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CodeGroup {
    private CodeType type;
    private Integer id;
    private String name;
    private String code;

    public CodeGroup() {
    }

    public CodeGroup(CodeType type, Integer id, String name, String code) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.code = code;
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
}
