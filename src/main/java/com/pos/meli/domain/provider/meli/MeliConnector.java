package com.pos.meli.domain.provider.meli;

import com.pos.meli.app.api.ProductApi;
import com.pos.meli.app.rest.response.meliconnector.MeliItemPrice;
import com.pos.meli.app.rest.response.meliconnector.MeliItemResult;
import com.pos.meli.app.rest.response.meliconnector.MeliPrice;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface MeliConnector
{
	ArrayList<MeliItemResult> getAllProducts(String site_id, String nickname) throws Exception;

	MeliItemResult getItemById(String meliId);

	MeliItemResult getItemBySku(String sku);

	MeliItemResult updateItemQuantity(String meliId, int quantity);

	MeliItemPrice getMshopsPriceById(String meliId);

	ArrayList<String> getAllMeliProductsIds(String siteId, String nickname, String userId);
}
