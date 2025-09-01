package com.santechture.price.entity;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@RequestScoped
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PriceLog {
  private String exception;
  private Timestamp timestamp;
  private String factHostname;
  private String engineHostname;
  private String errorMessage;
  private String errorHostname;

  private UUID priceRequest;

  public PriceLog(UUID priceRequestId, String exception, String errorMessage) {
    this(exception, Timestamp.from(Instant.now()), null, null, errorMessage,null, priceRequestId);
  }

}
