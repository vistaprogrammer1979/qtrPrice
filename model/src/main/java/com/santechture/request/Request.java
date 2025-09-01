package com.santechture.request;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import jakarta.persistence.OneToMany;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonRootName("Request")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Integer id;
    @JsonIgnore
    private Integer claimsCount;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date endDate;
    @JsonIgnore
    private Integer invalidClaimsCount;
    @JsonIgnore
    private Integer userId;
    @JsonIgnore
    private String request;
    @OneToOne(mappedBy = "requestID", cascade = CascadeType.PERSIST)
    @JsonProperty(value ="Header", required = true)
    private Header header;
    @OneToMany(mappedBy = "request", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER )
    @JsonProperty(value ="Claim", required = true)
    private List<Claim> claim;
    @OneToMany(mappedBy = "request", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Outcome", required = false)
    private List<RequestOutcome> outcome;
    public Request() {
    }
    public Request(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getClaimsCount() {
        return claimsCount;
    }
    public void setClaimsCount(Integer claimsCount) {
        this.claimsCount = claimsCount;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Integer getInvalidClaimsCount() {
        return invalidClaimsCount;
    }
    public void setInvalidClaimsCount(Integer invalidClaimsCount) {
        this.invalidClaimsCount = invalidClaimsCount;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getRequest() {
        return request;
    }
    public void setRequest(String request) {
        this.request = request;
    }
    public Header getHeader() {
        return header;
    }
    public void setHeader(Header header) {
        this.header = header;
    }
    public List<Claim> getClaim() {
        return claim;
    }
    public void setClaim(List<Claim> claim) {
        this.claim = claim;
    }
    public List<RequestOutcome> getOutcome() {
        return outcome;
    }
    public void setOutcome(List<RequestOutcome> outcome) {
        this.outcome = outcome;
    }
    public boolean Is20() {
        int total = 0;
        if (getOutcome() != null) {
            total+=getOutcome().size();
            if(total>=20){ return true;}
        }
        if (getHeader() != null) {
            if (getHeader().getOutcome() != null) {
                total+=getHeader().getOutcome().size();
                if(total>=20){ return true;}
            }
            if (getHeader().getWorkflow() != null) {
                if (getHeader().getWorkflow().getOutcome() != null) {
                    total+=getHeader().getWorkflow().getOutcome().size();
                    if(total>=20){ return true;}
                }
            }
            if (getHeader().getExtendedValidationType() != null) {
                for (com.santechture.request.ExtendedValidationType extendedValidationType : getHeader().getExtendedValidationType()) {
                    if (extendedValidationType.getOutcome() != null) {
                        total+=extendedValidationType.getOutcome().size();
                        if(total>=20){ return true;}
                    }
                }
            }
        }
        if (getClaim() != null) {
            for (com.santechture.request.Claim claim : getClaim()) {
                if (claim.getOutcome() != null) {
                    total+=claim.getOutcome().size();
                    if(total>=20){ return true;}
                }
                if (claim.getEncounter() != null) {
                    for (com.santechture.request.Encounter encounter : claim.getEncounter()) {
                        if (encounter.getOutcome() != null) {
                            total+=encounter.getOutcome().size();
                            if(total>=20){ return true;}
                        }
                        if (encounter.getAuthorisation() != null) {
                            if (encounter.getAuthorisation().getOutcome() != null) {
                                total+=encounter.getAuthorisation().getOutcome().size();
                                if(total>=20){ return true;}
                            }
                        }
                    }
                }
                if (claim.getDiagnosis() != null) {
                    for (com.santechture.request.Diagnosis diagnosis : claim.getDiagnosis()) {
                        if (diagnosis.getOutcome() != null) {
                            total+=diagnosis.getOutcome().size();
                            if(total>=20){ return true;}
                        }
                    }
                }
                if (claim.getActivity() != null) {
                    for (com.santechture.request.Activity activity : claim.getActivity()) {
                        if (activity.getOutcome() != null) {
                            total+=activity.getOutcome().size();
                            if(total>=20){ return true;}
                        }
                        if (activity.getObservation() != null) {
                            for (com.santechture.request.Observation observation : activity.getObservation()) {
                                if (observation.getOutcome() != null) {
                                    total+=observation.getOutcome().size();
                                    if(total>=20){ return true;}
                                }
                            }
                        }
                    }
                }
                if (claim.getResubmission() != null) {
                    if (claim.getResubmission().getOutcome() != null) {
                        total+=claim.getResubmission().getOutcome().size();
                        if(total>=20){ return true;}
                    }
                }
                if (claim.getContract() != null) {
                    if (claim.getContract().getOutcome() != null) {
                        total+=claim.getContract().getOutcome().size();
                        if(total>=20){ return true;}
                    }
                }
                if (claim.getPatient() != null) {
                    if (claim.getPatient().getOutcome() != null) {
                        total+=claim.getPatient().getOutcome().size();
                        if(total>=20){ return true;}
                    }
                    if (claim.getPatient().getPatientInsurance() != null) {
                        if (claim.getPatient().getPatientInsurance().getOutcome() != null) {
                            total+=claim.getPatient().getPatientInsurance().getOutcome().size();
                            if(total>=20){ return true;}
                        }
                    }
                }
            }
        }
        return total>=20;
    }
    public void fixRequest()
    {
        if (getClaim() != null) {
            for (com.santechture.request.Claim claim : getClaim()) {
                if(claim.getActivity() == null){
                    claim.setActivity(new ArrayList<Activity>());
                }
            }
        }
    }
    public boolean removeAllOutcomes() {
        if (getOutcome() != null) {
            setOutcome(null);
        }
        if (getHeader() != null) {
            if (getHeader().getOutcome() != null) {
                getHeader().setOutcome(null);
            }
            if (getHeader().getWorkflow() != null) {
                if (getHeader().getWorkflow().getOutcome() != null) {
                    getHeader().getWorkflow().setOutcome(null);
                }
            }
            if (getHeader().getExtendedValidationType() != null) {
                for (com.santechture.request.ExtendedValidationType extendedValidationType : getHeader().getExtendedValidationType()) {
                    if (extendedValidationType.getOutcome() != null) {
                        extendedValidationType.setOutcome(null);
                    }
                }
            }
        }
        if (getClaim() != null) {
            for (com.santechture.request.Claim claim : getClaim()) {
                if (claim.getOutcome() != null) {
                    claim.setOutcome(null);
                }
                if (claim.getEncounter() != null) {
                    for (com.santechture.request.Encounter encounter : claim.getEncounter()) {
                        if (encounter.getOutcome() != null) {
                            encounter.setOutcome(null);
                        }
                        if (encounter.getAuthorisation() != null) {
                            if (encounter.getAuthorisation().getOutcome() != null) {
                                encounter.getAuthorisation().setOutcome(null);
                            }
                        }
                    }
                }
                if (claim.getDiagnosis() != null) {
                    for (com.santechture.request.Diagnosis diagnosis : claim.getDiagnosis()) {
                        if (diagnosis.getOutcome() != null) {
                            diagnosis.setOutcome(null);
                        }
                    }
                }
                if (claim.getActivity() != null) {
                    for (com.santechture.request.Activity activity : claim.getActivity()) {
                        if (activity.getOutcome() != null) {
                            activity.setOutcome(null);
                        }
                        if (activity.getObservation() != null) {
                            for (com.santechture.request.Observation observation : activity.getObservation()) {
                                if (observation.getOutcome() != null) {
                                    observation.setOutcome(null);
                                }
                            }
                        }
                    }
                }
                if (claim.getResubmission() != null) {
                    if (claim.getResubmission().getOutcome() != null) {
                        claim.getResubmission().setOutcome(null);
                    }
                }
                if (claim.getContract() != null) {
                    if (claim.getContract().getOutcome() != null) {
                        claim.getContract().setOutcome(null);
                    }
                }
                if (claim.getPatient() != null) {
                    if (claim.getPatient().getOutcome() != null) {
                        claim.getPatient().setOutcome(null);
                    }
                    if (claim.getPatient().getPatientInsurance() != null) {
                        if (claim.getPatient().getPatientInsurance().getOutcome() != null) {
                            claim.getPatient().getPatientInsurance().setOutcome(null);
                        }
                    }
                }
            }
        }
        return true;
    }
    public boolean containsIgnoreCase(Set<String> l, String s) {
        Iterator<String> it = l.iterator();
        while (it.hasNext()) {
            if (it.next().equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }
    public void addOutcome(String severity, String ruleName, String ShortMsg, String longMsg) {
        if (this.outcome != null) {
            this.outcome.add(new RequestOutcome(severity, ruleName, ShortMsg, longMsg));
        } else {
            this.outcome = new ArrayList<RequestOutcome>();
            this.outcome.add(new RequestOutcome(severity, ruleName, ShortMsg, longMsg));
        }
    }
    public Boolean onlyTop20() {
        if (getHeader().getTop20() == null || getHeader().getTop20() == 0) {
            return false;
        }
        if (getClaim() == null) {
            return false;
        }
        if (getClaim().size() > 1) {
            return false;
        }
        return true;
    }
}
