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
package org.apache.fineract.cn.stellarbridge.service.internal.config;

import org.apache.fineract.cn.anubis.config.EnableAnubis;
import org.apache.fineract.cn.api.config.EnableApiFactory;
import org.apache.fineract.cn.async.config.EnableAsync;
import org.apache.fineract.cn.cassandra.config.EnableCassandra;
import org.apache.fineract.cn.command.config.EnableCommandProcessing;
import org.apache.fineract.cn.lang.config.EnableApplicationName;
import org.apache.fineract.cn.lang.config.EnableServiceException;
import org.apache.fineract.cn.lang.config.EnableTenantContext;
import org.apache.fineract.cn.mariadb.config.EnableMariaDB;
import org.apache.fineract.cn.permittedfeignclient.config.EnablePermissionRequestingFeignClient;
import org.apache.fineract.cn.stellarbridge.service.ServiceConstants;
import org.apache.fineract.cn.stellarbridge.service.internal.accounting.JournalEntryCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("WeakerAccess")
@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableApiFactory
@EnableAsync
@EnableTenantContext
@EnableCassandra
@EnableMariaDB(forTenantContext = false)
@EnableCommandProcessing
@EnableAnubis
@EnableServiceException
@EnablePermissionRequestingFeignClient(feignClasses = {JournalEntryCreator.class})
@RibbonClient(name = "rhythm-v1")
@EnableApplicationName
@EnableFeignClients(clients = {JournalEntryCreator.class})
@ComponentScan({
    "org.apache.fineract.cn.stellarbridge.service.rest",
    "org.apache.fineract.cn.stellarbridge.service.internal.service",
    "org.apache.fineract.cn.stellarbridge.service.internal.config",
    "org.apache.fineract.cn.stellarbridge.service.internal.repository",
    "org.apache.fineract.cn.stellarbridge.service.internal.federation",
    "org.apache.fineract.cn.stellarbridge.service.internal.horizonadapter",
    "org.apache.fineract.cn.stellarbridge.service.internal.accounting",
    "org.apache.fineract.cn.stellarbridge.service.internal.command.handler"
})
@EnableJpaRepositories({
    "org.apache.fineract.cn.stellarbridge.service.internal.repository"
})
public class StellarBridgeConfiguration extends WebMvcConfigurerAdapter {

  public StellarBridgeConfiguration() {
    super();
  }

  @Bean(name = ServiceConstants.LOGGER_NAME)
  public Logger logger() {
    return LoggerFactory.getLogger(ServiceConstants.LOGGER_NAME);
  }

  @Override
  public void configurePathMatch(final PathMatchConfigurer configurer) {
    configurer.setUseSuffixPatternMatch(Boolean.FALSE);
  }
}
