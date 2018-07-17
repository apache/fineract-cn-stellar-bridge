package org.apache.fineract.cn.stellarbridge.service.internal.horizonadapter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.fineract.cn.stellarbridge.service.internal.federation.StellarAccountId;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.AccountResponse.Balance;

class StellarAccountHelpers {
  static String getAssetCode(final Asset asset) {
    if (asset instanceof AssetTypeCreditAlphaNum)
    {
      return ((AssetTypeCreditAlphaNum)asset).getCode();
    }
    else
    {
      return "XLM";
    }
  }

  static String getIssuer(final Asset asset) {
    if (asset instanceof AssetTypeCreditAlphaNum)
    {
      return ((AssetTypeCreditAlphaNum)asset).getIssuer().getAccountId();
    }
    else
    {
      return "stellar";
    }
  }

  static boolean balanceIsInAsset(final AccountResponse.Balance balance, final String assetCode)
  {
    if (balance.getAssetType() == null)
      return false;

    if (balance.getAssetCode() == null) {
      return assetCode.equals("XLM") && balance.getAssetType().equals("native");
    }

    return balance.getAssetCode().equals(assetCode);
  }

  static Asset getAssetOfBalance(final AccountResponse.Balance balance)
  {
    if (balance.getAssetCode() == null)
      return new AssetTypeNative();
    else
      return Asset.createNonNativeAsset(balance.getAssetCode(),
          KeyPair.fromAccountId(balance.getAssetIssuer()));
  }

  static BigDecimal stellarBalanceToBigDecimal(final String balance)
  {
    return BigDecimal.valueOf(Double.parseDouble(balance));
  }

  static String bigDecimalToStellarBalance(final BigDecimal balance)
  {
    return balance.toString();
  }


  static Asset getAsset(final String assetCode, final StellarAccountId targetIssuer) {
    return Asset.createNonNativeAsset(assetCode, KeyPair.fromAccountId(targetIssuer.getPublicKey()));
  }

  static BigDecimal remainingTrustInBalance(final AccountResponse.Balance balance)
  {
    return stellarBalanceToBigDecimal(balance.getLimit())
        .subtract(stellarBalanceToBigDecimal(balance.getBalance()));
  }

  private final AccountResponse account;

  StellarAccountHelpers(final AccountResponse account)
  {
    this.account = account;
  }

  AccountResponse get()
  {
    return account;
  }

  BigDecimal getBalanceOfAsset(final Asset asset)
  {
    return getNumericAspectOfAsset(asset,
        balance -> stellarBalanceToBigDecimal(balance.getBalance()));
  }

  BigDecimal getNumericAspectOfAsset(
      final Asset asset,
      final Function<Balance, BigDecimal> aspect)
  {
    final Optional<BigDecimal> balanceOfGivenAsset
        = Arrays.stream(account.getBalances())
        .filter(balance -> getAssetOfBalance(balance).equals(asset))
        .map(aspect)
        .max(BigDecimal::compareTo);

    //Theoretically there shouldn't be more than one balance, but if this should turn out to be
    //incorrect, we return the largest one, rather than adding them together.

    return balanceOfGivenAsset.orElse(BigDecimal.ZERO);
  }

  BigDecimal getBalance(final String assetCode) {
    final AccountResponse.Balance[] balances = account.getBalances();

    return Arrays.stream(balances)
        .filter(balance -> balanceIsInAsset(balance, assetCode))
        .map(balance -> stellarBalanceToBigDecimal(balance.getBalance()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  Set<Asset> findAssetsWithBalance(
      final BigDecimal amount,
      final String assetCode) {

    return findAssetsWithAspect(amount, assetCode,
        balance -> stellarBalanceToBigDecimal(balance.getBalance()));
  }

  Set<Asset> findAssetsWithTrust(
      final BigDecimal amount,
      final String assetCode) {

    return findAssetsWithAspect(amount, assetCode,
        StellarAccountHelpers::remainingTrustInBalance);
  }

  private Set<Asset> findAssetsWithAspect(
      final BigDecimal amount,
      final String assetCode,
      final Function<AccountResponse.Balance, BigDecimal> numericAspect)
  {
    return Arrays.stream(account.getBalances())
        .filter(balance -> balanceIsInAsset(balance, assetCode))
        .filter(balance -> numericAspect.apply(balance).compareTo(amount) >= 0)
        .sorted(Comparator.comparing(numericAspect::apply))
        .map(StellarAccountHelpers::getAssetOfBalance)
        .collect(Collectors.toSet());
  }

  Stream<Balance> getAllNonnativeBalancesStream(final String assetCode, final Asset vaultAsset)
  {
    return Arrays.stream(account.getBalances())
        .filter(balance -> balanceIsInAsset(balance, assetCode))
        .filter(balance -> !getAssetOfBalance(balance).equals(vaultAsset));
  }

  public BigDecimal getRemainingTrustInAsset(final Asset asset) {
    return getTrustInAsset(asset).subtract(getBalanceOfAsset(asset));
  }

  public BigDecimal getTrustInAsset(final Asset asset) {
    return getNumericAspectOfAsset(asset,
        balance -> stellarBalanceToBigDecimal(balance.getLimit()));
  }

  public Stream<AccountResponse.Balance> getVaultBalancesStream(final String stellarVaultAccountId) {
    return Arrays.stream(account.getBalances())
        .filter(balance -> balance.getAssetIssuer() != null)
        .filter(balance -> balance.getAssetIssuer().equals(stellarVaultAccountId))
        .filter(balance -> stellarBalanceToBigDecimal(balance.getLimit()).compareTo(BigDecimal.ZERO) != 0);
  }

  public Stream<AccountResponse.Balance> getAllNonnativeBalancesStream() {
    return Arrays.stream(account.getBalances())
        .filter(balance -> balance.getAssetIssuer() != null)
        .filter(balance -> stellarBalanceToBigDecimal(balance.getBalance()).compareTo(BigDecimal.ZERO) != 0);
  }
}
