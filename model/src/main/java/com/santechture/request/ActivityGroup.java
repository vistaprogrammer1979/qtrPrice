package com.santechture.request;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty(value ="ID")
    private Integer id;
    @JsonProperty(value ="Type", required = true)
    private Integer type;
    @JsonProperty(value ="Name")
    private String name;

    @JsonProperty(value ="principalDiagnosisNotCovered")
    private Boolean principalDiagnosisNotCovered;
    @JoinColumn(name = "activityID", referencedColumnName = "ID")
    @ManyToOne
    @JsonIgnore
    private Activity activityID;
    public ActivityGroup() {
    }
    public ActivityGroup(Integer id) {
        this.id = id;
    }
    public ActivityGroup(Integer id, Integer type, String name, Activity activityID) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.activityID = activityID;
    }
    public Integer getType() {
        return type;
    }

    public Boolean getPrincipalDiagnosisNotCovered() {
        return principalDiagnosisNotCovered;
    }

    public void setPrincipalDiagnosisNotCovered(Boolean principalDiagnosisNotCovered) {
        this.principalDiagnosisNotCovered = principalDiagnosisNotCovered;
    }
    public void setType(Integer type) {
        this.type = type;
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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
