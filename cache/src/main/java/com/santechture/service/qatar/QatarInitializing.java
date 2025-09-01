package com.santechture.service.qatar;

import com.santechture.service.qatar.mssql.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Getter
public class QatarInitializing {

    //Qatar DB Clients
//    @Inject
//    SPCContractService spcContractClient;
    @Inject
    FacilityDBClient facilityDBClient;
    @Inject
    ContractsDBClient contractsDBClient;
    @Inject
    CusPriceListItemDBClient cusPriceListItemDBClient;
    @Inject
    CusPriceListDBClient cusPriceListDBClient;
//    @Inject
//    DhaDrgCostPreActivityDBClient dhaDrgCostPreActivityDBClient;
//    @Inject
//    DhaDrgDBClient dhaDrgDBClient;
//    @Inject
//    DhaDrgExcludedCptDBClient dhaDrgExcludedCptDBClient;
//    @Inject
//    DhaDrgHighCostDBClient dhaDrgHighCostDBClient;
//    @Inject
//    DrugPricesDBClient drugPricesDBClient;
//    @Inject
//    FacilityCliniciansDBClient facilityCliniciansDBClient;
//    @Inject
//    SpcCodeFactorsDBClient spcCodeFactorsDBClient;
//    @Inject
//    SpcGroupFactorsDBClient spcGroupFactorsDBClient;
//    @Inject
//    SPCMasterListsDBClient masterListsDBClient;
//    @Inject
//    SPCMasterListsItemsDBClient masterListsItemsDBClient;
    @Inject
    CodeGroupsDBClient codeGroupsDBClient;
    @Inject
    CusPriceListItem_Dentistry_DBClient cusPriceListItemDentistryDbClient;
    @Inject
    CusPriceListItem_Emergency_DBClient cusPriceListItemEmergencyDbClient;
    @Inject
    CusRadiology_ACHIPriceListItem_DBClient cusPriceListItemRadiologyAchiDbClient;
    @Inject
    CusPriceListItem_IPARDRG_DBClient cusPriceListItemIpArdrgDbClient;
    @Inject
    CusOP_QOCSPriceListItem_DBClient cusPriceListItemOpQocsDbClient;
    @Inject
    CusIP_SUBPriceListItem_DBClient cusIPSubPriceListItemDbClient;
    @Inject
    MedicinesPriceListItem_DBClient medicinesPriceListItemDbClient;




    private List<DBClientBase> dbClients;
    @PostConstruct
    private void init() {
        dbClients = new ArrayList<>();
        dbClients.add(facilityDBClient);
        dbClients.add(contractsDBClient);
        dbClients.add(cusPriceListDBClient);
        dbClients.add(cusPriceListItemDBClient);
        dbClients.add(cusPriceListItemIpArdrgDbClient);
        dbClients.add(cusPriceListItemOpQocsDbClient);
        dbClients.add(cusPriceListItemEmergencyDbClient);
        dbClients.add(cusPriceListItemDentistryDbClient);
        dbClients.add(cusPriceListItemRadiologyAchiDbClient);
        dbClients.add(cusIPSubPriceListItemDbClient);
        dbClients.add(codeGroupsDBClient);
        dbClients.add(medicinesPriceListItemDbClient);

    }
}
