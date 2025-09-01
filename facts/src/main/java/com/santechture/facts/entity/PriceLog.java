package com.santechture.facts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

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
  @Column
  private String exception;
  @Column
  private Timestamp timestamp;
  @Column
  private String factHostname;
  @Column
  private String engineHostname;
  @Column
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
