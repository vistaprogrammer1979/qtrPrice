package com.santechture.price.uae;

import com.santechture.price.filter.Loggable;
import com.santechture.request.Data;
import com.santechture.service.SessionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.kie.api.runtime.KieRuntimeBuilder;
import org.kie.api.runtime.KieSession;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;


@Path("api/v1")
public class PricingEngineResource {
    @Inject
    KieRuntimeBuilder kieRuntimeBuilder;
    @Inject
    SessionService sessionService;

    Logger logger = Logger.getLogger(PricingEngineResource.class.getName());
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    @Loggable
    @POST()
    @Path("price")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Data getPrice(Data data) {
        logger.info("new request");
//        logger.info(data.getDrugPriceList().toString());
        KieSession session = kieRuntimeBuilder.newKieSession();
        session.insert(data.getClaim());
        if (data.getClaim().getActivity() != null){
            sessionService.addListToSession(data.getClaim().getActivity(), session);
        }
        if(data.getClaim().getPatient() != null && data.getClaim().getPatient().getPatientInsurance() != null){
            if(data.getClaim().getPatient().getPatientInsurance().getDeductible() != null){
                sessionService.addListToSession(data.getClaim().getPatient().getPatientInsurance().getDeductible(), session);
            }
            if(data.getClaim().getPatient().getPatientInsurance().getCoPayment() != null){
                sessionService.addListToSession(data.getClaim().getPatient().getPatientInsurance().getCoPayment(), session);
            }
        }

        sessionService.addListToSession(data.getCodeGroupList(), session);
        sessionService.addListToSession(data.getCusContractList(), session);
        sessionService.addListToSession(data.getCusPriceListItemsList(), session);
        sessionService.addListToSession(data.getDhaDrgCostPerActivityList(), session);
        sessionService.addListToSession(data.getDhaDrgList(), session);
        sessionService.addListToSession(data.getDrgExcludedCptList(), session);
        sessionService.addListToSession(data.getDhaDrgHighCostList(), session);
        sessionService.addListToSession(data.getDrugPriceList(), session);
        sessionService.addListToSession(data.getFacilityList(), session);
        sessionService.addListToSession(data.getClinicianList(), session);
        sessionService.addListToSession(data.getSpcGroupFactorList(), session);
        sessionService.addListToSession(data.getSpcCodeFactorList(), session);
        sessionService.addListToSession(data.getSpcContractList(), session);
        sessionService.addListToSession(data.getMasterPriceListItemList(), session);
        sessionService.addListToSession(data.getMasterPriceListList(), session);
        sessionService.addListToSession(data.getPackageCodeList(), session);
        sessionService.addListToSession(data.getRcmFacilityCodeMappingList(), session);




//        if(data.getClaim().getActivity() != null)
//        {
//            for (Activity a : data.getClaim().getActivity()) {
//                session.insert(a);
//            }
//        }
//
//        if(data.getCodeGroupList() != null)
//        {
//            for (CodeGroup a :data.getCodeGroupList()) {
//                session.insert(a);
//            }
//        }

//        session.insert(data.getCusContractList());
//        session.insert(data.getCusPriceListItemsList());
//        session.insert(data.getDhaDrgCostPerActivityList());
//        session.insert(data.getDhaDrgList());
//        session.insert(data.getDrgExcludedCptList());
//        session.insert(data.getDhaDrgHighCostList());
//        session.insert(data.getDrugPriceList());
//        session.insert(data.getClinicianList());
//        session.insert(data.getSpcGroupFactorList());
//        session.insert(data.getSpcCodeFactorList());
//        session.insert(data.getSpcContractList());
//        session.insert(data.getMasterPriceListItemList());
//        session.insert(data.getMasterPriceListList());
        session.fireAllRules();
        return data;
    }

//
//    @Path("getBaseRate")
//    @GET()
//    public Double getBaseRate(
//            @FormParam("insurerLicense") String insurerLicense,
//            @FormParam("facilityLicense") String facilityLicense,
//            @FormParam("packageName") String packageName,
//            @FormParam("date") String date) throws InvalidArgument {
//        if (date == null || date.isEmpty() || !date.matches("^(((0[1-9]|[12]\\d|3[01])\\-(0[13578]|1[02])\\-((19|[2-9]\\d)\\d{2}))|((0[1-9]|[12]\\d|30)\\-(0[13456789]|1[012])\\-((19|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8])\\-02\\-((19|[2-9]\\d)\\d{2}))|(29\\-02\\-((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$")) {
//            throw new InvalidArgument("Invalid Date, or date format is invalid.");
//        }
//        Date effDate;
//        Integer regulatorID = 1;
//        try {
//            effDate = formatter.parse(date);
//        } catch (ParseException ex) {
//            logger.log(Level.SEVERE, null, ex);
//            throw new InvalidArgument("Invalid Date, or date format is invalid.");
//        }
//
//        if (insurerLicense == null || insurerLicense.isEmpty()) {
//            throw new InvalidArgument("Invalid insurer license.");
//        }
//
//        if (facilityLicense == null || facilityLicense.isEmpty()) {
//            throw new InvalidArgument("Invalid facility license.");
//        }
//
//        /*if (packageName == null || packageName.isEmpty()) {
//         throw new InvalidArgument("Invalid package name.");
//         }*/
//        if (packageName != null && packageName.trim().isEmpty()) {
//            packageName = null;
//        }
//        List<PLContract> contracts = this.getContracts(insurerLicense, facilityLicense,
//                packageName, effDate, effDate);
//        if (packageName == null && !contracts.isEmpty()) {
//            return contracts.get(0).getBaseRate() == null ? (double) 0 : contracts.get(0).getBaseRate();
//        } else {
//            for (PLContract contract : contracts) {
//                if (contract.getPackageName() != null
//                        && contract.getPackageName().equalsIgnoreCase(packageName)) {
//                    return contract.getBaseRate() == null ? (double) 0 : contract.getBaseRate();
//                }
//            }
//            //18 June 2018 -- if package contract is not found ... get baserate for contract with null package
//            for (PLContract contract : contracts) {
//                if (contract.getPackageName()==null) {
//                    return contract.getBaseRate() == null ? (double) 0 : contract.getBaseRate();
//                }
//            }
//            //End 18 June 2018
//        }
//        return 0d;
//    }
//
//    @Path("getGAP")
//    @GET()
//    public Double getGAP(
//            @FormParam("insurerLicense") String insurerLicense,
//            @FormParam("facilityLicense") String facilityLicense,
//            @FormParam("packageName") String packageName,
//            @FormParam("date") String date)
//            throws InvalidArgument {
//        if (date == null || date.isEmpty() || !date.matches("^(((0[1-9]|[12]\\d|3[01])\\-(0[13578]|1[02])\\-((19|[2-9]\\d)\\d{2}))|((0[1-9]|[12]\\d|30)\\-(0[13456789]|1[012])\\-((19|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8])\\-02\\-((19|[2-9]\\d)\\d{2}))|(29\\-02\\-((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$")) {
//            throw new InvalidArgument("Invalid Date, or date format is invalid.");
//        }
//        Date effDate;
//        Integer regulatorID = 1;
//        try {
//            effDate = formatter.parse(date);
//        } catch (ParseException ex) {
//            logger.log(Level.SEVERE, null, ex);
//            throw new InvalidArgument("Invalid Date, or date format is invalid.");
//        }
//
//        if (insurerLicense == null || insurerLicense.isEmpty()) {
//            throw new InvalidArgument("Invalid insurer license.");
//        }
//
//        if (facilityLicense == null || facilityLicense.isEmpty()) {
//            throw new InvalidArgument("Invalid facility license.");
//        }
//
//        /*if (packageName == null || packageName.isEmpty()) {
//         throw new InvalidArgument("Invalid package name.");
//         }*/
//        if (packageName != null && packageName.trim().isEmpty()) {
//            packageName = null;
//        }
//
//        List<PLContract> contracts = this.getContracts(insurerLicense, facilityLicense,
//                packageName, effDate, effDate);
//        if (packageName == null && !contracts.isEmpty()) {
//            return contracts.get(0).getGap() == null ? 0.0D : contracts.get(0).getGap();
//        } else {
//            for (PLContract contract : contracts) {
//                if (contract.getPackageName() != null
//                        && contract.getPackageName().equalsIgnoreCase(packageName)) {
//                    return contract.getGap() == null ? 0.0D : contract.getGap();
//                }
//            }
//        }
//        return 0d;
//    }
//
//    @Path("getMarginal")
//    @GET()
//    public Double getMarginal(
//            @FormParam("insurerLicense") String insurerLicense,
//            @FormParam("facilityLicense") String facilityLicense,
//            @FormParam("packageName") String packageName,
//            @FormParam("date") String date)
//            throws InvalidArgument {
//        if (date == null || date.isEmpty() || !date.matches("^(((0[1-9]|[12]\\d|3[01])\\-(0[13578]|1[02])\\-((19|[2-9]\\d)\\d{2}))|((0[1-9]|[12]\\d|30)\\-(0[13456789]|1[012])\\-((19|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8])\\-02\\-((19|[2-9]\\d)\\d{2}))|(29\\-02\\-((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$")) {
//            throw new InvalidArgument("Invalid Date, or date format is invalid.");
//        }
//        Date effDate;
//        Integer regulatorID = 1;
//        try {
//            effDate = formatter.parse(date);
//        } catch (ParseException ex) {
//            logger.log(Level.SEVERE, null, ex);
//            throw new InvalidArgument("Invalid Date, or date format is invalid.");
//        }
//
//        if (insurerLicense == null || insurerLicense.isEmpty()) {
//            throw new InvalidArgument("Invalid insurer license.");
//        }
//
//        if (facilityLicense == null || facilityLicense.isEmpty()) {
//            throw new InvalidArgument("Invalid facility license.");
//        }
//
//        /*if (packageName == null || packageName.isEmpty()) {
//         throw new InvalidArgument("Invalid package name.");
//         }*/
//        if (packageName != null && packageName.trim().isEmpty()) {
//            packageName = null;
//        }
//
//        List<PLContract> contracts = this.getContracts(insurerLicense, facilityLicense,
//                packageName, effDate, effDate);
//        if (packageName == null && !contracts.isEmpty()) {
//            return contracts.get(0).getMarginal() == null ? 0.0D : contracts.get(0).getMarginal();
//        } else {
//            for (PLContract contract : contracts) {
//                if (contract.getPackageName() != null
//                        && contract.getPackageName().equalsIgnoreCase(packageName)) {
//                    return contract.getMarginal() == null ? 0.0D : contract.getMarginal();
//                }
//            }
//        }
//        return 0d;
//    }
//
//    @Path( "isContractExists")
//    @GET()
//    public boolean isContractExists(
//            @FormParam("insurerLicense") String insurerLicense,
//            @FormParam("facilityLicense") String facilityLicense,
//            @FormParam("packageName") String packageName,
//            @FormParam("date") String date,
//            @FormParam("ignorePackage") Boolean ignorePackage)
//            throws InvalidArgument {
//
//        if (date == null || date.isEmpty() || !date.matches("^(((0[1-9]|[12]\\d|3[01])\\-(0[13578]|1[02])\\-((19|[2-9]\\d)\\d{2}))|((0[1-9]|[12]\\d|30)\\-(0[13456789]|1[012])\\-((19|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8])\\-02\\-((19|[2-9]\\d)\\d{2}))|(29\\-02\\-((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$")) {
//            throw new InvalidArgument("Invalid Date, or date format is invalid.");
//        }
//        Date effDate;
//        try {
//            effDate = formatter.parse(date);
//        } catch (ParseException ex) {
//            logger.log(Level.SEVERE, null, ex);
//            throw new InvalidArgument("Invalid Date, or date format is invalid.");
//        }
//
//        if (insurerLicense == null || insurerLicense.isEmpty()) {
//            throw new InvalidArgument("Invalid insurer license.");
//        }
//
//        if (facilityLicense == null || facilityLicense.isEmpty()) {
//            throw new InvalidArgument("Invalid facility license.");
//        }
//
//        if (packageName != null && packageName.trim().isEmpty()) {
//            packageName = null;
//        }
//
//        List<PLContract> contracts = this.getContracts(insurerLicense, facilityLicense,
//                packageName, effDate, effDate);
//        if (ignorePackage) {
//            return !contracts.isEmpty();
//        } else {
//            for (PLContract contract : contracts) {
//                if (contract.getPackageName() != null
//                        && contract.getPackageName().equalsIgnoreCase(packageName)) {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }
//
//    @Path( "getNegotiationDrgFactor")
//    @GET()
//    public Double getNegotiationDrgFactor(
//            @FormParam("insurerLicense") String insurerLicense,
//            @FormParam("facilityLicense") String facilityLicense,
//            @FormParam("packageName") String packageName,
//            @FormParam("date") String date)
//            throws InvalidArgument {
//        if (date == null || date.isEmpty() || !date.matches("^(((0[1-9]|[12]\\d|3[01])\\-(0[13578]|1[02])\\-((19|[2-9]\\d)\\d{2}))|((0[1-9]|[12]\\d|30)\\-(0[13456789]|1[012])\\-((19|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8])\\-02\\-((19|[2-9]\\d)\\d{2}))|(29\\-02\\-((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$")) {
//            throw new InvalidArgument("Invalid Date, or date format is invalid.");
//        }
//        Date effDate;
//        Integer regulatorID = 1;
//        try {
//            effDate = formatter.parse(date);
//        } catch (ParseException ex) {
//            logger.log(Level.SEVERE, null, ex);
//            throw new InvalidArgument("Invalid Date, or date format is invalid.");
//        }
//
//        if (insurerLicense == null || insurerLicense.isEmpty()) {
//            throw new InvalidArgument("Invalid insurer license.");
//        }
//
//        if (facilityLicense == null || facilityLicense.isEmpty()) {
//            throw new InvalidArgument("Invalid facility license.");
//        }
//
//
//        if (packageName != null && packageName.trim().isEmpty()) {
//            packageName = null;
//        }
//        List<PLContract> contracts = this.getContracts(insurerLicense, facilityLicense,
//                packageName, effDate, effDate);
//        if (packageName == null && !contracts.isEmpty()) {
//            return contracts.get(0).getIP_DRG_Factor()== null ? (double) 0 : contracts.get(0).getIP_DRG_Factor();
//        } else {
//            for (PLContract contract : contracts) {
//                if (contract.getPackageName() != null
//                        && contract.getPackageName().equalsIgnoreCase(packageName)) {
//                    return contract.getIP_DRG_Factor() == null ? (double) 0 : contract.getIP_DRG_Factor();
//                }
//            }
//            //18 June 2018 -- if package contract is not found ... get baserate for contract with null package
//            for (PLContract contract : contracts) {
//                if (contract.getPackageName()==null) {
//                    return contract.getIP_DRG_Factor() == null ? (double) 0 : contract.getIP_DRG_Factor();
//                }
//            }
//            //End 18 June 2018
//        }
//        return 0d;
//    }
//
//    private List<PLContract> getContracts(String insurerLicense,
//                                          String facilityLicense,
//                                          String packageName,
//                                          Date startDate,
//                                          Date endDate) {
//
////        Connection dbConn = null;
////        try {
////            dbConn = getPriceDB();
////            return com.accumed.pricing.cachedRepo.RepoUtils.getContracts(
////                    dbConn, facilityLicense,
////                    insurerLicense, startDate, endDate);
////
////        } catch (Exception ex) {
////            Logger.getLogger(PricingEngine.class.getName()).log(Level.SEVERE, null, ex);
////        } finally {
////            if (dbConn != null) {
////                try {
////                    dbConn.close();
////                } catch (SQLException ex) {
////                    Logger.getLogger(PricingEngine.class.getName()).log(Level.SEVERE, null, ex);
////                }
////                dbConn = null;
////            }
////        }
//        return null;
//    }
}
