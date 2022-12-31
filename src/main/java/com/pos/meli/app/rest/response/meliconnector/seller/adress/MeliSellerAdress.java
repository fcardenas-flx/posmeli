package com.pos.meli.app.rest.response.meliconnector.seller.adress;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class MeliSellerAdress
{
	@JsonProperty("comment")
	public String comment;

	@JsonProperty("address_line")
	public String addressLine;

	@JsonProperty("zip_code")
	public String zipCode;

	@JsonProperty("id")
	public String id;

	@JsonProperty("latitude")
	public String latitude;

	@JsonProperty("longitude")
	public String longitude;

	@JsonProperty("country")
	public MeliSellerCountry country;

	@JsonProperty("state")
	public MeliSellerState state;

	@JsonProperty("city")
	public MeliSellerCity city;
}
