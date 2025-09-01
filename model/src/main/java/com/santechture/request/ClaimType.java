package com.santechture.request;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import java.io.Serializable;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimType implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String Maternity = "Maternity";
    public static final String Dental = "Dental";
    public static final String Optical = "Optical";
    public static final String None = "None";
    @JsonIgnore
    private Integer id;
    @JsonProperty(value ="Type", required = true)
    private String type;
    @JoinColumn(name = "claimID", referencedColumnName = "ID")
    @ManyToOne
    @JsonIgnore
    private Claim claimID;
    public ClaimType() {
    }
    public ClaimType(String type) {
        this.type = type;
    }
    public ClaimType(Integer id) {
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
    public Claim getClaimID() {
        return claimID;
    }
    public void setClaimID(Claim claimID) {
        this.claimID = claimID;
    }
}
