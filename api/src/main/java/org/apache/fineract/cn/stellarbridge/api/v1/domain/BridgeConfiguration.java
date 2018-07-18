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
package org.apache.fineract.cn.stellarbridge.api.v1.domain;

import java.util.Objects;
import org.apache.fineract.cn.lang.validation.constraints.ValidIdentifier;

@SuppressWarnings({"WeakerAccess", "unused"})
public class BridgeConfiguration {
  @ValidIdentifier
  private String fineractOutgoingStagingLedgerIdentifier;

  @ValidIdentifier
  private String fineractStellarAssetsLedgerIdentifier;

  @ValidIdentifier
  private String fineractIncomingStagingLedgerIdentifier;

  private String stellarAccountIdentifier;

  private String stellarPrivateKey;

  public BridgeConfiguration() {
    super();
  }

  public BridgeConfiguration(
      String fineractOutgoingStagingLedgerIdentifier,
      String fineractStellarAssetsLedgerIdentifier,
      String fineractIncomingStagingLedgerIdentifier,
      String stellarAccountIdentifier,
      String stellarPrivateKey) {
    this.fineractOutgoingStagingLedgerIdentifier = fineractOutgoingStagingLedgerIdentifier;
    this.fineractStellarAssetsLedgerIdentifier = fineractStellarAssetsLedgerIdentifier;
    this.fineractIncomingStagingLedgerIdentifier = fineractIncomingStagingLedgerIdentifier;
    this.stellarAccountIdentifier = stellarAccountIdentifier;
    this.stellarPrivateKey = stellarPrivateKey;
  }

  public String getFineractOutgoingStagingLedgerIdentifier() {
    return fineractOutgoingStagingLedgerIdentifier;
  }

  public void setFineractOutgoingStagingLedgerIdentifier(
      String fineractOutgoingStagingLedgerIdentifier) {
    this.fineractOutgoingStagingLedgerIdentifier = fineractOutgoingStagingLedgerIdentifier;
  }

  public String getFineractStellarAssetsLedgerIdentifier() {
    return fineractStellarAssetsLedgerIdentifier;
  }

  public void setFineractStellarAssetsLedgerIdentifier(
      String fineractStellarAssetsLedgerIdentifier) {
    this.fineractStellarAssetsLedgerIdentifier = fineractStellarAssetsLedgerIdentifier;
  }

  public String getFineractIncomingStagingLedgerIdentifier() {
    return fineractIncomingStagingLedgerIdentifier;
  }

  public void setFineractIncomingStagingLedgerIdentifier(
      String fineractIncomingStagingLedgerIdentifier) {
    this.fineractIncomingStagingLedgerIdentifier = fineractIncomingStagingLedgerIdentifier;
  }

  public String getStellarAccountIdentifier() {
    return stellarAccountIdentifier;
  }

  public void setStellarAccountIdentifier(String stellarAccountIdentifier) {
    this.stellarAccountIdentifier = stellarAccountIdentifier;
  }

  public String  getStellarPrivateKey() {
    return stellarPrivateKey;
  }

  public void setStellarPrivateKey(String  stellarPrivateKey) {
    this.stellarPrivateKey = stellarPrivateKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BridgeConfiguration that = (BridgeConfiguration) o;
    return Objects
        .equals(fineractOutgoingStagingLedgerIdentifier,
            that.fineractOutgoingStagingLedgerIdentifier)
        &&
        Objects
            .equals(fineractStellarAssetsLedgerIdentifier,
                that.fineractStellarAssetsLedgerIdentifier)
        &&
        Objects.equals(fineractIncomingStagingLedgerIdentifier,
            that.fineractIncomingStagingLedgerIdentifier) &&
        Objects.equals(stellarAccountIdentifier, that.stellarAccountIdentifier) &&
        Objects.equals(stellarPrivateKey, that.stellarPrivateKey);
  }

  @Override
  public int hashCode() {

    return Objects
        .hash(fineractOutgoingStagingLedgerIdentifier, fineractStellarAssetsLedgerIdentifier,
            fineractIncomingStagingLedgerIdentifier, stellarAccountIdentifier, stellarPrivateKey);
  }

  @Override
  public String toString() {
    return "BridgeConfiguration{" +
        "fineractOutgoingStagingLedgerIdentifier='" + fineractOutgoingStagingLedgerIdentifier + '\''
        +
        ", fineractStellarAssetsLedgerIdentifier='" + fineractStellarAssetsLedgerIdentifier + '\'' +
        ", fineractIncomingStagingLedgerIdentifier='" + fineractIncomingStagingLedgerIdentifier
        + '\''
        +
        ", stellarAccountIdentifier='" + stellarAccountIdentifier + '\'' +
        '}';
  }
}
