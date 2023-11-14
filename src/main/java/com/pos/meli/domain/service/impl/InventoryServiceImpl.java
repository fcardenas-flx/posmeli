package com.pos.meli.domain.service.impl;

import com.pos.meli.app.api.InventoryApi;
import com.pos.meli.app.api.MeliProductApi;
import com.pos.meli.app.api.ProductApi;
import com.pos.meli.app.rest.response.meliconnector.MeliItemAttribute;
import com.pos.meli.app.rest.response.meliconnector.MeliItemResult;
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

		meliItemIds.parallelStream().forEach(meliItemId ->
		{
			MeliProductApi meliProductApi = new MeliProductApi();

			MeliItemResult meliItemResult = meliConnector.getItemById(meliItemId);

			meliProductApi.setName(meliItemResult.getTitle());
			meliProductApi.setMeliPrice(meliItemResult.getPrice());
			meliProductApi.setMeliId(meliItemResult.getId());

			meliProductApi.setQuantity(meliItemResult.getAvailableQuantity());

			List<MeliItemAttribute> attributesSku = meliItemResult.getAttributes().stream().
					filter(attribute -> attribute.getId().equals("SELLER_SKU"))
					.collect(Collectors.toList());

			if (attributesSku.isEmpty())
				meliProductApi.setSku(emptyData);
			else
				meliProductApi.setSku(attributesSku.get(0).getValueName());

			//productApi.setMshopsPrice(meliConnector.getMshopsPriceById(meliItemId).getPrices().get(0).getAmount());

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
					meliConnector.updateItemQuantity(meliProduct.get().getMeliId(), productApi.getQuantity());
				}
				catch (Exception exception)
				{
					System.out.println("Producto no se pudo actualizar");
				}
			}
		});

		System.out.println("Finalizado... Productos Sincronizados Satisfactoriamente");

//		meliProductApiList.stream().filter(meliProductApi -> meliProductApi.getSku().equals("0000001277")).collect(Collectors.toList());

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
