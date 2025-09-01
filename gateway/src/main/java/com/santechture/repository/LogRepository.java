package com.santechture.repository;

import com.santechture.entity.PriceLog;
import com.santechture.entity.PriceRequest;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogRepository implements PanacheRepository<PriceLog> {
    public PriceRequest save(PriceLog priceLog){
        return save(priceLog);
    }
}
