package com.santechture.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "price_alert")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PriceAlert {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
  @Column
  private String objectId;
  @Column
  private String objectType;
  @Column
  private UUID priceObjectId;
  @Column(columnDefinition = "TEXT")
  private String longMessage;
  @Column
  private String ruleId;
  @Column
//  @Enumerated(EnumType.STRING)
  private String severity;
  @Column(length = 500)
  private String shortMessage;
  @Column
  private java.sql.Timestamp createdAt;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "price_request_id")
  private PriceRequest priceRequest;

}
