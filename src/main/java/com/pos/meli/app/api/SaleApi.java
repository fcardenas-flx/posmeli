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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonRootName(value = "Product")
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Product.")
public class SaleApi
{

	@JsonProperty
	private String id;

	@JsonProperty
	private String code;

	@JsonProperty
	private LocalDateTime date;

	@JsonProperty
	private ClientApi client;

	@JsonProperty
	private SellerApi seller;

	@JsonProperty
	private List<ProductApi> soldProducts;

	@JsonProperty
	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	private BigDecimal totalAmount;
}
