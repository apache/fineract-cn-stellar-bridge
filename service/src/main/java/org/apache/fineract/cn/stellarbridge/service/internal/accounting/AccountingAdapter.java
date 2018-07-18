package org.apache.fineract.cn.stellarbridge.service.internal.accounting;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.fineract.cn.accounting.api.v1.client.JournalEntryAlreadyExistsException;
import org.apache.fineract.cn.accounting.api.v1.domain.Creditor;
import org.apache.fineract.cn.accounting.api.v1.domain.Debtor;
import org.apache.fineract.cn.accounting.api.v1.domain.JournalEntry;
import org.apache.fineract.cn.lang.DateConverter;
import org.apache.fineract.cn.stellarbridge.service.internal.config.StellarBridgeProperties;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.BridgeConfigurationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountingAdapter {
  private final JournalEntryCreator journalEntryCreator;
  private final StellarBridgeProperties stellarBridgeProperties;

  @Autowired
  public AccountingAdapter(
      final JournalEntryCreator journalEntryCreator,
      StellarBridgeProperties stellarBridgeProperties) {
    this.journalEntryCreator = journalEntryCreator;
    this.stellarBridgeProperties = stellarBridgeProperties;
  }

  public String acceptIncomingPayment(
      final BridgeConfigurationEntity bridgeConfigurationEntity,
      final BigDecimal amount,
      final String assetCode,
      final LocalDateTime transactionDate) throws InterruptedException {
    final JournalEntry journalEntryFundsAcceptance = new JournalEntry();
    final Creditor creditor = new Creditor(
        bridgeConfigurationEntity.getFineractStellerLedger() + "." + assetCode,
        amount.toString());
    final Debtor debtor = new Debtor(
        bridgeConfigurationEntity.getFineractIncomingLedger() + "." + assetCode,
        amount.toString());

    journalEntryFundsAcceptance.setClerk(stellarBridgeProperties.getUser());
    journalEntryFundsAcceptance.setCreditors(Collections.singleton(creditor));
    journalEntryFundsAcceptance.setDebtors(Collections.singleton(debtor));
    journalEntryFundsAcceptance.setTransactionDate(DateConverter.toIsoString(transactionDate));
    journalEntryFundsAcceptance.setTransactionType("BCHQ");

    final String transactionId = createWithUniqueTransactionIdentifier(journalEntryFundsAcceptance);

    Thread.sleep(2000); //TODO: replace with a wait on the journal entry creation.

    final JournalEntry journalEntryFundsForwarding = new JournalEntry();
    final Creditor fundsForwardingCreditor = new Creditor(
        bridgeConfigurationEntity.getFineractStellerLedger() + "." + assetCode,
        amount.toString());
    final Debtor fundsForwardingDebtor = new Debtor(
        bridgeConfigurationEntity.getFineractIncomingLedger() + "." + assetCode,
        amount.toString());

    journalEntryFundsForwarding.setClerk(stellarBridgeProperties.getUser());
    journalEntryFundsForwarding.setCreditors(Collections.singleton(fundsForwardingCreditor));
    journalEntryFundsForwarding.setDebtors(Collections.singleton(fundsForwardingDebtor));
    journalEntryFundsForwarding.setTransactionDate(DateConverter.toIsoString(transactionDate));
    journalEntryFundsForwarding.setTransactionType("ICCT");

    createWithUniqueTransactionIdentifier(journalEntryFundsForwarding);


    return transactionId;
  }

  private String createWithUniqueTransactionIdentifier(final JournalEntry journalEntry) {
    while (true) {
      try {
        final String transactionUniqueifier = RandomStringUtils.random(26, true, true);
        journalEntry.setTransactionIdentifier(formulateTransactionIdentifier(transactionUniqueifier));
        journalEntryCreator.createJournalEntry(journalEntry);
        return transactionUniqueifier;
      } catch (final JournalEntryAlreadyExistsException ignore) {
        //Try again with a new uniqueifier.
      }
    }
  }

  private static String formulateTransactionIdentifier(
      final String transactionUniqueifier) {
    return "stellarbridge." + transactionUniqueifier;
  }

  public void tellFineractPaymentSucceeded(
      final String fineractOutgoingLedger,
      final String fineractStellerLedger,
      final String assetCode,
      final BigDecimal amount)
  {
//TODO:
  }
}
