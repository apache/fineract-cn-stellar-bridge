package org.apache.fineract.cn.stellarbridge.service.internal.service;

import java.util.Optional;
import java.util.stream.Stream;
import org.apache.fineract.cn.stellarbridge.api.v1.domain.StellarCurrencyIssuer;
import org.apache.fineract.cn.stellarbridge.service.internal.mapper.StellarCurrencyIssuerMapper;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.StellarCurrencyIssuerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StellarCurrencyIssuerService {

  final private StellarCurrencyIssuerRepository stellarCurrencyIssuerRepository;

  @Autowired
  public StellarCurrencyIssuerService(
      final StellarCurrencyIssuerRepository stellarCurrencyIssuerRepository) {
    this.stellarCurrencyIssuerRepository = stellarCurrencyIssuerRepository;
  }

  public Optional<StellarCurrencyIssuer> find(final String tenantIdentifier, final String currencyCode) {
    return this.stellarCurrencyIssuerRepository
        .findByTenantIdentifierAndCurrencyCode(tenantIdentifier, currencyCode)
        .map(StellarCurrencyIssuerMapper::map);
  }

  public Stream<StellarCurrencyIssuer> findAll(final String tenantIdentifier) {
    return this.stellarCurrencyIssuerRepository.findByTenantIdentifier(tenantIdentifier)
        .map(StellarCurrencyIssuerMapper::map);
  }
}
