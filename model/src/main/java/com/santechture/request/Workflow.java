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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Workflow implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Integer id;
    @JsonProperty(value ="CurrentNode", required = true)
    private String currentNode;
    @JsonProperty(value ="Action", required = true)
    private String action;
    @JsonProperty(value ="ActionThreshold", required = true)
    private String actionThreshold;
    @JsonProperty(value ="WorkflowNextNode", required = false)
    private String workflowNextNode;
    @JsonProperty(value ="conditionalWorkflowNextNode", required = false)
    private String conditionalWorkflowNextNode;
    @JsonProperty(value ="RoutingRule", required = false)
    private String routingRule;
    @JoinColumn(name = "headerID", referencedColumnName = "ID")
    @OneToOne
    @JsonIgnore
    private Header headerID;
    @OneToMany(mappedBy = "workflow", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Outcome", required = false)
    private List<WorkflowOutcome> outcome;
    public Workflow() {
    }
    public Workflow(Integer id) {
        this.id = id;
    }
    public String getCurrentNode() {
        return currentNode;
    }
    public void setCurrentNode(String currentNode) {
        this.currentNode = currentNode;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getActionThreshold() {
        return actionThreshold;
    }
    public void setActionThreshold(String actionThreshold) {
        this.actionThreshold = actionThreshold;
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
    public List<WorkflowOutcome> getOutcome() {
        return outcome;
    }
    public void setOutcome(List<WorkflowOutcome> outcome) {
        this.outcome = outcome;
    }
    public String getWorkflowNextNode() {
        return workflowNextNode;
    }
    public void setWorkflowNextNode(String workflowNextNode) {
        this.workflowNextNode = workflowNextNode;
    }
    public String getConditionalWorkflowNextNode() {
        return conditionalWorkflowNextNode;
    }
    public void setConditionalWorkflowNextNode(String conditionalWorkflowNextNode) {
        this.conditionalWorkflowNextNode = conditionalWorkflowNextNode;
    }
    public String getRoutingRule() {
        return routingRule;
    }
    public void setRoutingRule(String routingRule) {
        this.routingRule = routingRule;
    }
    public void addOutcome(String severity, String ruleName, String ShortMsg, String longMsg) {
        if (this.outcome != null) {
            this.outcome.add(new WorkflowOutcome(severity, ruleName, ShortMsg, longMsg));
        } else {
            this.outcome = new ArrayList<WorkflowOutcome>();
            this.outcome.add(new WorkflowOutcome(severity, ruleName, ShortMsg, longMsg));
        }
    }
}
