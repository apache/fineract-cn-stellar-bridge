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
package org.apache.fineract.cn.stellarbridge.service.internal.repository;

import java.util.Objects;
import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table(name = "nenet_configuration")
public class BridgeConfigurationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "tenant_identifier")
  private String tenantIdentifier;
  @Column(name = "fineract_incoming_ledger")
  private String fineractIncomingLedger;
  @Column(name = "fineract_outgoing_ledger")
  private String fineractOutgoingLedger;
  @Column(name = "fineract_stellar_ledger")
  private String fineractStellerLedger;
  @Column(name = "stellar_identifier")
  private String stellarAccountIdentifier;
  @Column(name = "stellar_private_key")
  private String stellarAccountPrivateKey;

  public BridgeConfigurationEntity() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTenantIdentifier() {
    return tenantIdentifier;
  }

  public void setTenantIdentifier(String tenantIdentifier) {
    this.tenantIdentifier = tenantIdentifier;
  }

  public String getFineractIncomingLedger() {
    return fineractIncomingLedger;
  }

  public void setFineractIncomingLedger(String fineractIncomingLedger) {
    this.fineractIncomingLedger = fineractIncomingLedger;
  }

  public String getFineractOutgoingLedger() {
    return fineractOutgoingLedger;
  }

  public void setFineractOutgoingLedger(String fineractOutgoingLedger) {
    this.fineractOutgoingLedger = fineractOutgoingLedger;
  }

  public String getFineractStellerLedger() {
    return fineractStellerLedger;
  }

  public void setFineractStellerLedger(String fineractStellerLedger) {
    this.fineractStellerLedger = fineractStellerLedger;
  }

  public String getStellarAccountIdentifier() {
    return stellarAccountIdentifier;
  }

  public void setStellarAccountIdentifier(String stellarAccountIdentifier) {
    this.stellarAccountIdentifier = stellarAccountIdentifier;
  }

  public String getStellarAccountPrivateKey() {
    return stellarAccountPrivateKey;
  }

  public void setStellarAccountPrivateKey(String stellarAccountPrivateKey) {
    this.stellarAccountPrivateKey = stellarAccountPrivateKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BridgeConfigurationEntity that = (BridgeConfigurationEntity) o;
    return Objects.equals(stellarAccountIdentifier, that.stellarAccountIdentifier);
  }

  @Override
  public int hashCode() {

    return Objects.hash(stellarAccountIdentifier);
  }
}
