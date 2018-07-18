package org.apache.fineract.cn.stellarbridge.api.v1.domain;

import java.util.Objects;
import org.apache.fineract.cn.lang.validation.constraints.ValidIdentifier;

public class StellarCurrencyIssuer {
  @ValidIdentifier
  String currencyCode;

  String stellarIssuer;

  public StellarCurrencyIssuer() {
  }

  public StellarCurrencyIssuer(String currencyCode, String stellarIssuer) {
    this.currencyCode = currencyCode;
    this.stellarIssuer = stellarIssuer;
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
    StellarCurrencyIssuer that = (StellarCurrencyIssuer) o;
    return Objects.equals(currencyCode, that.currencyCode) &&
        Objects.equals(stellarIssuer, that.stellarIssuer);
  }

  @Override
  public int hashCode() {

    return Objects.hash(currencyCode, stellarIssuer);
  }

  @Override
  public String toString() {
    return "StellarCurrencyIssuer{" +
        "currencyCode='" + currencyCode + '\'' +
        ", stellarIssuer='" + stellarIssuer + '\'' +
        '}';
  }
}
