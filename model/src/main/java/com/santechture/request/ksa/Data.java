package com.santechture.request.ksa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.santechture.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Data {
    Claim claim;
    List<CusContract>  cusContractList;
    List<CusPriceListItem> cusPriceListItemsList;
    List<DHA_DRG_COST_PER_ACTIVITY> dhaDrgCostPerActivityList;
    List<DHA_DRG> dhaDrgList;
    List<DRGExcludedCpts> drgExcludedCptList;
    List<DHA_DRG_HighCost> dhaDrgHighCostList;
    List<DrugPrice> drugPriceList;
    List<Clinician> clinicianList;
    List<Facility> facilityList;
    List<SPCCodeFactor> spcCodeFactorList;
    List<SPCContract> spcContractList;
    List<SPCGroupFactor> spcGroupFactorList;
    List<MasterPriceList> masterPriceListList;
    List<MasterPriceListItem> masterPriceListItemList;
    List<CodeGroup> codeGroupList;
    List<PackageCode> PackageCodeList;
    List<RCMFacilityCodeMapping> rcmFacilityCodeMappingList;
    List<CusContractConfigurations> cusContractConfigurationsList;

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public List<CusContract> getCusContractList() {
        return cusContractList;
    }

    public void setCusContractList(List<CusContract> cusContractList) {
        this.cusContractList = cusContractList;
    }

    public List<CusPriceListItem> getCusPriceListItemsList() {
        return cusPriceListItemsList;
    }

    public void setCusPriceListItemsList(List<CusPriceListItem> cusPriceListItemsList) {
        this.cusPriceListItemsList = cusPriceListItemsList;
    }

    public List<DHA_DRG_COST_PER_ACTIVITY> getDhaDrgCostPerActivityList() {
        return dhaDrgCostPerActivityList;
    }

    public void setDhaDrgCostPerActivityList(List<DHA_DRG_COST_PER_ACTIVITY> dhaDrgCostPerActivityList) {
        this.dhaDrgCostPerActivityList = dhaDrgCostPerActivityList;
    }

    public List<DHA_DRG> getDhaDrgList() {
        return dhaDrgList;
    }

    public void setDhaDrgList(List<DHA_DRG> dhaDrgList) {
        this.dhaDrgList = dhaDrgList;
    }

    public List<DRGExcludedCpts> getDrgExcludedCptList() {
        return drgExcludedCptList;
    }

    public void setDrgExcludedCptList(List<DRGExcludedCpts> drgExcludedCptList) {
        this.drgExcludedCptList = drgExcludedCptList;
    }

    public List<DHA_DRG_HighCost> getDhaDrgHighCostList() {
        return dhaDrgHighCostList;
    }

    public void setDhaDrgHighCostList(List<DHA_DRG_HighCost> dhaDrgHighCostList) {
        this.dhaDrgHighCostList = dhaDrgHighCostList;
    }

    public List<DrugPrice> getDrugPriceList() {
        return drugPriceList;
    }

    public void setDrugPriceList(List<DrugPrice> drugPriceList) {
        this.drugPriceList = drugPriceList;
    }

    public List<Clinician> getClinicianList() {
        return clinicianList;
    }

    public void setClinicianList(List<Clinician> clinicianList) {
        this.clinicianList = clinicianList;
    }

    public List<Facility> getFacilityList() {
        return facilityList;
    }

    public void setFacilityList(List<Facility> facilityList) {
        this.facilityList = facilityList;
    }

    public List<SPCCodeFactor> getSpcCodeFactorList() {
        return spcCodeFactorList;
    }

    public void setSpcCodeFactorList(List<SPCCodeFactor> spcCodeFactorList) {
        this.spcCodeFactorList = spcCodeFactorList;
    }

    public List<SPCContract> getSpcContractList() {
        return spcContractList;
    }

    public void setSpcContractList(List<SPCContract> spcContractList) {
        this.spcContractList = spcContractList;
    }

    public List<SPCGroupFactor> getSpcGroupFactorList() {
        return spcGroupFactorList;
    }

    public void setSpcGroupFactorList(List<SPCGroupFactor> spcGroupFactorList) {
        this.spcGroupFactorList = spcGroupFactorList;
    }

    public List<MasterPriceList> getMasterPriceListList() {
        return masterPriceListList;
    }

    public void setMasterPriceListList(List<MasterPriceList> masterPriceListList) {
        this.masterPriceListList = masterPriceListList;
    }

    public List<MasterPriceListItem> getMasterPriceListItemList() {
        return masterPriceListItemList;
    }

    public void setMasterPriceListItemList(List<MasterPriceListItem> masterPriceListItemList) {
        this.masterPriceListItemList = masterPriceListItemList;
    }

    public List<CodeGroup> getCodeGroupList() {
        return codeGroupList;
    }

    public void setCodeGroupList(List<CodeGroup> codeGroupList) {
        this.codeGroupList = codeGroupList;
    }

    public List<PackageCode> getPackageCodeList() {
        return PackageCodeList;
    }

    public void setPackageCodeList(List<PackageCode> packageCodeList) {
        PackageCodeList = packageCodeList;
    }

    public List<RCMFacilityCodeMapping> getRcmFacilityCodeMappingList() {
        return rcmFacilityCodeMappingList;
    }

    public void setRcmFacilityCodeMappingList(List<RCMFacilityCodeMapping> rcmFacilityCodeMappingList) {
        this.rcmFacilityCodeMappingList = rcmFacilityCodeMappingList;
    }

    public List<CusContractConfigurations> getCusContractConfigurationsList() {
        return cusContractConfigurationsList;
    }

    public void setCusContractConfigurationsList(List<CusContractConfigurations> cusContractConfigurationsList) {
        this.cusContractConfigurationsList = cusContractConfigurationsList;
    }
}
