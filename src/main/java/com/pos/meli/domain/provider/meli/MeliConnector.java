package com.pos.meli.domain.provider.meli;

import com.pos.meli.app.api.ProductApi;
import com.pos.meli.app.rest.response.meliconnector.MeliItemResult;

import java.util.ArrayList;

public interface MeliConnector
{
	ArrayList<MeliItemResult> getAllProducts(String site_id, String nickname) throws Exception;

	MeliItemResult getItemById(String meliId);

	void updateItem(MeliItemResult meliItemResult);
}
