package com.santechture.entity;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@RequestScoped
@Entity
@Table(name = "price_activity")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PriceActivity {
  @Id
//  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID priceActivityId;
  @Column
  private String activityId;
  @Column
  private String start;
  @Column
  private String type;
  @Column
  private String code;
  @Column
  private String quantity;
  @Column
  private String net;
  @Column
  private String orderingClinician;
  @Column
  private String clinician;
  @Column
  private String priorAuthorizationId;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "price_request_id")
  private PriceRequest priceRequest;


}
