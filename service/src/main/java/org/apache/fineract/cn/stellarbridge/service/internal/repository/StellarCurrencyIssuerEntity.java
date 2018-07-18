package org.apache.fineract.cn.stellarbridge.service.internal.repository;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("unused")
@Entity
@Table(name = "nenet_currency_issuer")
public class StellarCurrencyIssuerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "tenant_identifier")
  private String tenantIdentifier;
  @Column(name = "currency_code")
  private String currencyCode;
  @Column(name = "stellar_issuer")
  private String stellarIssuer;

  public StellarCurrencyIssuerEntity() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTenantIdentifier() {
    return tenantIdentifier;
  }

  public void setTenantIdentifier(String tenantIdentifier) {
    this.tenantIdentifier = tenantIdentifier;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public String getStellarIssuer() {
    return stellarIssuer;
  }

  public void setStellarIssuer(String stellarIssuer) {
    this.stellarIssuer = stellarIssuer;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StellarCurrencyIssuerEntity that = (StellarCurrencyIssuerEntity) o;
    return Objects.equals(tenantIdentifier, that.tenantIdentifier) &&
        Objects.equals(currencyCode, that.currencyCode);
  }

  @Override
  public int hashCode() {

    return Objects.hash(tenantIdentifier, currencyCode);
  }
}
