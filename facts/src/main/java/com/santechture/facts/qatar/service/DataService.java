package com.santechture.facts.qatar.service;

import com.santechture.facts.qatar.service.client.*;
import com.santechture.request.Data;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.mutiny.Multi;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Singleton
public class DataService {
    @Inject
    ContractsClient contractsClient;
    @Inject
    CusPriceListClient cusPriceListClient;
    @Inject
    CusPriceListItemClient cusPriceListItemClient;

    @Inject
    FacilityClient facilityClient;

    @Inject
    CusRadiology_ACHI_PriceListItemClient cusRadiologyAchiPriceListItemClient;
    @Inject
    CusDentistryPriceListItemClient cusDentistryPriceListItemClient;
    @Inject
    CusEmergencyPriceListItemClient cusEmergencyPriceListItemClient;
    @Inject
    CusIP_ARDRG_PriceListItemClient cusIPArdrgPriceListItemClient;
    @Inject
    CusOP_QOCS_PriceListItemClient cusOPQocsPriceListItemClient;
    @Inject
    CusIP_SUB_PriceListItemClient cusIPSubPriceListItemClient;
    @Inject
    MedicinesPriceListItemClient medicinesPriceListItemClient;

    List<ClientBase> clients;
    List<ClientBase> primaryClients;

    @PostConstruct
    public void init() {
        primaryClients = List.of(contractsClient);
        clients = List.of(

                cusPriceListClient,
                cusPriceListItemClient,
                cusRadiologyAchiPriceListItemClient,
                cusDentistryPriceListItemClient,
                cusEmergencyPriceListItemClient,
                cusIPArdrgPriceListItemClient,
                cusOPQocsPriceListItemClient,
                cusIPSubPriceListItemClient,
                facilityClient,
                medicinesPriceListItemClient

        );
    }
    @WithSpan("Add facts to the request")
    public void addData(Data data) {

        // Create a reactive stream from the primaryClients list
        Multi.createFrom().iterable(primaryClients)
                .onItem().transformToUniAndMerge(client -> client.addFacts(data)) // Process in parallel
                .collect().last() // Wait for all operations to complete
                .await().indefinitely(); // Block the current thread until the pipeline completes
        // Create a reactive stream from the clients list
        Multi.createFrom().iterable(clients)
                .onItem().transformToUniAndMerge(client -> client.addFacts(data)) // Process in parallel
                .collect().last() // Wait for all operations to complete
                .await().indefinitely(); // Block the current thread until the pipeline completes

    }

    @WithSpan("Add facts to the request")
    public void addDataParallel(Data data) {
        contractsClient.addFacts(data);
        facilityClient.addFacts(data);
//        clients.forEach(client -> client.addFacts(data));
        List<CompletableFuture<Void>> futures = clients.stream().map(client -> CompletableFuture.runAsync(() -> client.addFacts(data))).toList();

//         Wait for all futures to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.join(); // Optional: Block until all are done
    }

}
