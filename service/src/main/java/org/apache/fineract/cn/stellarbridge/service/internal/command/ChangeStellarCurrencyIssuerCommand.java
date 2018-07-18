package org.apache.fineract.cn.stellarbridge.service.internal.command;

import org.apache.fineract.cn.stellarbridge.api.v1.domain.StellarCurrencyIssuer;

public class ChangeStellarCurrencyIssuerCommand {

  private final String tenantIdentifier;
  private final StellarCurrencyIssuer instance;

  public ChangeStellarCurrencyIssuerCommand(String tenantIdentifier,
      StellarCurrencyIssuer instance) {
    this.tenantIdentifier = tenantIdentifier;
    this.instance = instance;
  }

  public String getTenantIdentifier() {
    return tenantIdentifier;
  }

  public StellarCurrencyIssuer getInstance() {
    return instance;
  }

  @Override
  public String toString() {
    return "ChangeStellarCurrencyIssuerCommand{" +
        "tenantIdentifier='" + tenantIdentifier + '\'' +
        ", instance=" + instance +
        '}';
  }
}
