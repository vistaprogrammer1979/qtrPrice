package com.santechture.request;
import com.fasterxml.jackson.annotation.*;
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
public class Observation implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Integer id;
    @JsonProperty(value ="idCaller", required = false)
    private Integer idCaller;
    @JsonProperty(value ="Type", required = true)
    private String type;
    @JsonProperty(value ="Code", required = true)
    private String code;
    @JsonProperty(value ="Value")
    private String value;
    @JsonProperty(value ="ValueType")
    private String valueType;
    @JoinColumn(name = "activityID", referencedColumnName = "ID")
    @ManyToOne
    @JsonIgnore
    private Activity activityID;
    @OneToMany(mappedBy = "observation", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Outcome", required = false)
    private List<ObservationOutcome> outcome;
    public Observation() {
    }
    public Observation(Integer id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getValueType() {
        return valueType;
    }
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Activity getActivityID() {
        return activityID;
    }
    public void setActivityID(Activity activityID) {
        this.activityID = activityID;
    }
    public List<ObservationOutcome> getOutcome() {
        return outcome;
    }
    public void setOutcome(List<ObservationOutcome> outcome) {
        this.outcome = outcome;
    }
    public void addOutcome(String severity, String ruleName, String ShortMsg, String longMsg) {
        if (this.outcome != null) {
            this.outcome.add(new ObservationOutcome(severity, ruleName, ShortMsg, longMsg));
        } else {
            this.outcome = new ArrayList<ObservationOutcome>();
            this.outcome.add(new ObservationOutcome(severity, ruleName, ShortMsg, longMsg));
        }
    }
    public Integer getIdCaller() {
        return idCaller;
    }
    public void setIdCaller(Integer idCaller) {
        this.idCaller = idCaller;
    }
}
