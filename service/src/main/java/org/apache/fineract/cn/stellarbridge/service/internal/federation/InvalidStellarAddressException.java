package org.apache.fineract.cn.stellarbridge.service.internal.federation;

public class InvalidStellarAddressException extends RuntimeException {
  private InvalidStellarAddressException(final String message) {
    super(message);
  }

  static InvalidStellarAddressException invalidDomainName(final String domainName) {
    return new InvalidStellarAddressException("Domain name is not valid: " + domainName);
  }

  static InvalidStellarAddressException nonConformantStellarAddress(final String address) {
    return new InvalidStellarAddressException(
        "Non-conformant stellar address: " + address
    );
  }
}
