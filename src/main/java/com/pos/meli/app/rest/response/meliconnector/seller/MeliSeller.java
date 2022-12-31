package com.pos.meli.app.rest.response.meliconnector.seller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pos.meli.app.rest.response.meliconnector.seller.reputation.MeliSellerReputation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliSeller
{
	@JsonProperty("id")
	public String id;

	@JsonProperty("nickname")
	public String nickname;

	@JsonProperty("car_dealer")
	public boolean carDealer;

	@JsonProperty("real_estate_agency")
	public boolean realStateAgency;

	@JsonProperty("registration_date")
	public Date registrationDate;

	@JsonProperty("tags")
	public ArrayList<String> tags;

	@JsonProperty("permalink")
	public String permalink;

	@JsonProperty("seller_reputation")
	public MeliSellerReputation sellerReputation;
}
