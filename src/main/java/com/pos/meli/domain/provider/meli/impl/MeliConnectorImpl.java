package com.pos.meli.domain.provider.meli.impl;

import com.pos.meli.app.api.ProductApi;
import com.pos.meli.app.rest.response.meliconnector.MeliItemPrice;
import com.pos.meli.app.rest.response.meliconnector.MeliItemResult;
import com.pos.meli.app.rest.response.meliconnector.MeliItemSearchResponse;
import com.pos.meli.app.rest.response.meliconnector.MeliPrice;
import com.pos.meli.app.rest.response.meliconnector.MeliSearchResult;
import com.pos.meli.app.rest.response.meliconnector.MeliToken;
import com.pos.meli.domain.provider.meli.MeliConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.RequestEntity.head;
import static org.springframework.http.RequestEntity.post;

@Service
@EnableConfigurationProperties
public class MeliConnectorImpl implements MeliConnector
{

	@Autowired
	RestTemplate restTemplate;

	@Value("${provider.meli.clientId:1690093796892956}")
	private String clientId;

	@Value("${provider.meli.clientSecret:dF1n00gMvkpAFhDKnnv4T4UOjop8h85m}")
	private String clientSecret;

	@Value("${provider.meli.refreshToken:TG-655167b8ed7e48000199268a-537077242}")
	private String refreshToken;

	@Value("${provider.meli.api.grantType:refresh_token}")
	private String grantType;

	@Value("${provider.meli.api.url:https://api.mercadolibre.com}")
	private String url;

	@Value("${provider.meli.api.path:/}")
	private String path;

	@Override
	public ArrayList<MeliItemResult> getAllProducts(String site_id, String nickname) throws Exception
	{
		StringBuilder builder = new StringBuilder();
		String url = builder.append(this.url).append("/sites/").append(site_id).append("/search?nickname=").append(nickname).toString();

		String meliToken = getAuthorizationToken();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(meliToken);

		HttpEntity request = new HttpEntity(headers);

		MeliSearchResult meliSearchResult = new MeliSearchResult();

		meliSearchResult = restTemplate.exchange(url, HttpMethod.GET, request, MeliSearchResult.class, 1).getBody();

		return meliSearchResult.getResults();
	}

	@Override
	public MeliItemResult getItemById(String meliId)
	{
		StringBuilder builder = new StringBuilder();
		String url = builder.append(this.url).append("/items?ids=").append(meliId).toString();

		String meliToken = getAuthorizationToken();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(meliToken);

		HttpEntity request = new HttpEntity(headers);

		MeliItemSearchResponse[] meliItemsSearchResponse;

		meliItemsSearchResponse = restTemplate.exchange(url, HttpMethod.GET, request, MeliItemSearchResponse[].class, 1).getBody();

		return Arrays.stream(meliItemsSearchResponse).findFirst().get().getBody();
	}

	@Override
	public MeliItemResult getItemBySku(String sku)
	{

		return null;
	}

	@Override
	public MeliItemResult updateItemQuantity(String meliID, int newQuantity)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		String meliToken = getAuthorizationToken();
		headers.setBearerAuth(meliToken);

		HttpEntity formEntity = new HttpEntity<String>("{\"available_quantity\":"+ newQuantity +"}", headers);

		return restTemplate.exchange(url + "/items/" + meliID, HttpMethod.PUT, formEntity, MeliItemResult.class).getBody();
	}

	@Override
	public MeliItemPrice getMshopsPriceById(String meliId)
	{
		StringBuilder builder = new StringBuilder();
		String url = builder.append(this.url).append("/items/").append(meliId).append("/prices/types/standard/channels/mshops").toString();

		String meliToken = getAuthorizationToken();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(meliToken);

		HttpEntity request = new HttpEntity(headers);

		MeliItemPrice meliPrice;

		meliPrice = restTemplate.exchange(url, HttpMethod.GET, request, MeliItemPrice.class, 1).getBody();

		return meliPrice;
	}

	private String getAuthorizationToken()
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(buildAuthorizationRequest(), headers);

		return restTemplate.exchange(url + "/oauth/token", HttpMethod.POST, formEntity, MeliToken.class)
				.getBody().accessToken;
	}

	private MultiValueMap<String, String> buildAuthorizationRequest()
	{
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
		requestBody.add("grant_type", this.grantType);
		requestBody.add("client_id", this.clientId);
		requestBody.add("client_secret", this.clientSecret);
		requestBody.add("refresh_token", this.refreshToken);

		return requestBody;
	}

}
