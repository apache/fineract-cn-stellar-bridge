package org.apache.fineract.cn.stellarbridge.service.internal.horizonadapter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import org.apache.fineract.cn.stellarbridge.service.ServiceConstants;
import org.apache.fineract.cn.stellarbridge.service.internal.config.StellarBridgeProperties;
import org.apache.fineract.cn.stellarbridge.service.internal.federation.StellarAccountId;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.stellar.sdk.Account;
import org.stellar.sdk.Asset;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Memo;
import org.stellar.sdk.PathPaymentOperation;
import org.stellar.sdk.Server;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.PathResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;

@Component
public class HorizonServerUtilities {
  private final StellarBridgeProperties stellarBridgeProperties;
  private final Logger logger;

  private Server server;

  private final LoadingCache<String, Account> accounts;

  @Autowired
  HorizonServerUtilities(
      final StellarBridgeProperties stellarBridgeProperties,
      @Qualifier(ServiceConstants.LOGGER_NAME) final Logger logger)
  {
    this.stellarBridgeProperties = stellarBridgeProperties;
    this.logger = logger;

    accounts = CacheBuilder.newBuilder().build(
        new CacheLoader<String, Account>() {
          public Account load(final String accountId)
              throws InvalidConfigurationException {
            final KeyPair accountKeyPair = KeyPair.fromAccountId(accountId);
            final StellarAccountHelpers accountHelper = getAccount(accountKeyPair);
            final Long sequenceNumber = accountHelper.get().getSequenceNumber();
            return new Account(accountKeyPair, sequenceNumber);
          }
        });
  }

  @PostConstruct
  void init()
  {
    server = new Server(stellarBridgeProperties.getHorizonAddress());
  }

  public void simplePay(
      final StellarAccountId targetAccountId,
      final BigDecimal amount,
      final String assetCode,
      final StellarAccountId issuingAccountId,
      final char[] stellarAccountPrivateKey)
      throws InvalidConfigurationException, StellarPaymentFailedException
  {
    logger.info("HorizonServerUtilities.simplePay");
    final Asset asset = StellarAccountHelpers.getAsset(assetCode, issuingAccountId);

    pay(targetAccountId, amount, asset, asset, stellarAccountPrivateKey);
  }

  private void pay(
      final StellarAccountId targetAccountId,
      final BigDecimal amount,
      final Asset sendAsset,
      final Asset receiveAsset,
      final char[] stellarAccountPrivateKey)
      throws InvalidConfigurationException, StellarPaymentFailedException
  {
    final KeyPair sourceAccountKeyPair = KeyPair.fromSecretSeed(stellarAccountPrivateKey);
    final KeyPair targetAccountKeyPair = KeyPair.fromAccountId(targetAccountId.getPublicKey());

    final Account sourceAccount = accounts.getUnchecked(sourceAccountKeyPair.getAccountId());

    final Transaction.Builder transferTransactionBuilder
        = new Transaction.Builder(sourceAccount);
    final PathPaymentOperation paymentOperation =
        new PathPaymentOperation.Builder(
            sendAsset,
            StellarAccountHelpers.bigDecimalToStellarBalance(amount),
            targetAccountKeyPair,
            receiveAsset,
            StellarAccountHelpers.bigDecimalToStellarBalance(amount))
            .setSourceAccount(sourceAccountKeyPair).build();

    transferTransactionBuilder.addOperation(paymentOperation);

    if (targetAccountId.getSubAccount().isPresent())
    {
      final Memo subAccountMemo = Memo.text(targetAccountId.getSubAccount().get());
      transferTransactionBuilder.addMemo(subAccountMemo);
    }

    submitTransaction(sourceAccount, transferTransactionBuilder, sourceAccountKeyPair,
        StellarPaymentFailedException::transactionFailed);
  }

  public BigDecimal getBalance(
      final StellarAccountId stellarAccountId,
      final String assetCode)
  {
    logger.info("HorizonServerUtilities.getBalance");
    return getAccount(KeyPair.fromAccountId(stellarAccountId.getPublicKey())).getBalance(assetCode);
  }

  public BigDecimal getBalanceByIssuer(
      final StellarAccountId stellarAccountId,
      final String assetCode,
      final StellarAccountId accountIdOfIssuingStellarAddress)
      throws InvalidConfigurationException
  {
    logger.info("HorizonServerUtilities.getBalanceByIssuer");

    final Asset asset = StellarAccountHelpers.getAsset(assetCode, accountIdOfIssuingStellarAddress);

    return getAccount(KeyPair.fromAccountId(stellarAccountId.getPublicKey()))
        .getBalanceOfAsset(asset);
  }

  private StellarAccountHelpers getAccount(final KeyPair installationAccountKeyPair)
      throws InvalidConfigurationException
  {
    final AccountResponse installationAccount;
    try {
      installationAccount = server.accounts().account(installationAccountKeyPair);
    }
    catch (final IOException e) {
      throw InvalidConfigurationException.unreachableStellarServerAddress(stellarBridgeProperties.getHorizonAddress());
    }

    if (installationAccount == null)
    {
      throw InvalidConfigurationException.invalidInstallationAccountSecretSeed();
    }

    return new StellarAccountHelpers(installationAccount);
  }


