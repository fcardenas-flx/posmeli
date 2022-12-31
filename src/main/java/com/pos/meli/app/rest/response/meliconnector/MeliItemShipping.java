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
public class MeliItemShipping
{
	@JsonProperty("logistic_type")
	public String logisticType;

	@JsonProperty("mode")
	public String mode;

	@JsonProperty("store_pick_up")
	public boolean storePickUp;

	@JsonProperty("free_shipping")
	public boolean freeShipping;

	@JsonProperty("tags")
	public ArrayList<String> tags;

	@JsonProperty("promise")
	public String promise;

}
