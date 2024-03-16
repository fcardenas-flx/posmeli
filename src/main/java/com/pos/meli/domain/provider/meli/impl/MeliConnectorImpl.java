package com.pos.meli.domain.provider.meli.impl;

import com.pos.meli.app.rest.response.meliconnector.MeliItemPrice;
import com.pos.meli.app.rest.response.meliconnector.MeliItemResult;
import com.pos.meli.app.rest.response.meliconnector.MeliItemSearchResponse;
import com.pos.meli.app.rest.response.meliconnector.MeliItemVariationResult;
import com.pos.meli.app.rest.response.meliconnector.MeliSearchResult;
import com.pos.meli.app.rest.response.meliconnector.MeliSearchScrollResult;
import com.pos.meli.app.rest.response.meliconnector.MeliToken;
import com.pos.meli.domain.provider.meli.MeliConnector;
import org.apache.poi.ss.usermodel.charts.ScatterChartData;
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

	@Value("${provider.meli.clientId:5153304841324457}")
	private String clientId;

	@Value("${provider.meli.clientSecret:YFGPJuX9HIh1zB4U0oLb363wucCpv2oP}")
	private String clientSecret;

	@Value("${provider.meli.refreshToken:TG-65cfcb3b690e44000112be09-537077242}")
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
		String url = builder.append(this.url).append("/items?ids=").append(meliId).append("&attributes=id,title,price,available_quantity,attributes").toString();

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
	public MeliItemResult getItemById(String meliId, String meliToken)
	{
		MeliItemSearchResponse[] meliItemsSearchResponse;


		StringBuilder builder = new StringBuilder();
		String url = builder.append(this.url).append("/items?ids=").append(meliId)
				.append("&attributes=id,title,price,available_quantity,attributes,variations").toString();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(meliToken);

		HttpEntity request = new HttpEntity(headers);

		meliItemsSearchResponse = restTemplate.exchange(url, HttpMethod.GET, request,
				MeliItemSearchResponse[].class, 1).getBody();


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

	@Override
	public ArrayList<String> getAllMeliProductsIds(String siteId, String nickname, String userId)
	{

		ArrayList<String> resultProducts = new ArrayList<>();

		StringBuilder builder = new StringBuilder();
		String url = builder.append(this.url).append("/users/").append(userId)
				.append("/items/search?search_type=scan&limit=100&attributes=scroll_id,results").toString();

		String meliToken = getAuthorizationToken();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(meliToken);

		HttpEntity request = new HttpEntity(headers);

		MeliSearchScrollResult meliSearchScrollResult = restTemplate.exchange(url, HttpMethod.GET, request, MeliSearchScrollResult.class, 1).getBody();

		String scrollId = meliSearchScrollResult.getScrollId();

		resultProducts.addAll(meliSearchScrollResult.getResults());

		do
		{
			HttpEntity scrollRequest = new HttpEntity(headers);

			meliSearchScrollResult = restTemplate.exchange(url + "&scroll_id=" + scrollId, HttpMethod.GET, scrollRequest, MeliSearchScrollResult.class, 1).getBody();

			scrollId = meliSearchScrollResult.getScrollId();

			if (scrollId != "")
			{
				resultProducts.addAll(meliSearchScrollResult.getResults());
			}
		}
		while (!scrollId.isEmpty());

		return resultProducts;
	}

	@Override
	public MeliItemResult updateItemQuantityVariation(String meliId, String variationId, int quantity)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		String meliToken = getAuthorizationToken();
		headers.setBearerAuth(meliToken);

		HttpEntity formEntity = new HttpEntity<String>("{\"variations\":[{\"id\":"+variationId+",\"available_quantity\":"+quantity+"}]}", headers);

		return restTemplate.exchange(url + "/items/" + meliId, HttpMethod.PUT, formEntity, MeliItemResult.class).getBody();
	}

	@Override
	public String getAuthorizationToken()
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(buildAuthorizationRequest(), headers);

		return restTemplate.exchange(url + "/oauth/token", HttpMethod.POST, formEntity, MeliToken.class)
				.getBody().accessToken;
	}

	@Override
	public MeliItemVariationResult getVariationItemByMeliIdAndVariationId(String meliId, String variationId, String meliToken)
	{
		MeliItemVariationResult meliItemVariationResult;

		StringBuilder builder = new StringBuilder();
		String url = builder.append(this.url).append("/items/").append(meliId).append("/variations/").append(variationId)
				.toString();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(meliToken);

		HttpEntity request = new HttpEntity(headers);

		meliItemVariationResult = restTemplate.exchange(url, HttpMethod.GET, request,
				MeliItemVariationResult.class, 1).getBody();

		return meliItemVariationResult;
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
