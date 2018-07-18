package org.apache.fineract.cn.stellarbridge.service.internal.command.handler;

import java.util.Optional;
import org.apache.fineract.cn.command.annotation.Aggregate;
import org.apache.fineract.cn.command.annotation.CommandHandler;
import org.apache.fineract.cn.command.annotation.CommandLogLevel;
import org.apache.fineract.cn.stellarbridge.api.v1.events.EventConstants;
import org.apache.fineract.cn.stellarbridge.service.internal.accounting.AccountingAdapter;
import org.apache.fineract.cn.stellarbridge.service.internal.command.FineractPaymentCommand;
import org.apache.fineract.cn.stellarbridge.service.internal.federation.StellarAccountId;
import org.apache.fineract.cn.stellarbridge.service.internal.federation.StellarAddress;
import org.apache.fineract.cn.stellarbridge.service.internal.federation.StellarAddressResolver;
import org.apache.fineract.cn.stellarbridge.service.internal.horizonadapter.HorizonServerUtilities;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.BridgeConfigurationEntity;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.BridgeConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Aggregate
public class FineractPaymentCommandHandler {
  private final BridgeConfigurationRepository bridgeConfigurationRepository;
  private final StellarAddressResolver stellarAddressResolver;
  private final HorizonServerUtilities horizonServerUtilities;
  private final AccountingAdapter accountingAdapter;
  private final EventHelper eventHelper;

  @Autowired
  public FineractPaymentCommandHandler(
      final BridgeConfigurationRepository bridgeConfigurationRepository,
      final StellarAddressResolver stellarAddressResolver,
      final HorizonServerUtilities horizonServerUtilities,
      AccountingAdapter accountingAdapter,
      final EventHelper eventHelper) {
    this.bridgeConfigurationRepository = bridgeConfigurationRepository;
    this.stellarAddressResolver = stellarAddressResolver;
    this.horizonServerUtilities = horizonServerUtilities;
    this.accountingAdapter = accountingAdapter;
    this.eventHelper = eventHelper;
  }

  @CommandHandler(logStart = CommandLogLevel.INFO, logFinish = CommandLogLevel.INFO)
  @Transactional
  public void handle(final FineractPaymentCommand command) {

    final Optional<BridgeConfigurationEntity> accountBridge =
        bridgeConfigurationRepository.findByTenantIdentifier(command.getTenantIdentifier());

    accountBridge.ifPresent(x -> pay(command, x));
  }

  private void pay(
      final FineractPaymentCommand command,
      final BridgeConfigurationEntity bridgeConfigurationEntity)
  {
    final StellarAccountId targetAccountId;
    targetAccountId =  stellarAddressResolver.getAccountIdOfStellarAccount(
        StellarAddress.forTenant(command.getTargetAccount(), command.getSinkDomain()));

    final char[] decodedStellarPrivateKey =
        bridgeConfigurationEntity.getStellarAccountPrivateKey().toCharArray();

    horizonServerUtilities.findPathPay(
        targetAccountId,
        command.getAmount(), command.getAssetCode(),
        decodedStellarPrivateKey);

    accountingAdapter.tellFineractPaymentSucceeded(
        bridgeConfigurationEntity.getFineractOutgoingLedger(),
        bridgeConfigurationEntity.getFineractStellerLedger(),
        command.getAssetCode(),
        command.getAmount());

    eventHelper.sendEvent(EventConstants.FINERACT_PAYMENT_PROCESSED, command.getTenantIdentifier(), command.getTransactionIdentifier());
  }
}
