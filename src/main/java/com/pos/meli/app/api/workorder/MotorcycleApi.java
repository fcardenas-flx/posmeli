package com.pos.meli.app.api.workorder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonRootName(value = "Motorcycle")
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Motorcycle.")
public class MotorcycleApi
{
	@JsonProperty
	@Schema(description = "id")
	private Long id;

	@JsonProperty
	@Schema(description = "patent")
	private String patent;

	@JsonProperty
	@Schema(description = "reference")
	private String Reference;

	@JsonProperty
	@Schema(description = "color")
	private String color;

	@JsonProperty
	@Schema(description = "model")
	private String model;
}
