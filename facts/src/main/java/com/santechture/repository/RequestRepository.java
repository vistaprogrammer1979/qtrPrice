package com.santechture.repository;

import com.santechture.facts.entity.PriceRequest;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class RequestRepository implements PanacheRepository<PriceRequest> {
    public PriceRequest findByPriceRequestId(UUID priceRequestId){
        return find("priceRequestId", priceRequestId).firstResult();
    }
}
