package org.apache.fineract.cn.stellarbridge.service.internal.federation;

public class FederationFailedException extends RuntimeException {
  private FederationFailedException(final String message) { super(message);
  }

  static FederationFailedException domainDoesNotReferToValidFederationServer
      (final String domain)
  {
    return new FederationFailedException(
        "The federation server for the given domain could not be reached: " + domain);
  }

  static FederationFailedException addressRequiresUnsupportedMemoType(final String memoType)
  {
    return new FederationFailedException(
        "The given federation address returned an unsupported memo type: " + memoType);
  }

  static FederationFailedException wrongDomain(final String domain) {
    return new FederationFailedException("Wrong domain: " + domain);
  }

  static FederationFailedException addressNameNotFound(final String address) {
    return new FederationFailedException("The address name is not found: " + address);
  }

  static FederationFailedException malformedAddress(final String address) {
    return new FederationFailedException("The address is not a valid stellar address: " + address);
  }

  static FederationFailedException needTopLevelStellarAccount
      (final String address) {
    return new FederationFailedException(
        "Need top level Stellar account: " + address);
  }
}