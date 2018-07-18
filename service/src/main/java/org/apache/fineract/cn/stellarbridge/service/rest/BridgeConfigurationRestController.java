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
package org.apache.fineract.cn.stellarbridge.service.rest;

import static org.apache.fineract.cn.lang.config.TenantHeaderFilter.TENANT_HEADER;

import javax.validation.Valid;
import org.apache.fineract.cn.anubis.annotation.AcceptedTokenType;
import org.apache.fineract.cn.anubis.annotation.Permittable;
import org.apache.fineract.cn.command.gateway.CommandGateway;
import org.apache.fineract.cn.lang.ServiceException;
import org.apache.fineract.cn.stellarbridge.api.v1.PermittableGroupIds;
import org.apache.fineract.cn.stellarbridge.api.v1.domain.BridgeConfiguration;
import org.apache.fineract.cn.stellarbridge.service.internal.command.ChangeConfigurationCommand;
import org.apache.fineract.cn.stellarbridge.service.internal.command.InitializeServiceCommand;
import org.apache.fineract.cn.stellarbridge.service.internal.service.BridgeConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/")
public class BridgeConfigurationRestController {

  private final CommandGateway commandGateway;
  private final BridgeConfigurationService bridgeConfigurationService;

  @Autowired
  public BridgeConfigurationRestController(
      final CommandGateway commandGateway,
      final BridgeConfigurationService bridgeConfigurationService) {
    super();
    this.commandGateway = commandGateway;
    this.bridgeConfigurationService = bridgeConfigurationService;
  }

  @Permittable(value = AcceptedTokenType.SYSTEM)
  @RequestMapping(
      value = "/initialize",
      method = RequestMethod.POST,
      consumes = MediaType.ALL_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public
  @ResponseBody
  ResponseEntity<Void> initialize() {
      this.commandGateway.process(new InitializeServiceCommand());
      return ResponseEntity.accepted().build();
  }

  @Permittable(value = AcceptedTokenType.TENANT, groupId = PermittableGroupIds.CONFIGURATION_MANAGEMENT)
  @RequestMapping(
          value = "/config",
          method = RequestMethod.GET,
          consumes = MediaType.ALL_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE
  )
  public
  @ResponseBody
  ResponseEntity<BridgeConfiguration> getBridgeConfiguration(
      @RequestHeader(TENANT_HEADER) final String tenantIdentifier) {
    return this.bridgeConfigurationService.findByTenantIdentifier(tenantIdentifier)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> ServiceException.notFound("Tenant not found."));
  }

  @Permittable(value = AcceptedTokenType.TENANT, groupId = PermittableGroupIds.CONFIGURATION_MANAGEMENT)
  @RequestMapping(
      value = "/config",
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public
  @ResponseBody
  ResponseEntity<Void> setBridgeConfiguration(
      @RequestHeader(TENANT_HEADER) final String tenantIdentifier,
      @RequestBody @Valid final BridgeConfiguration instance) {
    this.commandGateway.process(new ChangeConfigurationCommand(tenantIdentifier, instance));
    return ResponseEntity.accepted().build();
  }
}