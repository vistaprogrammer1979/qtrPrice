package com.santechture.request;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtendedValidationType implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Integer id;
    @JsonProperty(value ="Type", required = true)
    private String type;
    @JoinColumn(name = "headerID", referencedColumnName = "ID")
    @ManyToOne
    @JsonIgnore
    private Header headerID;
    @OneToMany(mappedBy = "extendedValidationType", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Outcome", required = false)
    private List<ExtendedValidationTypeOutcome> outcome;
    public ExtendedValidationType() {
    }
    public ExtendedValidationType(String type) {
        this.type = type;
    }
    public ExtendedValidationType(Integer id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Header getHeaderID() {
        return headerID;
    }
    public void setHeaderID(Header headerID) {
        this.headerID = headerID;
    }
    public List<ExtendedValidationTypeOutcome> getOutcome() {
        return outcome;
    }
    public void setOutcome(List<ExtendedValidationTypeOutcome> outcome) {
        this.outcome = outcome;
    }
    public void addOutcome(String severity, String ruleName, String ShortMsg, String longMsg) {
        if (this.outcome != null) {
            this.outcome.add(new ExtendedValidationTypeOutcome(severity, ruleName, ShortMsg, longMsg));
        } else {
            this.outcome = new ArrayList<ExtendedValidationTypeOutcome>();
            this.outcome.add(new ExtendedValidationTypeOutcome(severity, ruleName, ShortMsg, longMsg));
        }
    }
}
