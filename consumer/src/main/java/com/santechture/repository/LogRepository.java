package com.santechture.repository;

import com.santechture.entity.PriceLog;
import com.santechture.entity.PriceRequest;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
public class LogRepository implements PanacheRepository<PriceLog> {
    final static Logger logger = LoggerFactory.getLogger(LogRepository.class);
    public boolean save(PriceLog priceLog){
        try {
             persist(priceLog);
             return true;
        }catch (Exception e){
            logger.error("Couldn't save PriceLog: {}", e.getMessage());
            return false;
        }
    }
}
