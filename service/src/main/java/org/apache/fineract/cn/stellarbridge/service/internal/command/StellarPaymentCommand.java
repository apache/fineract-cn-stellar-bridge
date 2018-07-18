package org.apache.fineract.cn.stellarbridge.service.internal.command;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StellarPaymentCommand {

  private final String tenantIdentifier;
  private final String assetCode;
  private final BigDecimal amount;
  private final LocalDateTime transactionDate;

  public StellarPaymentCommand(
      String tenantIdentifier,
      String assetCode,
      BigDecimal amount,
      LocalDateTime transactionDate) {
    this.tenantIdentifier = tenantIdentifier;
    this.assetCode = assetCode;
    this.amount = amount;
    this.transactionDate = transactionDate;
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

  public LocalDateTime getTransactionDate() {
    return transactionDate;
  }
}
