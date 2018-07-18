package org.apache.fineract.cn.stellarbridge.service.internal.horizonadapter;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import org.apache.fineract.cn.command.gateway.CommandGateway;
import org.apache.fineract.cn.stellarbridge.service.ServiceConstants;
import org.apache.fineract.cn.stellarbridge.service.internal.command.StellarPaymentCommand;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.BridgeConfigurationEntity;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.BridgeConfigurationRepository;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.StellarCursorEntity;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.StellarCursorRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.responses.effects.AccountCreditedEffectResponse;
import org.stellar.sdk.responses.effects.AccountDebitedEffectResponse;
import org.stellar.sdk.responses.effects.EffectResponse;

@Component
public class HorizonServerEffectsListener implements EventListener<EffectResponse> {

  private final BridgeConfigurationRepository accountBridgeRepository;
  private final StellarCursorRepository stellarCursorRepository;
  private final CommandGateway commandGateway;

  private final Logger logger;


  @Autowired
  HorizonServerEffectsListener(
      final BridgeConfigurationRepository accountBridgeRepository,
      final StellarCursorRepository stellarCursorRepository,
      final CommandGateway commandGateway,
      @Qualifier(ServiceConstants.LOGGER_NAME) final Logger logger)
  {
    this.accountBridgeRepository = accountBridgeRepository;
    this.stellarCursorRepository = stellarCursorRepository;
    this.commandGateway = commandGateway;
    this.logger = logger;
  }

  @Override public void onEvent(final EffectResponse operation) {
    final String pagingToken = operation.getPagingToken();

    //This is important, because an event can be sent twice if we are managing both the sending and
    //receiving account.  We need to be certain we process it only once.
    final StellarCursorEntity cursorPersistency = markPlace(pagingToken);
    if (cursorPersistency.getProcessed())
      return;

    logger.info("Operation with cursor {}", pagingToken);

    handleOperation(operation);

    cursorPersistency.setProcessed(true);
    stellarCursorRepository.save(cursorPersistency);
  }

  StellarCursorEntity markPlace(final String pagingToken)
  {
    synchronized (stellarCursorRepository) {
      final Optional<StellarCursorEntity> entry =
          stellarCursorRepository.findByCursor(pagingToken);

      return entry.orElse(
          stellarCursorRepository.save(new StellarCursorEntity(pagingToken, new Date())));
    }
  }

  private void handleOperation(final EffectResponse effect) {

    if (effect instanceof AccountCreditedEffectResponse)
    {
      final AccountCreditedEffectResponse accountCreditedEffect = (AccountCreditedEffectResponse) effect;
      final BridgeConfigurationEntity toAccount
          = accountBridgeRepository.findByStellarAccountIdentifier(effect.getAccount().getAccountId());
      if (toAccount == null)
        return; //Nothing to do.  Not one of ours.

      final BigDecimal amount
          = StellarAccountHelpers.stellarBalanceToBigDecimal(accountCreditedEffect.getAmount());
      final Asset asset = accountCreditedEffect.getAsset();
      final String assetCode = StellarAccountHelpers.getAssetCode(asset);
      final String issuer = StellarAccountHelpers.getIssuer(asset);

      logger.info("Credit to {} of {}, in currency {}@{}",
          toAccount.getTenantIdentifier(), amount, assetCode, issuer);

      //TODO: This will prevent lumens from being registered in the mifos account (likewise below in debit)...
      if (!(asset instanceof AssetTypeCreditAlphaNum))
        return;

      final StellarPaymentCommand receivePaymentCommand =
          new StellarPaymentCommand(toAccount.getTenantIdentifier(), assetCode, amount,
              LocalDateTime.now(Clock.systemUTC()));
      commandGateway.process(receivePaymentCommand);
    }
    else if (effect instanceof AccountDebitedEffectResponse)
    {
      final AccountDebitedEffectResponse accountDebitedEffect = (AccountDebitedEffectResponse)effect;

      final BridgeConfigurationEntity toAccount = accountBridgeRepository
          .findByStellarAccountIdentifier(accountDebitedEffect.getAccount().getAccountId());
      if (toAccount == null)
        return; //Nothing to do.  Not one of ours.

      final BigDecimal amount
          = StellarAccountHelpers.stellarBalanceToBigDecimal(accountDebitedEffect.getAmount());
      final Asset asset = accountDebitedEffect.getAsset();
      final String assetCode = StellarAccountHelpers.getAssetCode(asset);
      final String issuer = StellarAccountHelpers.getIssuer(asset);

      logger.info("Debit to {} of {}, in currency {}@{}",
          toAccount.getTenantIdentifier(), amount, assetCode, issuer);

      if (!(asset instanceof AssetTypeCreditAlphaNum))
        return;

      final StellarPaymentCommand receivePaymentCommand =
          new StellarPaymentCommand(toAccount.getTenantIdentifier(), assetCode, amount.negate(),
              LocalDateTime.now(Clock.systemUTC()));
      commandGateway.process(receivePaymentCommand);
    }
    else
    {
      logger.info("Effect of type {}", effect.getType());
    }
  }
}
