package org.apache.fineract.cn.stellarbridge.service.internal.accounting;

import java.math.BigDecimal;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.BridgeConfigurationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountingAdapter {
  private final JournalEntryCreator journalEntryCreator;

  @Autowired
  public AccountingAdapter(
      final JournalEntryCreator journalEntryCreator) {
    this.journalEntryCreator = journalEntryCreator;
  }

  public String adjustFineractBalances(
      final BridgeConfigurationEntity bridgeConfigurationEntity,
      final BigDecimal amount,
      final String assetCode) {
    //journalEntryCreator.createJournalEntry(journalEntry);
    return null;
  }

  public void tellFineractPaymentSucceeded(String fineractStagingAccountIdentifier,
      String assetCode, BigDecimal amount)
  {

  }
}
