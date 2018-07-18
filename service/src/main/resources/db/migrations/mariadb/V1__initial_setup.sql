--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements.  See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership.  The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License.  You may obtain a copy of the License at
--
--   http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied.  See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

CREATE TABLE nenet_configuration (
  id BIGINT NOT NULL AUTO_INCREMENT,
  tenant_identifier        VARCHAR(32)  NOT NULL,
  fineract_incoming_ledger VARCHAR(512) NOT NULL,
  fineract_outgoing_ledger VARCHAR(512) NOT NULL,
  fineract_stellar_ledger VARCHAR(512) NOT NULL,
  stellar_identifier VARCHAR(512) NULL,
  stellar_private_key VARCHAR(512) NULL,
  CONSTRAINT nenet_configuration_uq UNIQUE (tenant_identifier),
  CONSTRAINT nenet_configuration_pk PRIMARY KEY (id)
);

CREATE TABLE nenet_stellar_cursor (
  id BIGINT NOT NULL AUTO_INCREMENT,
  xcursor            VARCHAR(50)  NOT NULL,
  processed          BOOLEAN      NOT NULL,
  created_on         TIMESTAMP    NOT NULL,
  CONSTRAINT nenet_stellar_cursor_uq UNIQUE (xcursor),
  CONSTRAINT nenet_stellar_cursor_pk PRIMARY KEY (id)
);

CREATE TABLE nenet_currency_issuer (
  id BIGINT NOT NULL AUTO_INCREMENT,
  tenant_identifier  VARCHAR(32)  NOT NULL,
  currency_code      VARCHAR(3)   NOT NULL,
  stellar_issuer     VARCHAR(512) NULL,
  CONSTRAINT nenet_currency_issuer_uq UNIQUE (tenant_identifier, currency_code),
  CONSTRAINT nenet_currency_issuer_pk PRIMARY KEY (id)
);