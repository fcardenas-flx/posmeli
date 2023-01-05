package com.pos.meli.domain.service.impl;

import com.pos.meli.app.api.ProductApi;
import com.pos.meli.app.rest.response.meliconnector.MeliItemAttribute;
import com.pos.meli.app.rest.response.meliconnector.MeliItemResult;
import com.pos.meli.app.rest.response.meliconnector.MeliPrice;
import com.pos.meli.domain.provider.meli.MeliConnector;
import com.pos.meli.domain.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class InventoryServiceImpl implements InventoryService
{

	@Autowired
	MeliConnector meliConnector;

	@Override
	public List<ProductApi> getAllProducts() throws Exception
	{

		String site_id = "MCO";
		String nickname = "CBR1542892";

		List<ProductApi> productApiList = new ArrayList<>();
		ProductApi productApi = new ProductApi();

		ArrayList<MeliItemResult> meliItemResults = meliConnector.getAllProducts(site_id, nickname);

		meliItemResults.stream().forEach(meliItemResult -> {

			productApi.setName(meliItemResult.getTitle());
			productApi.setMeliPrice(meliItemResult.getPrice());
			productApi.setMeliId(meliItemResult.getId());



			MeliItemResult meliItemSearched = meliConnector.getItemById(meliItemResult.getId());

			productApi.setQuantity(meliItemSearched.getInitialQuantity());

			MeliItemAttribute meliItemAttribute = meliItemSearched.getAttributes().stream().
					filter(attribute -> attribute.getId().equals("SELLER_SKU"))
					.collect(Collectors.toList()).get(0);

			productApi.setSku(meliItemAttribute.getValueName());

			MeliPrice mercashopsPrice = meliItemResult.getPrices().getPrices().stream().
					filter(meliPrice -> meliPrice.getConditions().getContextRestrictions().get(0).equals("channel_mshops")).
					collect(Collectors.toList()).get(0);

			productApi.setMshopsPrice(mercashopsPrice.getAmount());

			productApiList.add(productApi);

		});

		return productApiList;
	}

	@Override
	public List<ProductApi> saveProducts(List<ProductApi> products)
	{

		products.stream().forEach(productApi -> {



		});


		return null;
	}
}
