package com.pos.meli.app.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pos.meli.domain.util.BigDecimalMoneySerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "meliProduct")
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "meliProduct.")
public class MeliProductApi extends ProductApi
{
	@JsonProperty
	private String meliId;

	@JsonProperty
	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	@Schema(description = "Meli Price")
	private BigDecimal meliPrice;

	@JsonProperty
	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	@Schema(description = "Total Amount")
	private BigDecimal mshopsPrice;

	@JsonProperty
	@Schema(description = "Meli Product Variations")
	private ArrayList<String> variations;

}
