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
	@JsonIgnore
	@Schema(description = "id")
	private Long id;

	@JsonProperty
	@JsonIgnore
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
	private String description;

	@JsonProperty
	@Schema(description = "status")
	private String status;

	@JsonProperty
	@Schema(description = "motorcycle")
	private MotorcycleApi motorcycle;

	@JsonProperty
	@Schema(description = "motorcycle owner")
	private MotorcycleOwnerApi motorcycleOwner;

	@JsonProperty
	@JsonIgnore
	@Schema(description = "motorcyles parts")
	private List<MotorcyclePartApi> motorcycleParts;

	@JsonProperty
	@JsonIgnore
	@Schema(description = "motorcycle techinal Services")
	private List<TechnicalServiceApi> technicalServices;

	@JsonProperty
	@JsonIgnore
	@Schema(description = "assigned technician")
	private TechnicianApi technician;

	@JsonProperty
	@JsonIgnore
	@Schema(description = "Work order Total Amount")
	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	private BigDecimal totalAmount;

	@JsonProperty
	@JsonIgnore
	@Schema(description = "Work order Total Amount Parts")
	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	private BigDecimal totalAmountParts;

	@JsonProperty
	@JsonIgnore
	@Schema(description = "Work order Total Amount Service")
	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	private BigDecimal totalAmountService;
}
