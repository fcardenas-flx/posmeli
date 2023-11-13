package com.pos.meli.domain.service;

import com.pos.meli.app.api.ProductApi;

import java.util.List;

public interface InventoryService
{
	List<ProductApi>  getAllProducts() throws Exception;

	List<ProductApi> saveProducts(List<ProductApi> products);

	ProductApi getProductById(String meliId);

	List<ProductApi> getAllMeliProducts();

	List<ProductApi> syncProducts();
}
