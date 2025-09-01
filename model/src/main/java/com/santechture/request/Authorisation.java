package com.santechture.request;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.santechture.converter.DateDeserializer;
import com.santechture.converter.DateSerializer;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Authorisation implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value ="Start", required = true)
//    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date start;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value ="End", required = true)
//    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date end;
    @JsonProperty(value ="Verbal", required = true)
    private Boolean verbal;
    @JoinColumn(name = "EncounterID", referencedColumnName = "ID")
    @OneToOne
    @JsonIgnore
    private Encounter encounterID;
    @OneToMany(mappedBy = "authorisation", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Outcome", required = false)
    private List<AuthorisationOutcome> outcome;
    public Authorisation() {
    }
    public Authorisation(Integer id) {
        this.id = id;
    }
    public Date getStart() {
        return start;
    }
    public void setStart(Date start) {
        this.start = start;
    }
    public Date getEnd() {
        return end;
    }
    public void setEnd(Date end) {
        this.end = end;
    }
    public Boolean getVerbal() {
        return verbal;
    }
    public void setVerbal(Boolean verbal) {
        this.verbal = verbal;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Encounter getEncounterID() {
        return encounterID;
    }
    public void setEncounterID(Encounter encounterID) {
        this.encounterID = encounterID;
    }
    public List<AuthorisationOutcome> getOutcome() {
        return outcome;
    }
    public void setOutcome(List<AuthorisationOutcome> outcome) {
        this.outcome = outcome;
    }
    public void addOutcome(String severity, String ruleName, String ShortMsg, String longMsg) {
        if (this.outcome != null) {
            this.outcome.add(new AuthorisationOutcome(severity, ruleName, ShortMsg, longMsg));
        } else {
            this.outcome = new ArrayList<AuthorisationOutcome>();
            this.outcome.add(new AuthorisationOutcome(severity, ruleName, ShortMsg, longMsg));
        }
    }
}
