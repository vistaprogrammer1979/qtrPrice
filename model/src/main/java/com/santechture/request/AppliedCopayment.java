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
public class AppliedCopayment implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String type;
    @JsonProperty(value ="TotalValue", required = false)
    private double totalValue;
    @JoinColumn(name = "claimID", referencedColumnName = "ID")
    @ManyToOne
    @JsonIgnore
    private Claim claimID;
    public AppliedCopayment() {
    }
    public AppliedCopayment(String type) {
        this.type = type;
    }
    public AppliedCopayment(String type, double totalValue) {
        this.type = type;
        this.totalValue = totalValue;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public double getTotalValue() {
        return totalValue;
    }
    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Claim getClaimID() {
        return claimID;
    }
    public void setClaimID(Claim claimID) {
        this.claimID = claimID;
    }
}
