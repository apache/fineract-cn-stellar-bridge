package org.apache.fineract.cn.stellarbridge.service.internal.horizonadapter;

public class InvalidConfigurationException extends RuntimeException {
  private InvalidConfigurationException(final String message)
  {
    super(message);
  }

  static InvalidConfigurationException invalidInstallationAccountSecretSeed() {
    return new InvalidConfigurationException(
        "Invalid installation account secret seed.  Have your admin check configuration.");
  }

  static InvalidConfigurationException unreachableStellarServerAddress(
      final String serverAddress) {
    return new InvalidConfigurationException(
        "Unreachable stellar server address: " + serverAddress +
            ". Have your admin check configuration.");
  }
}
