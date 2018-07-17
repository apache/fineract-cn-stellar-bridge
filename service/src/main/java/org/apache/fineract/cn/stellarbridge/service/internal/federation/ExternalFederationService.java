package org.apache.fineract.cn.stellarbridge.service.internal.federation;

import java.io.IOException;
import org.springframework.stereotype.Service;
import org.stellar.sdk.federation.Federation;
import org.stellar.sdk.federation.FederationResponse;
import org.stellar.sdk.federation.MalformedAddressException;

@Service
public class ExternalFederationService {

  class StellarResolver
  { //To make a static function mockable.
    FederationResponse resolve(final String address)
        throws IOException, MalformedAddressException {
      return Federation.resolve(address);
    }
  }

  private StellarResolver stellarResolver;

  ExternalFederationService()
  {
    this.stellarResolver = new StellarResolver();
  }

  ExternalFederationService(final StellarResolver stellarResolver)
  {
    this.stellarResolver = stellarResolver;
  }

  /**
   * Based on the stellar address, finds the stellar account id.  Resolves the domain, and calls
   * the federation service to do so.  This only returns an account id if the memo type is id or
   * there is no memo type.
   *
   * @param stellarAddress The stellar address for which to return a stellar account id.
   * @return The corresponding stellar account id.
   *
   * @throws FederationFailedException for the following cases:
   * * domain server not reachable,
   * * stellar.toml not parseable for federation server,
   * * federation server not reachable,
   * * federation server response does not match expected format.
   * * memo type is not id.
   */
  public StellarAccountId getAccountId(final StellarAddress stellarAddress)
      throws FederationFailedException
  {
    final org.stellar.sdk.federation.FederationResponse federationResponse;
    try {
      federationResponse = stellarResolver.resolve(stellarAddress.toString());
    }
    catch (final MalformedAddressException e)
    {
      throw FederationFailedException.malformedAddress(stellarAddress.toString());
    }
    catch (final IOException e)
    {
      throw FederationFailedException
          .domainDoesNotReferToValidFederationServer(stellarAddress.getDomain().toString());
    }

    if (federationResponse == null)
    {
      throw FederationFailedException.addressNameNotFound(stellarAddress.toString());
    }
    if (federationResponse.getAccountId() == null)
    {
      throw FederationFailedException.addressNameNotFound(stellarAddress.toString());
    }

    return convertFederationResponseToStellarAddress(federationResponse);
  }

  private StellarAccountId convertFederationResponseToStellarAddress(
      final org.stellar.sdk.federation.FederationResponse response)
  {
    if (response.getMemoType().equalsIgnoreCase("text"))
    {
      return StellarAccountId.subAccount(response.getAccountId(), response.getMemo());
    }
    else if (response.getMemoType() == null || response.getMemoType().isEmpty())
    {
      return StellarAccountId.mainAccount(response.getAccountId());
    }
    else
    {
      throw FederationFailedException.addressRequiresUnsupportedMemoType(response.getMemoType());
    }
  }
}
