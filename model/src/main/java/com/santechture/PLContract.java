package com.santechture;


public class PLContract {

    Integer id;
    Integer type;
    Double baseRate;
    Double gap;
    Double marginal;
    Double negotiation_drg_factor;
    String packageName;

    public PLContract(Integer id, Integer type, Double baseRate, Double gap, Double marginal, Double negotiation_drg_factor,
                      String packageName) {
        this.id = id;
        this.type = type;
        this.baseRate = baseRate;
        this.gap = gap;
        this.marginal = marginal;
        this.packageName = packageName;
        this.negotiation_drg_factor = negotiation_drg_factor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(Double baseRate) {
        this.baseRate = baseRate;
    }

    public Double getGap() {
        return gap;
    }

    public void setGap(Double gap) {
        this.gap = gap;
    }

    public Double getMarginal() {
        return marginal;
    }

    public void setMarginal(Double marginal) {
        this.marginal = marginal;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Double getNegotiation_drg_factor() {
        return negotiation_drg_factor;
    }

    public void setNegotiation_drg_factor(Double negotiation_drg_factor) {
        this.negotiation_drg_factor = negotiation_drg_factor;
    }



}
