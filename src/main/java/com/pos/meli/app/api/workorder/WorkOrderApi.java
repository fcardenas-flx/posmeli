package com.pos.meli.app.api.workorder;

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
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonRootName(value = "Work Order")
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Work Order.")
public class WorkOrderApi
{
	@JsonProperty
	@Schema(description = "id")
	private Long id;

	@JsonProperty
	@Schema(description = "code")
	private String code;

	@JsonProperty
	@JsonIgnore
	@Schema(description = "order created at")
	private LocalDateTime createdAt;

	@JsonProperty
	@JsonIgnore
	@Schema(description = "order update at")
	private LocalDateTime updateAt;

	@JsonProperty
	@Schema(description = "description")
	private int description;

	@JsonProperty
	@Schema(description = "motorcycle")
	private MotorcycleApi motorcycle;

	@JsonProperty
	@Schema(description = "motorcycle owner")
	private MotorcycleOwnerApi motorcycleOwner;

	@JsonProperty
	@Schema(description = "motorcyles parts")
	private List<MotorcyclePartApi> motorcycleParts;

	@JsonProperty
	@Schema(description = "motorcycle techinal Services")
	private List<TechnicalServiceApi> technicalServices;

	@JsonProperty
	@Schema(description = "assigned technician")
	private Technician technician;

	@JsonProperty
	@Schema(description = "Work order Total Amount")
	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	private BigDecimal totalAmount;
}
