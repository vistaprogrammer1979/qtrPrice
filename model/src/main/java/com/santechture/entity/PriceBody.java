package com.santechture.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "price_body")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PriceBody {
  @Id
  @OneToOne
  @MapsId
  @JoinColumn(name = "price_request_id")
  private PriceRequest priceRequest;

  @Column(columnDefinition = "TEXT")
  private String request;
  @Column(columnDefinition = "TEXT")
  private String response;
  @Column(columnDefinition = "TEXT")
  private String facts;


}
