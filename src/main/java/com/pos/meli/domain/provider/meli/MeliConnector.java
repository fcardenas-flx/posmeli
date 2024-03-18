package com.pos.meli.domain.provider.meli;

import com.pos.meli.app.api.ProductApi;
import com.pos.meli.app.rest.response.meliconnector.MeliItemPrice;
import com.pos.meli.app.rest.response.meliconnector.MeliItemResult;
import com.pos.meli.app.rest.response.meliconnector.MeliItemVariationResult;
import com.pos.meli.app.rest.response.meliconnector.MeliPrice;
import com.pos.meli.domain.model.MeliApiCredential;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface MeliConnector
{
	ArrayList<MeliItemResult> getAllProducts(String site_id, String nickname) throws Exception;

	MeliItemResult getItemById(String meliId);

	MeliItemResult getItemById(String meliId, String meliToken);

	MeliItemResult getItemBySku(String sku);

	MeliItemResult updateItemQuantity(String meliId, int quantity);

	MeliItemPrice getMshopsPriceById(String meliId);

	ArrayList<String> getAllMeliProductsIds(String siteId, String nickname, String userId);

	MeliItemResult updateItemQuantityVariation(String meliId,  String variationId, int quantity);

	String getAuthorizationToken(MeliApiCredential meliApiCredential);

	String getAuthorizationToken();

	MeliItemVariationResult getVariationItemByMeliIdAndVariationId(String meliId, String variationId, String meliToken);
}
