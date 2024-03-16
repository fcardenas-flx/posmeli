package com.pos.meli.app.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonRootName(value = "Product")
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Product.")
public class ProductApi
{
	@JsonProperty
	private String name;

	@JsonProperty
	private String sku;

	@JsonProperty
	private int quantity;

	@JsonProperty
	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	@Schema(description = "Purchase Price")
	private BigDecimal purchasePrice;

	@JsonProperty
	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	@Schema(description = "Sale Price")
	private BigDecimal salePrice;
}
