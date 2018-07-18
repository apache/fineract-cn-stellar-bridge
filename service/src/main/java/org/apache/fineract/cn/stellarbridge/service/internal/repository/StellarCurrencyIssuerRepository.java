package org.apache.fineract.cn.stellarbridge.service.internal.repository;

import java.util.Optional;
import java.util.stream.Stream;
import org.apache.fineract.cn.stellarbridge.api.v1.domain.StellarCurrencyIssuer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StellarCurrencyIssuerRepository extends
    JpaRepository<StellarCurrencyIssuerEntity, Long>
{

  Optional<StellarCurrencyIssuerEntity> findByTenantIdentifierAndCurrencyCode(String tenantIdentifier, String currencyCode);

  Stream<StellarCurrencyIssuerEntity> findByTenantIdentifier(String tenantIdentifier);
}
