package com.santechture.facts.qatar.service;

import com.santechture.facts.qatar.service.cache.RedisService;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
@ApplicationScoped
public class InitializeService {
    private static final Logger log = LoggerFactory.getLogger("InitializeService");

    @Inject
    RedisService redisService;
    private List<String> masterPriceListKeyList;
    private List<String> cusContractKeyList;

    private ArrayList<String> clinicalServiceGroups;
    private ArrayList<String> generalServiceGroups;
    private ArrayList<String> investigationServiceGroups;
    private ArrayList<String> labSuppliesServiceGroups;
    private ArrayList<String> roomAndBedServiceGroups;
    private ArrayList<String> otherServiceGroups;
    private ArrayList<String> pharmaceticalsAndSuppliesServiceGroups;
    private ArrayList<String> oncServiceGroups;
    private ArrayList<String> staffHealthInsuranceChargeType;
    private ArrayList<String> packagedServices;




    @Startup
    public void updateRedisKeys() {

        // Qatar
        log.info("InitializeService - start caching - Qatar");
        masterPriceListKeyList = redisService.getKeys("MasterPriceList:*");
        log.info("MasterPriceList {}", masterPriceListKeyList.size());
        cusContractKeyList = redisService.getKeys("CusContract:*");
        log.info("CusContract{}", cusContractKeyList.size());

        clinicalServiceGroups = redisService.getGroupsNames("Clinical");
        log.info("Clinical {}", clinicalServiceGroups.size());
        generalServiceGroups = redisService.getGroupsNames("General");
        log.info("General {}", generalServiceGroups.size());
        investigationServiceGroups = redisService.getGroupsNames("Investigation");
        log.info("Investigation {}", investigationServiceGroups.size());
        labSuppliesServiceGroups = redisService.getGroupsNames("LabSupplies");
        log.info("LabSupplies {}", labSuppliesServiceGroups.size());
        roomAndBedServiceGroups = redisService.getGroupsNames("RoomAndBed");
        log.info("RoomAndBed {}", roomAndBedServiceGroups.size());
        otherServiceGroups = redisService.getGroupsNames("Other");
        log.info("Other {}", otherServiceGroups.size());
        pharmaceticalsAndSuppliesServiceGroups = redisService.getGroupsNames("PharmaceticalsAndSupplies");
        log.info("PharmaceticalsAndSupplies {}", pharmaceticalsAndSuppliesServiceGroups.size());
        oncServiceGroups = redisService.getGroupsNames("Onc");
        log.info("Onc {}", oncServiceGroups.size());
        staffHealthInsuranceChargeType = redisService.getGroupsNames("StaffHealthInsuranceChargeType");
        log.info("StaffHealthInsuranceChargeType {}", staffHealthInsuranceChargeType.size());
        packagedServices = redisService.getGroupsNames("PackagedServices");
        log.info("PackagedServices {}", packagedServices.size());
    }

    public void updateMasterPriceList(){
        log.info("InitializeService - updateMasterPriceList");
        masterPriceListKeyList = redisService.getKeys("MasterPriceList:*");
    }
    public void updateCusContract(){
        log.info("InitializeService - updateCusContract");
        cusContractKeyList = redisService.getKeys("CusContract:*");
    }
   public void updateClinical(){
       log.info("InitializeService - updateClinical");
       clinicalServiceGroups = redisService.getGroupsNames("Clinical");
   }
   public void updateGeneral(){
       log.info("InitializeService - updateGeneral");
       generalServiceGroups = redisService.getGroupsNames("General");
   }
   public void updateInvestigation(){
       log.info("InitializeService - updateInvestigation");
       investigationServiceGroups = redisService.getGroupsNames("Investigation");
   }
   public void updateLabSupplies(){
       log.info("InitializeService - updateLabSupplies");
       labSuppliesServiceGroups = redisService.getGroupsNames("LabSupplies");
   }
    public void updateRoomAndBed(){
        log.info("InitializeService - updateRoomAndBed");
        roomAndBedServiceGroups = redisService.getGroupsNames("RoomAndBed");
    }
    public void updateOther(){
        log.info("InitializeService - updateOther");
        otherServiceGroups = redisService.getGroupsNames("Other");
    }
    public void updatePharmaceticalsAndSupplies(){
        log.info("InitializeService - updatePharmaceticalsAndSupplies");
        pharmaceticalsAndSuppliesServiceGroups = redisService.getGroupsNames("PharmaceticalsAndSupplies");
    }
    public void updateOnc(){
        log.info("InitializeService - updateOnc");
        oncServiceGroups = redisService.getGroupsNames("Onc");
    }
    public void updateStaffHealthInsuranceChargeType(){
        log.info("InitializeService - updateStaffHealthInsuranceChargeType");
        staffHealthInsuranceChargeType = redisService.getGroupsNames("StaffHealthInsuranceChargeType");
    }
    public void updatePackagedServices(){
        log.info("InitializeService - updatePackagedServices ");
        packagedServices = redisService.getGroupsNames("PackagedServices");
    }

}
