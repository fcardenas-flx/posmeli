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
@JsonRootName(value = "Technician")
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Technician.")
public class TechnicianApi
{
	@JsonProperty
	@Schema(description = "id")
	private Long id;

	@JsonProperty
	@Schema(description = "name")
	private String name;

	@JsonProperty
	@Schema(description = "lastName")
	private String lastName;

	@JsonProperty
	@Schema(description = "owner identification")
	private String identification;

	@JsonProperty
	@Schema(description = "phone number")
	private String phoneNumber;
}
