package org.apache.fineract.cn.stellarbridge.service.internal.command;

import java.math.BigDecimal;

public class StellarPaymentCommand {

  private final String tenantIdentifier;
  private final String assetCode;
  private final BigDecimal amount;

  public StellarPaymentCommand(String tenantIdentifier, String assetCode, BigDecimal amount) {
    this.tenantIdentifier = tenantIdentifier;
    this.assetCode = assetCode;
    this.amount = amount;
  }

  public String getTenantIdentifier() {
    return tenantIdentifier;
  }

  public String getAssetCode() {
    return assetCode;
  }

  public BigDecimal getAmount() {
    return amount;
  }
}
