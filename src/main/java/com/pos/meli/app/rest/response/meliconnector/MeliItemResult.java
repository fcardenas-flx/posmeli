package com.pos.meli.app.rest.response.meliconnector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pos.meli.app.rest.response.meliconnector.seller.MeliSeller;
import com.pos.meli.app.rest.response.meliconnector.seller.adress.MeliSellerAdress;
import com.pos.meli.domain.util.BigDecimalMoneySerializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliItemResult
{
	@JsonProperty("id")
	public String id;

	@JsonProperty("title")
	public String title;

	@JsonProperty("condition")
	public String condition;

	@JsonProperty("thumbnail_id")
	public String thumbnailId;

	@JsonProperty("catalog_product_id")
	public String catalogProductId;

	@JsonProperty("listing_type_id")
	public String listingTypeId;

	@JsonProperty("permalink")
	public String permalink;

	@JsonProperty("buying_mode")
	public String buyingMode;

	@JsonProperty("site_id")
	public String siteId;

	@JsonProperty("category_id")
	public String categoryId;

	@JsonProperty("domain_id")
	public String domainId;

	@JsonProperty("thumbnail")
	public String thumbnail;

	@JsonProperty("currency_id")
	public String currencyId;

	@JsonProperty("order_backend")
	public String orderBackend;

	@JsonProperty("price")
	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	public BigDecimal price;

	@JsonProperty("original_price")
	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	public BigDecimal originalPrice;

	@JsonProperty("sale_price")
	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	public BigDecimal salePrice;

	@JsonProperty("sold_quantity")
	public int soldQuantity;

	@JsonProperty("available_quantity")
	public int availableQuantity;

	@JsonProperty("official_store_id")
	public String officialStoreId;

	@JsonProperty("use_thumbnail_id")
	public boolean useThumbnailId;

	@JsonProperty("accepts_mercadopago")
	public boolean acceptsMercadoPago;

	@JsonProperty("tags")
	public ArrayList<String> tags;

	@JsonProperty("shipping")
	public MeliItemShipping shipping;

	@JsonProperty("stop_time")
	public Date stopTime;

	@JsonProperty("seller")
	public MeliSeller seller;

	@JsonProperty("seller_address")
	public MeliSellerAdress sellerAddress;

	@JsonProperty("address")
	public MeliItemAdress address;

	@JsonProperty("attributes")
	public ArrayList<MeliItemAttribute> attributes;

	@JsonProperty("prices")
	public MeliItemPrice prices;

	@JsonProperty("installments")
	public MeliItemInstallments installments;

	@JsonProperty("winner_item_id")
	public String winnerItemId;

	@JsonProperty("discounts")
	public String discounts;

	@JsonProperty("promotions")
	public String promotions;

	@JsonProperty("inventory_id")
	public String inventoryId;
}
