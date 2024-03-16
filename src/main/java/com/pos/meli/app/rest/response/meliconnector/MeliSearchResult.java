package com.pos.meli.app.rest.response.meliconnector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pos.meli.app.rest.response.meliconnector.paging.MeliPaging;
import com.pos.meli.app.rest.response.meliconnector.seller.MeliSeller;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliSearchResult
{
	@JsonProperty("site_id")
	public String siteId;

	@JsonProperty("seller")
	public MeliSeller seller;

	@JsonProperty("country_default_time_zone")
	public String countryDefaultTimeZone;

	@JsonProperty("paging")
	public MeliPaging paging;

	@JsonProperty("results")
	public ArrayList<MeliItemResult> results;

	@JsonProperty("sort")
	public MeliSort sort;

	@JsonProperty("available_sorts")
	public ArrayList<MeliSort> availableSorts;

	@JsonProperty("filters")
	public ArrayList<MeliFilter> filters;

	@JsonProperty("available_filters")
	public ArrayList<MeliFilter> availableFilters;

	@JsonProperty("scroll_id")
	public String scrollId;
}
