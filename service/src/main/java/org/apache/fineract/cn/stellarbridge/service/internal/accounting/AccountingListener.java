package org.apache.fineract.cn.stellarbridge.service.internal.accounting;

import org.apache.fineract.cn.command.gateway.CommandGateway;
import org.apache.fineract.cn.lang.config.TenantHeaderFilter;
import org.apache.fineract.cn.stellarbridge.service.internal.command.FineractPaymentCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class AccountingListener {
  private final CommandGateway commandGateway;

  @Autowired
  public AccountingListener(
      final CommandGateway commandGateway) {
    this.commandGateway = commandGateway;
  }

  public void onFineractPayment(
      @Header(TenantHeaderFilter.TENANT_HEADER) final String tenant,
      final String payload) {
    final FineractPaymentCommand fineractPaymentCommand = new FineractPaymentCommand(tenant,
        payload, null, null, null, null);
    commandGateway.process(fineractPaymentCommand);
  }

}
