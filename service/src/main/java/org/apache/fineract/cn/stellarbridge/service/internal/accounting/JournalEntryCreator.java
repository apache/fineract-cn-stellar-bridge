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
package org.apache.fineract.cn.stellarbridge.service.internal.accounting;

import org.apache.fineract.cn.accounting.api.v1.client.JournalEntryAlreadyExistsException;
import org.apache.fineract.cn.accounting.api.v1.client.JournalEntryValidationException;
import org.apache.fineract.cn.accounting.api.v1.domain.JournalEntry;
import org.apache.fineract.cn.anubis.annotation.Permittable;
import org.apache.fineract.cn.api.annotation.ThrowsException;
import org.apache.fineract.cn.api.annotation.ThrowsExceptions;
import org.apache.fineract.cn.permittedfeignclient.annotation.EndpointSet;
import org.apache.fineract.cn.permittedfeignclient.annotation.PermittedFeignClientsConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Myrle Krantz
 */
@EndpointSet(identifier = "stellarbridge__v1__accounting__v1")
@FeignClient(name="accounting-v1", path="/accounting/v1", configuration=PermittedFeignClientsConfiguration.class)
public interface JournalEntryCreator {
  @RequestMapping(
      value = "/journal",
      method = RequestMethod.POST,
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE}
  )
  @ThrowsExceptions({
      @ThrowsException(status = HttpStatus.BAD_REQUEST, exception = JournalEntryValidationException.class),
      @ThrowsException(status = HttpStatus.CONFLICT, exception = JournalEntryAlreadyExistsException.class)
  })
  @Permittable(groupId = org.apache.fineract.cn.accounting.api.v1.PermittableGroupIds.THOTH_JOURNAL)
  void createJournalEntry(@RequestBody final JournalEntry journalEntry);
}
