package org.apache.fineract.cn.stellarbridge.service.internal.command.handler;

import java.util.Optional;
import org.apache.fineract.cn.command.annotation.Aggregate;
import org.apache.fineract.cn.command.annotation.CommandHandler;
import org.apache.fineract.cn.command.annotation.CommandLogLevel;
import org.apache.fineract.cn.stellarbridge.api.v1.events.EventConstants;
import org.apache.fineract.cn.stellarbridge.service.internal.accounting.AccountingAdapter;
import org.apache.fineract.cn.stellarbridge.service.internal.command.StellarPaymentCommand;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.BridgeConfigurationEntity;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.BridgeConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Aggregate
public class StellarPaymentCommandHandler {
  private final BridgeConfigurationRepository bridgeConfigurationRepository;
  private final AccountingAdapter accountingAdapter;
  private final EventHelper eventHelper;

  @Autowired
  public StellarPaymentCommandHandler(
      final BridgeConfigurationRepository bridgeConfigurationRepository,
      final AccountingAdapter accountingAdapter,
      final EventHelper eventHelper) {
    this.bridgeConfigurationRepository = bridgeConfigurationRepository;
    this.accountingAdapter = accountingAdapter;
    this.eventHelper = eventHelper;
  }


  @CommandHandler(logStart = CommandLogLevel.INFO, logFinish = CommandLogLevel.INFO)
  @Transactional
  public void handle(final StellarPaymentCommand command) throws InterruptedException {

    final Optional<BridgeConfigurationEntity> accountBridge =
        bridgeConfigurationRepository.findByTenantIdentifier(command.getTenantIdentifier());

    if (accountBridge.isPresent())
    {
      final String transactionIdentifier = accountingAdapter.acceptIncomingPayment(
          accountBridge.get(), command.getAmount(), command.getAssetCode(),
          command.getTransactionDate());

      eventHelper.sendEvent(
          EventConstants.STELLAR_PAYMENT_PROCESSED,
          command.getTenantIdentifier(),
          transactionIdentifier);
    }
  }

}
