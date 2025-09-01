package com.santechture.entity;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@RequestScoped
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

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    PriceBody priceBody = (PriceBody) o;
    return getPriceRequest() != null && Objects.equals(getPriceRequest(), priceBody.getPriceRequest());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }
}
