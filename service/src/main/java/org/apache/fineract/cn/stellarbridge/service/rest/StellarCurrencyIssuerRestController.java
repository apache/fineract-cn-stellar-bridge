package org.apache.fineract.cn.stellarbridge.service.rest;

import static org.apache.fineract.cn.lang.config.TenantHeaderFilter.TENANT_HEADER;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.fineract.cn.anubis.annotation.AcceptedTokenType;
import org.apache.fineract.cn.anubis.annotation.Permittable;
import org.apache.fineract.cn.command.gateway.CommandGateway;
import org.apache.fineract.cn.lang.ServiceException;
import org.apache.fineract.cn.stellarbridge.api.v1.PermittableGroupIds;
import org.apache.fineract.cn.stellarbridge.api.v1.domain.StellarCurrencyIssuer;
import org.apache.fineract.cn.stellarbridge.service.internal.command.ChangeStellarCurrencyIssuerCommand;
import org.apache.fineract.cn.stellarbridge.service.internal.service.StellarCurrencyIssuerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currencyissuers")
public class StellarCurrencyIssuerRestController {

  private final CommandGateway commandGateway;
  private final StellarCurrencyIssuerService stellarCurrencyIssuerService;

  @Autowired
  public StellarCurrencyIssuerRestController(
      final CommandGateway commandGateway,
      final StellarCurrencyIssuerService stellarCurrencyIssuerService) {
    this.commandGateway = commandGateway;
    this.stellarCurrencyIssuerService = stellarCurrencyIssuerService;
  }
  @Permittable(value = AcceptedTokenType.TENANT, groupId = PermittableGroupIds.CONFIGURATION_MANAGEMENT)
  @RequestMapping(
      method = RequestMethod.GET,
      consumes = MediaType.ALL_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public
  @ResponseBody
  ResponseEntity<List<StellarCurrencyIssuer>> get(
      @RequestHeader(TENANT_HEADER) final String tenantIdentifier) {
    return ResponseEntity.ok(this.stellarCurrencyIssuerService.findAll(tenantIdentifier)
        .collect(Collectors.toList()));
  }

  @Permittable(value = AcceptedTokenType.TENANT, groupId = PermittableGroupIds.CONFIGURATION_MANAGEMENT)
  @RequestMapping(
      value = "/{currencycode}",
      method = RequestMethod.GET,
      consumes = MediaType.ALL_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public
  @ResponseBody
  ResponseEntity<StellarCurrencyIssuer> getBridgeConfiguration(
      @RequestHeader(TENANT_HEADER) final String tenantIdentifier,
      @PathVariable("currencycode") String currencyCode) {
    return this.stellarCurrencyIssuerService.find(tenantIdentifier, currencyCode)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> ServiceException.notFound("Tenant not found."));
  }

  @Permittable(value = AcceptedTokenType.TENANT, groupId = PermittableGroupIds.CONFIGURATION_MANAGEMENT)
  @RequestMapping(
      value = "{currencycode}",
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public @ResponseBody ResponseEntity<Void> changeCase(
      @RequestHeader(TENANT_HEADER) final String tenantIdentifier,
      @PathVariable("currencycode") final String currencyCode,
      @RequestBody @Valid final StellarCurrencyIssuer instance)
  {
    if (!currencyCode.equals(instance.getCurrencyCode()))
      throw ServiceException.badRequest("Instance currency code may not be changed.");

    this.commandGateway.process(new ChangeStellarCurrencyIssuerCommand(tenantIdentifier, instance));
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }
}
