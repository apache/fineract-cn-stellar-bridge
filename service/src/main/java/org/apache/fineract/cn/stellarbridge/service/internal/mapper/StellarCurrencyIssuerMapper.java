package org.apache.fineract.cn.stellarbridge.service.internal.mapper;

import org.apache.fineract.cn.stellarbridge.api.v1.domain.StellarCurrencyIssuer;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.StellarCurrencyIssuerEntity;

public class StellarCurrencyIssuerMapper {
  private StellarCurrencyIssuerMapper() {
    super();
  }

  public static StellarCurrencyIssuer map(final StellarCurrencyIssuerEntity toMap) {
    final StellarCurrencyIssuer ret = new StellarCurrencyIssuer();
    ret.setCurrencyCode(toMap.getCurrencyCode());
    ret.setStellarIssuer(toMap.getStellarIssuer());
    return ret;
  }

  public static StellarCurrencyIssuerEntity map(final String tenantIdentifier, final StellarCurrencyIssuer toMap) {
    final StellarCurrencyIssuerEntity ret = new StellarCurrencyIssuerEntity();
    ret.setTenantIdentifier(tenantIdentifier);
    ret.setCurrencyCode(toMap.getCurrencyCode());
    ret.setStellarIssuer(toMap.getStellarIssuer());
    return ret;
  }

}
