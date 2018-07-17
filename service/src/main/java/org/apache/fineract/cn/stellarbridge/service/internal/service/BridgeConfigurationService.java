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
package org.apache.fineract.cn.stellarbridge.service.internal.service;

import java.util.Optional;
import org.apache.fineract.cn.stellarbridge.api.v1.domain.BridgeConfiguration;
import org.apache.fineract.cn.stellarbridge.service.internal.mapper.BridgeConfigurationMapper;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.BridgeConfigurationEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BridgeConfigurationService {

  private final BridgeConfigurationEntityRepository bridgeConfigurationEntityRepository;

  @Autowired
  public BridgeConfigurationService(final BridgeConfigurationEntityRepository bridgeConfigurationEntityRepository) {
    super();
    this.bridgeConfigurationEntityRepository = bridgeConfigurationEntityRepository;
  }

  public Optional<BridgeConfiguration> findByTenantIdentifier(final String tenantIdentifier) {
    return this.bridgeConfigurationEntityRepository.findByTenantIdentifier(tenantIdentifier).map(BridgeConfigurationMapper::map);
  }
}
