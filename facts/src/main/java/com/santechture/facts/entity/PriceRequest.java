package com.santechture.facts.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.santechture.facts.entity.*;
import com.santechture.pojo.PriceMarket;
import com.santechture.pojo.StatusEnum;
import com.santechture.pojo.TypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "price_request")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PriceRequest {
  @Id
  private UUID priceRequestId;
  @Column
  @Enumerated(EnumType.STRING)
  private StatusEnum status;
  @Column
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
  private Timestamp createdAt;
  @Column
  private String errorMessage;
  @Column
  private String facility;
  @Column
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
  private Timestamp finishedAt;
  @Column
  @Enumerated(EnumType.STRING)
  private TypeEnum requestType;
  @Column
  private String traceId;
  @Column
  private String claimId;
  @Column
  private long processTime;
  @Column
  private long recall;
  @Column
  @Enumerated(EnumType.STRING)
  private PriceMarket market;
  @Basic
  @Column(name = "sub_billing_group")
  private String subBillingGroup;

  @Basic
  @Column(name = "visit_id")
  private String visitId;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "priceRequest")
  @PrimaryKeyJoinColumn
  private PriceBody body;

//  @OneToOne(cascade = CascadeType.ALL, mappedBy = "priceRequest")
//  @PrimaryKeyJoinColumn
//  private PriceLog log;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "priceRequest")
  private List<PriceAlert> alerts;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "priceRequest")
  private List<PriceActivity> activities;

  @Column
  private Double gross;
  @Column
  private Double patientShare;
  @Column
  private Double net;
  @Column
  private Integer multipleProcedures;
  @Column
  private Double primaryProc;
  @Column
  private Double secondaryProc;
  @Column
  private Double thirdProc;
  @Column
  private Double forthProc;
  @Column
  private Long cusDentalId;
  @Column
  private Long spcId;
  @Column
  private Long cusId;
  @Column
  private String memberId;


}
