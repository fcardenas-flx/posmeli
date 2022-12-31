package com.pos.meli.app.rest.response.meliconnector;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

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
	public ArrayList<String> referencePrices;

	@JsonProperty("purchase_discounts")
	public ArrayList<String> purchaseDiscounts;
}
