package org.apache.fineract.cn.stellarbridge.service.internal.horizonadapter;

public class StellarPaymentFailedException extends RuntimeException {
  private StellarPaymentFailedException(final String msg) {
    super(msg);
  }

  static StellarPaymentFailedException noPathExists(final String assetCode) {
    return new StellarPaymentFailedException("No path exists in the given currency: " + assetCode);
  }

  static StellarPaymentFailedException transactionFailed() {
    return new StellarPaymentFailedException(
        "Stellar Horizon server did not accept payment for unknown reason.");
  }
}
