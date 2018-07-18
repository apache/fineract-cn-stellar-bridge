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
package org.apache.fineract.cn.stellarbridge.api.v1.events;

@SuppressWarnings("unused")
public interface EventConstants {

  String DESTINATION = "stellarbridge-v1";
  String SELECTOR_NAME = "action";
  String INITIALIZE = "initialize";
  String PUT_CONFIG = "put-config";
  String PUT_STELLAR_CURRENCY_ISSUER = "put-stellar-currency-issuer";
  String STELLAR_PAYMENT_PROCESSED = "bridge-stellar-payment";
  String FINERACT_PAYMENT_PROCESSED = "bridge-fineract-payment";
  String SELECTOR_INITIALIZE = SELECTOR_NAME + " = '" + INITIALIZE + "'";
  String SELECTOR_PUT_CONFIG = SELECTOR_NAME + " = '" + PUT_CONFIG + "'";
  String SELECTOR_STELLAR_PAYMENT_PROCESSED = SELECTOR_NAME + " = '" + STELLAR_PAYMENT_PROCESSED + "'";
  String SELECTOR_FINERACT_PAYMENT_PROCESSED = SELECTOR_NAME + " = '" + FINERACT_PAYMENT_PROCESSED + "'";
  String SELECTOR_PUT_STELLAR_CURRENCY_ISSUER = SELECTOR_NAME + " = '" + PUT_STELLAR_CURRENCY_ISSUER + "'";
}
