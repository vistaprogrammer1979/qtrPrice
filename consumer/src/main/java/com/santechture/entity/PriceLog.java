package com.santechture.entity;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@RequestScoped
@Entity
@Table(name = "price_logs")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PriceLog {
//  @Id
//  private UUID priceRequestId;
  @Column(columnDefinition = "TEXT")
  private String exception;
  @Column
  private java.sql.Timestamp timestamp;
  @Column
  private String factHostname;
  @Column
  private String engineHostname;
  @Column(columnDefinition = "TEXT")
  private String errorMessage;
  @Column
  private String errorHostname;

  //  @Id
//  @OneToOne
//  @MapsId
//  @JoinColumn(name = "preice_request_id")
//  private PriceRequest priceRequest;
  @Id
  @Column(name = "preice_request_id")
  private UUID priceRequest;

  public PriceLog(UUID priceRequestId, String exception, String errorMessage) {
    this(exception, Timestamp.from(Instant.now()), null, null, errorMessage,null, priceRequestId);
  }

}