  public void findPathPay(
      final StellarAccountId targetAccountId,
      final BigDecimal amount,
      final String assetCode,
      final char[] stellarAccountPrivateKey)
      throws InvalidConfigurationException, StellarPaymentFailedException
  {
    logger.info("HorizonServerUtilities.findPathPay");
    final KeyPair sourceAccountKeyPair = KeyPair.fromSecretSeed(stellarAccountPrivateKey);
    final KeyPair targetAccountKeyPair = KeyPair.fromAccountId(targetAccountId.getPublicKey());

    final StellarAccountHelpers sourceAccount = getAccount(sourceAccountKeyPair);
    final StellarAccountHelpers targetAccount = getAccount(targetAccountKeyPair);

    final Set<Asset> targetAssets = targetAccount.findAssetsWithTrust(amount, assetCode);
    final Set<Asset> sourceAssets = sourceAccount.findAssetsWithBalance(amount, assetCode);

    final Optional<MatchingAssetPair> assetPair = findAnyMatchingAssetPair(
        amount, sourceAssets, targetAssets, sourceAccountKeyPair, targetAccountKeyPair);
    if (!assetPair.isPresent())
      throw StellarPaymentFailedException.noPathExists(assetCode);

    pay(targetAccountId, amount,
        assetPair.get().asset1, assetPair.get().asset2,
        stellarAccountPrivateKey);
  }

  static class MatchingAssetPair {
    final Asset asset1;
    final Asset asset2;

    MatchingAssetPair(Asset asset1, Asset asset2) {
      this.asset1 = asset1;
      this.asset2 = asset2;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      MatchingAssetPair that = (MatchingAssetPair) o;
      return Objects.equals(asset1, that.asset1) &&
          Objects.equals(asset2, that.asset2);
    }

    @Override
    public int hashCode() {

      return Objects.hash(asset1, asset2);
    }
  }

  private Optional<MatchingAssetPair> findAnyMatchingAssetPair(
      final BigDecimal amount,
      final Set<Asset> sourceAssets,
      final Set<Asset> targetAssets,
      final KeyPair sourceAccountKeyPair,
      final KeyPair targetAccountKeyPair) {
    if (sourceAssets.isEmpty())
      return Optional.empty();

    for (final Asset targetAsset : targetAssets) {
      Page<PathResponse> paths;
      try {
        paths = server.paths()
            .sourceAccount(sourceAccountKeyPair)
            .destinationAccount(targetAccountKeyPair)
            .destinationAsset(targetAsset)
            .destinationAmount(StellarAccountHelpers.bigDecimalToStellarBalance(amount))
            .execute();
      } catch (final IOException e) {
        return Optional.empty();
      }

      while (paths != null && paths.getRecords() != null) {
        for (final PathResponse path : paths.getRecords())
        {
          if (StellarAccountHelpers.stellarBalanceToBigDecimal(path.getSourceAmount()).compareTo(amount) <= 0)
          {
            if (sourceAssets.contains(path.getSourceAsset()))
            {
              return Optional.of(new MatchingAssetPair(path.getSourceAsset(), targetAsset));
            }
          }
        }

        try {
          paths = ((paths.getLinks() == null) || (paths.getLinks().getNext() == null)) ?
              null : paths.getNextPage();
        } catch (final URISyntaxException | IOException e) {
          return Optional.empty();
        }
      }
    }

    return Optional.empty();
  }

  private <T extends Exception> void submitTransaction(
      final Account transactionSubmitter,
      final Transaction.Builder transactionBuilder,
      final KeyPair signingKeyPair,
      final Supplier<T> failureHandler)
      throws T
  {
    try {
      //final Long sequenceNumberSubmitted = account.getSequenceNumber();

      //noinspection SynchronizationOnLocalVariableOrMethodParameter
      synchronized (transactionSubmitter) {
        final Transaction transaction = transactionBuilder.build();
        transaction.sign(signingKeyPair);
        final SubmitTransactionResponse transactionResponse = server.submitTransaction(transaction);
        if (!transactionResponse.isSuccess()) {
          if (transactionResponse.getExtras() != null) {
            logger.info("Stellar transaction failed, request: {}", transactionResponse.getExtras().getEnvelopeXdr());
            logger.info("Stellar transaction failed, response: {}", transactionResponse.getExtras().getResultXdr());
          }
          else
          {
            logger.info("Stellar transaction failed.  No extra information available.");
          }
          //TODO: resend transaction if you get a bad sequence.
              /*Thread.sleep(6000); //Wait for ledger to close.
              Long sequenceNumberShouldHaveBeen =
                  server.accounts().account(account.getKeypair()).getSequenceNumber();
              if (sequenceNumberSubmitted != sequenceNumberShouldHaveBeen) {
                logger.info("Sequence number submitted: {}, Sequence number should have been: {}",
                    sequenceNumberSubmitted, sequenceNumberShouldHaveBeen);
              }*/
          throw failureHandler.get();
        }
      }
    } catch (final IOException e) {
      throw InvalidConfigurationException.unreachableStellarServerAddress(stellarBridgeProperties.getHorizonAddress());
    }
  }
}
