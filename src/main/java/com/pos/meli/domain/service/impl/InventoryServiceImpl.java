package com.pos.meli.domain.service.impl;

import com.pos.meli.app.api.InventoryApi;
import com.pos.meli.app.api.MeliProductApi;
import com.pos.meli.app.api.MeliProductVariationApi;
import com.pos.meli.app.api.ProductApi;
import com.pos.meli.app.rest.response.meliconnector.MeliItemAttribute;
import com.pos.meli.app.rest.response.meliconnector.MeliItemResult;
import com.pos.meli.app.rest.response.meliconnector.MeliItemVariation;
import com.pos.meli.app.rest.response.meliconnector.MeliItemVariationResult;
import com.pos.meli.app.rest.response.meliconnector.MeliPrice;
import com.pos.meli.domain.provider.meli.MeliConnector;
import com.pos.meli.domain.service.AbstractService;
import com.pos.meli.domain.service.FileService;
import com.pos.meli.domain.service.InventoryService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class InventoryServiceImpl extends AbstractService implements InventoryService
{

	@Autowired
	MeliConnector meliConnector;

	@Autowired
	FileService fileService;

	@Override
	public List<MeliProductApi> getAllProducts() throws Exception
	{

		String site_id = "MCO";
		String nickname = "MOTOSHOP2REPUESTOS";

		List<MeliProductApi> meliProductApiList = new ArrayList<>();
		MeliProductApi meliProductApi = new MeliProductApi();

		ArrayList<MeliItemResult> meliItemResults = meliConnector.getAllProducts(site_id, nickname);

		meliItemResults.stream().forEach(meliItemResult -> {

			meliProductApi.setName(meliItemResult.getTitle());
			meliProductApi.setMeliPrice(meliItemResult.getPrice());
			meliProductApi.setMeliId(meliItemResult.getId());



			MeliItemResult meliItemSearched = meliConnector.getItemById(meliItemResult.getId());

			meliProductApi.setQuantity(meliItemSearched.getInitialQuantity());

			MeliItemAttribute meliItemAttribute = meliItemSearched.getAttributes().stream().
					filter(attribute -> attribute.getId().equals("SELLER_SKU"))
					.collect(Collectors.toList()).get(0);

			meliProductApi.setSku(meliItemAttribute.getValueName());

			MeliPrice mercashopsPrice = meliItemResult.getPrices().getPrices().stream().
					filter(meliPrice -> meliPrice.getConditions().getContextRestrictions().get(0).equals("channel_mshops")).
					collect(Collectors.toList()).get(0);

			meliProductApi.setMshopsPrice(mercashopsPrice.getAmount());

			meliProductApiList.add(meliProductApi);

		});

		return meliProductApiList;
	}

	@Override
	public List<ProductApi> saveProducts(List<ProductApi> products)
	{

		products.stream().forEach(productApi -> {



		});


		return null;
	}

	@Override
	public MeliProductApi getProductById(String meliId)
	{
		MeliItemResult meliItemSearched = meliConnector.getItemById(meliId);

		MeliProductApi meliProductApi = new MeliProductApi();

		meliProductApi.setName(meliItemSearched.getTitle());

		meliProductApi.setMeliId(meliId);

		meliProductApi.setQuantity(meliItemSearched.getInitialQuantity());

		MeliItemAttribute meliItemAttribute = meliItemSearched.getAttributes().stream().
				filter(attribute -> attribute.getId().equals("SELLER_SKU"))
				.collect(Collectors.toList()).get(0);

		meliProductApi.setSku(meliItemAttribute.getValueName());

		meliProductApi.setMeliPrice(meliItemSearched.getPrice());

		meliProductApi.setMshopsPrice(meliConnector.getMshopsPriceById(meliId).getPrices().get(0).getAmount());

		return meliProductApi;
	}

	@Override
	public List<MeliProductApi> getAllMeliProducts()
	{
		String siteId = "MCO";
		String nickname = "MOTOSHOP2REPUESTOS";
		String userId = "537077242";

		List<MeliProductApi> meliProductApiList = new ArrayList<>();

		ArrayList<String> meliItemIds = meliConnector.getAllMeliProductsIds(siteId, nickname, userId);

		String meliToken = meliConnector.getAuthorizationToken();

		meliItemIds.parallelStream().forEach(meliItemId ->
		{
			MeliProductApi meliProductApi = new MeliProductApi();

			try
			{
				System.out.println(meliItemId);

				MeliItemResult meliItemResult = meliConnector.getItemById(meliItemId, meliToken);

				meliProductApi.setName(meliItemResult.getTitle());
				meliProductApi.setMeliPrice(meliItemResult.getPrice());
				meliProductApi.setMeliId(meliItemResult.getId());
				meliProductApi.setSku(emptyData);
				meliProductApi.setQuantity(meliItemResult.getAvailableQuantity());

				if (!meliItemResult.getVariations().isEmpty())
				{
					ArrayList<MeliProductVariationApi> variations = new ArrayList<>();

					meliItemResult.getVariations().stream().forEach(meliItemVariation ->
					{
						MeliProductVariationApi meliProductVariationApi = new MeliProductVariationApi();
						meliProductVariationApi.setId(meliItemVariation.getId());
						meliProductVariationApi.setQuantity(meliItemVariation.getAvailableQuantity());

						MeliItemVariationResult meliItemVariationResult =
								meliConnector.getVariationItemByMeliIdAndVariationId(meliItemId,
										meliItemVariation.getId(), meliToken);

						List<MeliItemAttribute> attributesSku = meliItemVariationResult.getAttributes().stream().
								filter(attribute -> attribute.getId().equals("SELLER_SKU"))
								.collect(Collectors.toList());

						if (attributesSku.isEmpty())
							meliProductVariationApi.setSku(emptyData);
						else
							meliProductVariationApi.setSku(attributesSku.get(0).getValueName());

						variations.add(meliProductVariationApi);
					});

					meliProductApi.setVariations(variations);
				}
				else
				{
					List<MeliItemAttribute> attributesSku = meliItemResult.getAttributes().stream().
							filter(attribute -> attribute.getId().equals("SELLER_SKU"))
							.collect(Collectors.toList());

					if (attributesSku.isEmpty())
						meliProductApi.setSku(emptyData);
					else
						meliProductApi.setSku(attributesSku.get(0).getValueName());
				}
			}
			catch (Exception exception)
			{
				System.out.println("Error Obteniendo información de Producto " + meliItemId);
			}

			meliProductApiList.add(meliProductApi);
		});

		return meliProductApiList;
	}

	@Override
	public List<ProductApi> syncProducts() throws IOException, InvalidFormatException
	{
		System.out.println("Obteniendo Información de Inventario...");

		InventoryApi inventoryDataFile = getInventoryDataFile();

		System.out.println("Obteniendo Información de productos Publicados en Meli...");

		List<MeliProductApi> meliProductApiList = getAllMeliProducts();

		System.out.println("Obteniendo Información de productos con cantidades diferentes...");

		List<ProductApi> productApiListWithQuantityDifferences = inventoryDataFile.getProductApiList().stream()
				.filter(productApi -> meliProductApiList.stream()
						.anyMatch(meliProductApi -> meliProductApi.getSku().equals(productApi.getSku())
								&& meliProductApi.getQuantity() != productApi.getQuantity())).collect(Collectors.toList());

		//TODO: Optimizar este filtro para obtener directamente las variaciones con cantidades diferentes
		List<MeliProductApi> productApiListWithVariations = meliProductApiList.stream()
				.filter(meliProductApi -> meliProductApi.getVariations() != null).collect(Collectors.toList());


		List<ProductApi> productApiListNonPublished = new ArrayList<>();

//		productApiListNonPublished = inventoryDataFile.getProductApiList().stream()
//				.filter(productApi -> meliProductApiList.stream()
//						.noneMatch(meliProductApi -> meliProductApi.getSku().equals(productApi.getSku()))).collect(Collectors.toList());


		System.out.println("Sincronizando stock de productos... "+productApiListWithQuantityDifferences.size());

		productApiListWithQuantityDifferences.stream().forEach(productApi ->
		{
			System.out.println("Sincronizando Producto: " + productApi.getSku() + " " + productApi.getName());

			Optional<MeliProductApi> meliProduct = meliProductApiList.stream()
					.filter(meliProductApi -> meliProductApi.getSku().equals(productApi.getSku())).findFirst();

			if (meliProduct.isPresent())
			{
				try
				{
					if (meliProduct.get().getVariations() == null)
					{
						meliConnector.updateItemQuantity(meliProduct.get().getMeliId(), productApi.getQuantity());
						System.out.println("Producto actualizado :" + meliProduct.get().getSku());
					}
					else
					{
						meliProduct.get().getVariations().stream().forEach(variation ->
						{
							meliConnector.updateItemQuantityVariation(meliProduct.get().getMeliId(), variation.getId(), productApi.getQuantity());
							System.out.println("Variación actualizada :" + variation.getId());
						});
					}
				}
				catch (Exception exception)
				{
					System.out.println("Producto no se pudo actualizar");
				}
			}
			else
			{
				//productApiListNonPublished.add(productApi);
			}
		});

		System.out.println("Finalizado... Productos Sincronizados Satisfactoriamente");


		System.out.println("Actualizando stock de productos con Variación");

		productApiListWithVariations.parallelStream().forEach(meliProductApi ->
		{
			meliProductApi.getVariations().stream().forEach(variation ->
			{


				//System.out.println("Sincronizando Producto con variacion: " + variation.getId() + " Sku: " + variation.getSku() + " " + meliProductApi.getName());

				try
				{
					ProductApi product = inventoryDataFile.getProductApiList().stream()
							.filter(productApi -> productApi.getSku().equals(variation.getSku())).findFirst().get();

					if(variation.getQuantity() != product.getQuantity())
					{
						meliConnector.updateItemQuantityVariation(meliProductApi.getMeliId(), variation.getId(),
								product.getQuantity());

						System.out.println(product.getName());
						System.out.println(product.getSku());
					}
				}
				catch (Exception exception)
				{
					System.out.println("No se pudo actualizar producto con variación ");
				}

				System.out.println("Variación actualizada :" + variation.getId());
			});
		});



//		inventoryDataFile.getProductApiList().stream().forEach(productApi ->
//		{
//			System.out.println("Sincronizando Producto: " + productApi.getSku() + " " + productApi.getName());
//
//			Optional<MeliProductApi> meliProduct = meliProductApiList.stream()
//					.filter(meliProductApi -> meliProductApi.getSku().equals(productApi.getSku())).findFirst();
//
//			if (meliProduct.isPresent())
//			{
//				try
//				{
//					if (meliProduct.get().getVariations() == null)
//					{
//						meliConnector.updateItemQuantity(meliProduct.get().getMeliId(), productApi.getQuantity());
//						System.out.println("Producto actualizado:" + meliProduct.get().getSku());
//					}
//					else
//					{
//						meliProduct.get().getVariations().stream().forEach(variationId ->
//						{
//							meliConnector.updateItemQuantityVariation(variationId, productApi.getQuantity());
//							System.out.println("Producto con Variación actualizada :" + variationId);
//						});
//					}
//				}
//				catch (Exception exception)
//				{
//					System.out.println("Producto no se pudo actualizar");
//				}
//			}
//			else
//			{
//				productApiListNonPublished.add(productApi);
//				System.out.println("Producto no publicado en Meli");
//			}
//		});


		System.out.println("Finalizado... Productos Sincronizados Satisfactoriamente");

//		List<ProductApi> productApiListNonPublished = inventoryDataFile.getProductApiList().stream()
//				.filter(productApi -> meliProductApiList.stream()
//						.noneMatch((meliProductApi -> meliProductApi.getSku().equals(productApi.getSku())))).collect(
//						Collectors.toList());
		return null;
	}

	private InventoryApi getInventoryDataFile() throws IOException, InvalidFormatException
	{
		InventoryApi inventoryApi = new InventoryApi();

		List<ProductApi> productApiList = new ArrayList<>();

		Workbook wb = WorkbookFactory.create(new File("/home/usuario/Escritorio/" + "inventory.xls"));

		Sheet ws = wb.getSheetAt(0);

		Iterator<Row> rowIterator = ws.iterator();

		while (rowIterator.hasNext())
		{
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();

			if (row.getRowNum() == 0) {
				continue;
			}

			ProductApi productApi = new ProductApi();

			productApi.setName(row.getCell(2).getStringCellValue());
			productApi.setQuantity((int) row.getCell(7).getNumericCellValue());
			productApi.setPurchasePrice(BigDecimal.valueOf(row.getCell(5).getNumericCellValue()));
			productApi.setSalePrice(BigDecimal.valueOf(row.getCell(6).getNumericCellValue()));

			switch (row.getCell(3).getCellType())
			{
				case Cell.CELL_TYPE_STRING:
					productApi.setSku(row.getCell(3).getStringCellValue());
					break;

				case Cell.CELL_TYPE_NUMERIC:
					productApi.setSku(String.valueOf(row.getCell(3).getNumericCellValue()));
					break;
			}

			productApiList.add(productApi);
		}

		inventoryApi.setProductApiList(productApiList);

		return inventoryApi;
	}


}
