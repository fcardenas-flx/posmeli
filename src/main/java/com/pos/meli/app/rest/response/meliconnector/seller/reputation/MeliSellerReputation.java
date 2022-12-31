package com.pos.meli.app.rest.response.meliconnector.seller.reputation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pos.meli.app.rest.response.meliconnector.seller.reputation.metrics.MeliMetrics;
import com.pos.meli.app.rest.response.meliconnector.seller.reputation.transactions.MeliTransactions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliSellerReputation
{
	@JsonProperty("level_id")
	public String levelId;

	@JsonProperty("power_seller_status")
	public String powerSellerStatus;

	@JsonProperty("transactions")
	public MeliTransactions transactions;

	@JsonProperty("metrics")
	public MeliMetrics metrics;
}
