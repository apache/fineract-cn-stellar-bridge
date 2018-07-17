package org.apache.fineract.cn.stellarbridge.service.internal.federation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StellarAddressResolver {
  private final ExternalFederationService externalFederationService;

  @Autowired
  public StellarAddressResolver(
      final ExternalFederationService externalFederationService) {
    this.externalFederationService = externalFederationService;
  }

  public StellarAccountId getAccountIdOfStellarAccount(final StellarAddress stellarAddress)
      throws FederationFailedException
  {
      return externalFederationService.getAccountId(stellarAddress);
  }
}
