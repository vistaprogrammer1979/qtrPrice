package com.santechture.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.santechture.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Facts {
    protected List<CusContract>  cusContractList;
    protected List<CusPriceListItem> cusPriceListItemsList;
    protected List<DHA_DRG_COST_PER_ACTIVITY> dhaDrgCostPerActivityList;
    protected List<DHA_DRG> dhaDrgList;
    protected List<DRGExcludedCpts> drgExcludedCptList;
    protected List<DHA_DRG_HighCost> dhaDrgHighCostList;
    protected List<DrugPrice> drugPriceList;
    protected List<Clinician> clinicianList;
    protected List<Facility> facilityList;
    protected List<SPCCodeFactor> spcCodeFactorList;
    protected List<SPCContract> spcContractList;
    protected List<SPCGroupFactor> spcGroupFactorList;
    protected List<MasterPriceList> masterPriceListList;
    protected List<MasterPriceListItem> masterPriceListItemList;
    protected List<CodeGroup> codeGroupList;
    protected List<PackageCode> PackageCodeList;
    protected List<RCMFacilityCodeMapping> rcmFacilityCodeMappingList;
    protected List<CusPriceList> cusPriceList;
    protected List<CusContractConfigurations> cusContractConfigurationsList;
    protected List<PLCUSContractCodesConfigurations> plcusContractCodesConfigurationsList;
    protected List<CusDentistryASDSGPriceListItem> cusDentistryAsdsgPriceListItems;
    protected List<CusEmergencyURGPriceListItem> cusEmergencyUrgPriceListItems;
    protected List<CusOP_QOCSPriceListItem> cusOpQocsPriceListItems;
    protected List<CusRadiology_ACHIPriceListItem> cusRadiologyAchiPriceListItems;
    protected List<CusIP_ARDRGPriceListItem> cusIP_ARDRGPriceListItems;
    protected List<MedicinesPriceListItem> medicinesPriceListItems;

}
