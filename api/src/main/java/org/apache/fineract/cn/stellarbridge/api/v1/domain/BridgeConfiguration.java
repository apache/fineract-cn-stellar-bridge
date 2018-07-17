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
  private String fineractIncomingAccountIdentifier;

  @ValidIdentifier
  private String fineractOutgoingAccountIdentifier;

  private String stellarAccountIdentifier;

  public BridgeConfiguration() {
    super();
  }

  public BridgeConfiguration(String fineractIncomingAccountIdentifier,
      String fineractOutgoingAccountIdentifier, String stellarAccountIdentifier) {
    this.fineractIncomingAccountIdentifier = fineractIncomingAccountIdentifier;
    this.fineractOutgoingAccountIdentifier = fineractOutgoingAccountIdentifier;
    this.stellarAccountIdentifier = stellarAccountIdentifier;
  }

  public String getFineractIncomingAccountIdentifier() {
    return fineractIncomingAccountIdentifier;
  }

  public void setFineractIncomingAccountIdentifier(String fineractIncomingAccountIdentifier) {
    this.fineractIncomingAccountIdentifier = fineractIncomingAccountIdentifier;
  }

  public String getFineractOutgoingAccountIdentifier() {
    return fineractOutgoingAccountIdentifier;
  }

  public void setFineractOutgoingAccountIdentifier(String fineractOutgoingAccountIdentifier) {
    this.fineractOutgoingAccountIdentifier = fineractOutgoingAccountIdentifier;
  }

  public String getStellarAccountIdentifier() {
    return stellarAccountIdentifier;
  }

  public void setStellarAccountIdentifier(String stellarAccountIdentifier) {
    this.stellarAccountIdentifier = stellarAccountIdentifier;
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
        .equals(fineractIncomingAccountIdentifier, that.fineractIncomingAccountIdentifier) &&
        Objects
            .equals(fineractOutgoingAccountIdentifier, that.fineractOutgoingAccountIdentifier) &&
        Objects.equals(stellarAccountIdentifier, that.stellarAccountIdentifier);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fineractIncomingAccountIdentifier, fineractOutgoingAccountIdentifier,
        stellarAccountIdentifier);
  }

  @Override
  public String toString() {
    return "BridgeConfiguration{" +
        "fineractIncomingAccountIdentifier='" + fineractIncomingAccountIdentifier + '\'' +
        ", fineractOutgoingAccountIdentifier='" + fineractOutgoingAccountIdentifier + '\'' +
        ", stellarAccountIdentifier='" + stellarAccountIdentifier + '\'' +
        '}';
  }
}
