package com.santechture.request;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.santechture.converter.StringLengthDeserializer;
import com.santechture.converter.StringLengthSerializer;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Resubmission implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private Integer id;
    @JsonProperty(value ="Type", required = true)
    private String type;
    @JsonProperty(value ="Comment", required = true)
    @JsonSerialize(using = StringLengthSerializer.class)
    @JsonDeserialize(using = StringLengthDeserializer.class)
    private String comment;
    @JsonProperty(value ="Attachment")
    private byte[] attachment;
    @JoinColumn(name = "claimID", referencedColumnName = "ID")
    @OneToOne
    @JsonIgnore
    private Claim claimID;
    @OneToMany(mappedBy = "resubmission", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty(value ="Outcome", required = false)
    private List<ResubmissionOutcome> outcome;
    public Resubmission() {
    }
    public Resubmission(Integer id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public byte[] getAttachment() {
        return attachment;
    }
    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Claim getClaimID() {
        return claimID;
    }
    public void setClaimID(Claim claimID) {
        this.claimID = claimID;
    }
    public List<ResubmissionOutcome> getOutcome() {
        return outcome;
    }
    public void setOutcome(List<ResubmissionOutcome> outcome) {
        this.outcome = outcome;
    }
    public void addOutcome(String severity, String ruleName, String ShortMsg, String longMsg) {
        if (this.outcome != null) {
            this.outcome.add(new ResubmissionOutcome(severity, ruleName, ShortMsg, longMsg));
        } else {
            this.outcome = new ArrayList<ResubmissionOutcome>();
            this.outcome.add(new ResubmissionOutcome(severity, ruleName, ShortMsg, longMsg));
        }
    }
}
