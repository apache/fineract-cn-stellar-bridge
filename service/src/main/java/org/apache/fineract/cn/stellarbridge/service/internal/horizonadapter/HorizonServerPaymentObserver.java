package org.apache.fineract.cn.stellarbridge.service.internal.horizonadapter;

import java.net.URI;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import org.apache.fineract.cn.stellarbridge.service.ServiceConstants;
import org.apache.fineract.cn.stellarbridge.service.internal.config.StellarBridgeProperties;
import org.apache.fineract.cn.stellarbridge.service.internal.federation.StellarAccountId;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.BridgeConfigurationRepository;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.StellarCursorEntity;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.StellarCursorRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Component;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.requests.EffectsRequestBuilder;

@Component
public class HorizonServerPaymentObserver {
  private final StellarBridgeProperties stellarBridgeProperties;

  private final BridgeConfigurationRepository bridgeConfigurationRepository;
  private final StellarCursorRepository stellarCursorRepository;
  private final HorizonServerEffectsListener listener;
  private final Logger logger;

  @PostConstruct
  void init()
  {
    try {
      final Optional<String> cursor = this.getCurrentCursor();

      bridgeConfigurationRepository.findAll()
          .forEach(config -> setupListeningForAccount(
              StellarAccountId.mainAccount(config.getStellarAccountIdentifier()), cursor));
    }
    catch (InvalidDataAccessResourceUsageException x) {
      //Nothing.  If the repository hasn't been provisioned yet, there are no mapped accounts to
      //listen in on.
    }
  }

  @Autowired
  HorizonServerPaymentObserver(
      final StellarBridgeProperties stellarBridgeProperties,
      final BridgeConfigurationRepository bridgeConfigurationRepository,
      final StellarCursorRepository stellarCursorRepository,
      final HorizonServerEffectsListener listener,
      @Qualifier(ServiceConstants.LOGGER_NAME) final Logger logger)
  {
    this.stellarBridgeProperties = stellarBridgeProperties;
    this.bridgeConfigurationRepository = bridgeConfigurationRepository;
    this.stellarCursorRepository = stellarCursorRepository;

    this.listener = listener;

    this.logger = logger;
  }

  public void setupListeningForAccount(final StellarAccountId stellarAccountId)
  {
    setupListeningForAccount(stellarAccountId, Optional.empty());
  }

  private Optional<String> getCurrentCursor() {
    final Optional<StellarCursorEntity> cursorPersistency
        = stellarCursorRepository.findTopByProcessedTrueOrderByCreatedOnDesc();

    return cursorPersistency.map(StellarCursorEntity::getCursor);
  }

  private void setupListeningForAccount(
      @NotNull final StellarAccountId stellarAccountId, @NotNull final Optional<String> cursor)
  {
    logger.info("HorizonServerPaymentObserver.setupListeningForAccount {}, cursor {}",
        stellarAccountId.getPublicKey(), cursor);

    final EffectsRequestBuilder effectsRequestBuilder
        = new EffectsRequestBuilder(URI.create(stellarBridgeProperties.getHorizonAddress()));
    effectsRequestBuilder.forAccount(KeyPair.fromAccountId(stellarAccountId.getPublicKey()));
    cursor.ifPresent(effectsRequestBuilder::cursor);

    effectsRequestBuilder.stream(listener);
  }

}
