package com.santechture;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import com.santechture.service.ksa.mssql.*;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Getter
public class KSAInitializing {

    //KSA DB Clients
    @Inject
    CodeGroupsDBClient codeGroupsDBClient;
    @Inject
    ContractsDBClient contractsDBClient;

    @Inject
    CusContractCodesConfigurationDBClient cusContractCodesConfigurationDBClient;
    @Inject
    CusContractConfigurationDBClient cusContractConfigurationDBClient;
    @Inject
    CusPriceListItemDBClient cusPriceListItemDBClient;
    @Inject
    DrugPricesDBClient drugsDBClient;
    @Inject
    FacilityCliniciansDBClient facilityCliniciansDBClient;
    @Inject
    FacilityDBClient facilityDBClient;
    @Inject
    SpcCodeFactorsDBClient spcCodeFactorsDBClient;
    @Inject
    SPCContractsDBClient spcContractsDBClient;
    @Inject
    SpcGroupFactorsDBClient spcGroupFactorsDBClient;
    @Inject
    SPCMasterListsDBClient spcMasterListsDBClient;
    @Inject
    SPCMasterListsItemsDBClient spcMasterListsItemsDBClient;

    private List<DBClientBase> dbClients;
    @PostConstruct
    private void init() {

        dbClients = List.of(codeGroupsDBClient, contractsDBClient, cusContractCodesConfigurationDBClient, cusContractConfigurationDBClient, cusPriceListItemDBClient, drugsDBClient, facilityCliniciansDBClient, facilityDBClient, spcCodeFactorsDBClient, spcContractsDBClient, spcGroupFactorsDBClient, spcMasterListsDBClient, spcMasterListsItemsDBClient);
    }
}
