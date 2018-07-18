/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.cn.stellarbridge.service.internal.mapper;

import org.apache.fineract.cn.stellarbridge.api.v1.domain.BridgeConfiguration;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.BridgeConfigurationEntity;


public class BridgeConfigurationMapper {

  private BridgeConfigurationMapper() {
    super();
  }

  public static BridgeConfiguration map(final BridgeConfigurationEntity toMap) {
    final BridgeConfiguration bridgeConfiguration = new BridgeConfiguration();
    bridgeConfiguration.setFineractIncomingStagingLedgerIdentifier(toMap.getFineractIncomingLedger());
    bridgeConfiguration.setFineractOutgoingStagingLedgerIdentifier(toMap.getFineractOutgoingLedger());
    bridgeConfiguration.setFineractStellarAssetsLedgerIdentifier(toMap.getFineractStellerLedger());
    bridgeConfiguration.setStellarAccountIdentifier(toMap.getStellarAccountIdentifier());
    bridgeConfiguration.setStellarPrivateKey(toMap.getStellarAccountPrivateKey());
    return bridgeConfiguration;
  }

  public static BridgeConfigurationEntity map(final String forTenant, final BridgeConfiguration toMap) {
    final BridgeConfigurationEntity bridgeConfigurationEntity = new BridgeConfigurationEntity();
    bridgeConfigurationEntity.setTenantIdentifier(forTenant);
    bridgeConfigurationEntity.setFineractIncomingLedger(toMap.getFineractIncomingStagingLedgerIdentifier());
    bridgeConfigurationEntity.setFineractOutgoingLedger(toMap.getFineractOutgoingStagingLedgerIdentifier());
    bridgeConfigurationEntity.setFineractStellerLedger(toMap.getFineractStellarAssetsLedgerIdentifier());
    bridgeConfigurationEntity.setStellarAccountIdentifier(toMap.getStellarAccountIdentifier());
    bridgeConfigurationEntity.setStellarAccountPrivateKey(toMap.getStellarPrivateKey());
    return bridgeConfigurationEntity;
  }
}
