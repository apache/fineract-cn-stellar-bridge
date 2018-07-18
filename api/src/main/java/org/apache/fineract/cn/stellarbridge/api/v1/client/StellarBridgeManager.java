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
package org.apache.fineract.cn.stellarbridge.api.v1.client;

import java.util.List;
import org.apache.fineract.cn.api.util.CustomFeignClientsConfiguration;
import org.apache.fineract.cn.stellarbridge.api.v1.domain.BridgeConfiguration;
import org.apache.fineract.cn.stellarbridge.api.v1.domain.StellarCurrencyIssuer;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@SuppressWarnings("unused")
@FeignClient(value="stellarbridge-v1", path="/stellarbridge/v1", configuration = CustomFeignClientsConfiguration.class)
public interface StellarBridgeManager {
  @RequestMapping(
          value = "/config",
          method = RequestMethod.GET,
          produces = MediaType.ALL_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE
  )
  BridgeConfiguration getBridgeConfiguration();


  @RequestMapping(
      value = "/config",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  void setBridgeConfiguration(final BridgeConfiguration bridgeConfiguration);

/**
 *  A currency with the same code can be available from many issuers.  The stellar bridge needs to
 *  know which one to use.
 */
  @RequestMapping(
      value = "/currencyissuers",
      method = RequestMethod.GET,
      produces = {MediaType.ALL_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE}
  )
  List<StellarCurrencyIssuer> getAllCurrencyIssuers();

  @RequestMapping(
      value = "/currencyissuers/{currencycode}/",
      method = RequestMethod.GET,
      produces = {MediaType.ALL_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE}
  )
  StellarCurrencyIssuer getCurrencyIssuerForCurrency(
      @PathVariable("currencycode") final String currencyCode);
}
