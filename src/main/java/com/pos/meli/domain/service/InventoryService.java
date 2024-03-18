package com.pos.meli.domain.service;

import com.pos.meli.app.api.MeliProductApi;
import com.pos.meli.app.api.ProductApi;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.List;

public interface InventoryService
{
	List<MeliProductApi> getAllProducts() throws Exception;

	List<ProductApi> saveProducts(List<ProductApi> products);

	MeliProductApi getProductById(String meliId);

	List<MeliProductApi> getAllMeliProducts(String nickname);

	List<ProductApi> syncProducts(String nickname) throws IOException, InvalidFormatException;
}
