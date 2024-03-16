package com.pos.meli.app.rest.response.meliconnector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliItemPrice
{
	@JsonProperty("id")
	public String id;

	@JsonProperty("prices")
	public ArrayList<MeliPrice> prices;

	@JsonProperty("presentation")
	public MeliItemPricePresentation presentation;
	@JsonProperty("payment_method_prices")
	public ArrayList<String> paymentMethodPrices;

	@JsonProperty("reference_prices")
	public ArrayList<MeliPrice> referencePrices;

	@JsonProperty("purchase_discounts")
	public ArrayList<String> purchaseDiscounts;
}
