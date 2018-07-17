package org.apache.fineract.cn.stellarbridge.service.internal.command;

import java.math.BigDecimal;

public class FineractPaymentCommand {

  final private String tenantIdentifer;
  final private String transactionIdentifier;
  final private String targetAccount;
  final private String sinkDomain;
  final private BigDecimal amount;
  final private String assetCode;

  public FineractPaymentCommand(
      String tenantIdentifer,
      String transactionIdentifier,
      String targetAccount,
      String sinkDomain,
      BigDecimal amount,
      String assetCode) {
    this.tenantIdentifer = tenantIdentifer;
    this.transactionIdentifier = transactionIdentifier;
    this.targetAccount = targetAccount;
    this.sinkDomain = sinkDomain;
    this.amount = amount;
    this.assetCode = assetCode;
  }

  public String getTenantIdentifier() {
    return tenantIdentifer;
  }

  public String getTransactionIdentifier() {
    return transactionIdentifier;
  }

  public String getTargetAccount() {
    return targetAccount;
  }

  public String getSinkDomain() {
    return sinkDomain;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getAssetCode() {
    return assetCode;
  }
}
