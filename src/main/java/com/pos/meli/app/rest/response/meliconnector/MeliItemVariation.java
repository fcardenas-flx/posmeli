package com.pos.meli.app.rest.response.meliconnector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pos.meli.domain.util.BigDecimalMoneySerializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliItemVariation
{
	@JsonProperty("id")
	public String id;

	@JsonProperty("price")
	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	public BigDecimal price;

	@JsonProperty("available_quantity")
	public int availableQuantity;

	@JsonProperty("sold_quantity")
	public int soldQuantity;
}
