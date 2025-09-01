package com.santechture.facts.qatar.service.client;

import com.santechture.Facility;
import com.santechture.request.Data;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class FacilityClient extends ClientBase {

    @Override
    public List<String> getKeys(Data data) {
        var claim = data.getClaim();
        return List.of(String.format("%s:%s", "Facility", claim.getProviderID()));
    }


    @WithSpan("Add Facts For Facility")
    @Override
    public Uni<Void> addFacts(Data data) {
        data.setFacilityList(new ArrayList<>());
        //Step 1: Fetch data from Redis (returns Uni<List<Map<String, String>>>)
        return redisService.getData(this.getKeys(data)).onItem().transformToUni(fetchedData -> Uni.createFrom().voidItem().invoke(() -> {
            for (var map : fetchedData) {
                if(!map.isEmpty()){
                    var obj = objectMapper.convertValue(map, Facility.class);
                    data.getFacilityList().add(obj);
                }
            }
        }));
    }
}
