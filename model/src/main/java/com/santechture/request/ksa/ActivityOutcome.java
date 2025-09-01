package com.santechture.request.ksa;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.santechture.request.Activity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityOutcome implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull
    private Integer id;
    private String ruleName;
    private String ruleID;
    private String severity;
    private String longMsg;
    private String shortMsg;
    @JoinColumn(name = "activity", referencedColumnName = "ID")
    @ManyToOne
    @JsonIgnore
    private com.santechture.request.Activity activity;
    public ActivityOutcome() {
    }
    public ActivityOutcome(Integer id) {
        this.id = id;
    }
    public ActivityOutcome(String severity, String ruleName, String shortMsg, String longMsg) {
        if (shortMsg.equalsIgnoreCase(longMsg)) {
            longMsg = "E";
        }
        this.ruleName = ruleName;
        this.severity = severity;
        this.longMsg = longMsg;
        this.shortMsg = shortMsg;
        ruleID = "";
    }
    public String getRuleName() {
        return ruleName;
    }
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
    public String getRuleID() {
        return ruleID;
    }
    public void setRuleID(String ruleID) {
        this.ruleID = ruleID;
    }
    public String getSeverity() {
        return severity;
    }
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    public String getLongMsg() {
        return longMsg;
    }
    public void setLongMsg(String longMsg) {
        this.longMsg = longMsg;
    }
    public String getShortMsg() {
        return shortMsg;
    }
    public void setShortMsg(String shortMsg) {
        this.shortMsg = shortMsg;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public com.santechture.request.Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
