package com.santechture.facts.qatar.service.client;

import com.santechture.CodeGroup;
import com.santechture.facts.qatar.service.InitializeService;
import com.santechture.request.Activity;
import com.santechture.request.Data;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class CodeGroupsClient extends ClientBase {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CodeGroupsClient.class);
    @Inject
    Logger logger;
    @Inject
    InitializeService initializeService;

    @Override
    public List<String> getKeys(Data data) {
        var claim = data.getClaim();
        var activities = claim.getActivity();
        var diagnoses = claim.getDiagnosis();
        List<String> keys = new ArrayList<>();
        if (activities != null) {

            for (Activity activity : activities) {
                var activity_groups = new ArrayList<String>();
                switch (activity.getType()) {
                    case Clinical -> activity_groups = initializeService.getClinicalServiceGroups();
                    case General -> activity_groups = initializeService.getGeneralServiceGroups();
                    case INVESTIGATION -> activity_groups = initializeService.getInvestigationServiceGroups();
                    case LabSupplies -> activity_groups = initializeService.getLabSuppliesServiceGroups();
                    case RoomAndBed -> activity_groups = initializeService.getRoomAndBedServiceGroups();
                    case Other -> activity_groups = initializeService.getOtherServiceGroups();
                    case PharmaceticalsAndSupplies ->
                            activity_groups = initializeService.getPharmaceticalsAndSuppliesServiceGroups();
                    case onc -> activity_groups = initializeService.getOncServiceGroups();
                    case StaffHealthInsuranceCharge ->
                            activity_groups = initializeService.getStaffHealthInsuranceChargeType();
                    case PackagedServices -> activity_groups = initializeService.getPackagedServices();
                }
                if (activity_groups != null && !activity_groups.isEmpty()) {
                    var tmp = activity_groups.stream().map(g -> "CodeGroup:" + activity.getType().getValue() + ":" + g + ":" + activity.getCode()).toList();
                    keys.addAll(tmp);
                }
            }
        }

        keys.forEach(k -> logger.info(k));
        return keys;
    }

    @Override
    @WithSpan("Add Facts For CodeGroups")
    public Uni<Void> addFacts(Data data) {
        data.setCodeGroupList(new ArrayList<>());
        //Step 1: Fetch data from Redis (returns Uni<List<Map<String, String>>>)
        return redisService.getData(this.getKeys(data))
                .onItem()
                .transformToUni(fetchedData -> Uni.createFrom().voidItem().invoke(() -> {
                    for (var map : fetchedData){
                        if (!map.isEmpty()){
                            var obj =  objectMapper.convertValue(map, CodeGroup.class);
                            data.getCodeGroupList().add(obj);
                        }

                    }
                }));
    }
}
