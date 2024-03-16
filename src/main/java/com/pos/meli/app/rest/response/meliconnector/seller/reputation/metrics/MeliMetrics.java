package com.pos.meli.app.rest.response.meliconnector.seller.reputation.metrics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pos.meli.app.rest.response.meliconnector.seller.reputation.metrics.MeliCancellations;
import com.pos.meli.app.rest.response.meliconnector.seller.reputation.metrics.MeliClaims;
import com.pos.meli.app.rest.response.meliconnector.seller.reputation.metrics.MeliDelayedHandlingTime;
import com.pos.meli.app.rest.response.meliconnector.seller.reputation.metrics.MeliSales;
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
public class MeliMetrics
{
	@JsonProperty("sales")
	public MeliSales sales;

	@JsonProperty("claims")
	public MeliClaims claims;

	@JsonProperty("delayed_handling_time")
	public MeliDelayedHandlingTime delayedHandlingTime;

	@JsonProperty("cancellations")
	public MeliCancellations cancellations;

}
