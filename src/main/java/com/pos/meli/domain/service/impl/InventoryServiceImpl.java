package com.pos.meli.domain.service.impl;

import com.pos.meli.app.api.IncomingInvoiceApi;
import com.pos.meli.app.api.InventoryApi;
import com.pos.meli.app.api.InvoiceItemApi;
import com.pos.meli.app.api.MeliProductApi;
import com.pos.meli.app.api.MeliProductVariationApi;
import com.pos.meli.app.api.ProductApi;
import com.pos.meli.app.rest.request.IncomingInvoiceRequest;
import com.pos.meli.app.rest.response.meliconnector.MeliItemAttribute;
import com.pos.meli.app.rest.response.meliconnector.MeliItemResult;
import com.pos.meli.app.rest.response.meliconnector.MeliItemVariationResult;
import com.pos.meli.app.rest.response.meliconnector.MeliPrice;
import com.pos.meli.domain.model.IncomingProduct;
import com.pos.meli.domain.model.InventoryProduct;
import com.pos.meli.domain.model.MeliAccount;
import com.pos.meli.domain.model.Seller;
import com.pos.meli.domain.model.SynchronizedProduct;
import com.pos.meli.domain.provider.email.EmailConnector;
import com.pos.meli.domain.provider.meli.MeliConnector;
import com.pos.meli.domain.repository.IncomingProductRepository;
import com.pos.meli.domain.repository.InventoryProductRepository;
import com.pos.meli.domain.repository.MeliAccountRepository;
import com.pos.meli.domain.repository.SellerRepository;
import com.pos.meli.domain.repository.SynchronizedProductRepository;
import com.pos.meli.domain.service.AbstractService;
import com.pos.meli.domain.service.Enum.UnWantedString;
import com.pos.meli.domain.service.FileService;
import com.pos.meli.domain.service.InventoryService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

@Service
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class InventoryServiceImpl extends AbstractService implements InventoryService
{

	@Autowired
	MeliConnector meliConnector;

	@Autowired
	EmailConnector emailConnector;

	@Autowired
	MeliAccountRepository meliAccountRepository;

	@Autowired
	SynchronizedProductRepository synchronizedProductRepository;

	@Autowired
	InventoryProductRepository inventoryProductRepository;

	@Autowired
	IncomingProductRepository incomingProductRepository;

	@Autowired
	SellerRepository sellerRepository;

	@Autowired
	FileService fileService;

	protected ModelMapper mapper;

	@PostConstruct
	protected void init()
	{
		mapper = new ModelMapper();
	}

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
	public List<MeliProductApi> getAllMeliProducts(String nickname, String meliToken)
	{
		List<MeliProductApi> meliProductApiList = new ArrayList<>();

		try
		{
			System.out.println("Consultando cuenta Meli...");

			MeliAccount meliAccount = meliAccountRepository.findByNickname(nickname);

			System.out.println("Obteniendo Ids de Productos...");

			ArrayList<String> meliItemIds = meliConnector.getAllMeliProductsIds(meliAccount.getSiteId(),
					meliAccount.getNickname(), meliAccount.getUserId(), meliToken);

			System.out.println("Completando información de Productos...");

			meliItemIds.parallelStream().forEach(meliItemId ->
			{
				MeliProductApi meliProductApi = new MeliProductApi();

				try
				{
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

		}
		catch (Exception exception)
		{
			System.out.println(exception.getCause() + " " + exception.getMessage());
		}

		return meliProductApiList;
	}

	@Override
	public List<MeliProductApi> getAllMeliProducts(String nickname)
	{
		MeliAccount meliAccount = meliAccountRepository.findByNickname(nickname);

		List<MeliProductApi> meliProductApiList = new ArrayList<>();

		String meliToken = meliConnector.getAuthorizationToken(meliAccount.getMeliApiCredential());

		ArrayList<String> meliItemIds = meliConnector.getAllMeliProductsIds(meliAccount.getSiteId(), meliAccount.getNickname(), meliAccount.getUserId(), meliToken);

		meliItemIds.parallelStream().forEach(meliItemId ->
		{
			MeliProductApi meliProductApi = new MeliProductApi();

			try
			{
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

	@Async
	@Override
	public void syncProducts(String nickname) throws IOException, InvalidFormatException
	{

		try
		{
			String UUID = generateRandomUUID();

			System.out.println("UUID del Proceso..." + UUID);

			System.out.println("Obteniendo Información de Inventario...");

			InventoryApi inventoryDataFile = getInventoryDataFile(nickname);

			System.out.println("Obteniendo Información de productos Publicados en Meli...");

			MeliAccount meliAccount = meliAccountRepository.findByNickname(nickname);

			String meliToken = meliConnector.getAuthorizationToken(meliAccount.getMeliApiCredential());

			System.out.println("Token Obtenido...");

			List<MeliProductApi> meliProductApiList = getAllMeliProducts(nickname, meliToken);

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
							meliConnector.updateItemQuantity(meliProduct.get().getMeliId(), productApi.getQuantity(), meliToken);
							System.out.println("Producto actualizado :" + meliProduct.get().getSku());
						}
						else
						{
							meliProduct.get().getVariations().stream().forEach(variation ->
							{
								meliConnector.updateItemQuantityVariation(meliProduct.get().getMeliId(), variation.getId(), productApi.getQuantity(), meliToken);
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
									product.getQuantity(), meliToken);

							System.out.println(product.getName());
							System.out.println(product.getSku());
						}
					}
					catch (Exception exception)
					{
						System.out.println("No se pudo actualizar producto con variación ");
					}

					//System.out.println("Variación actualizada :" + variation.getId());
				});
			});

			System.out.println("Finalizado... Productos Sincronizados Satisfactoriamente");

			System.out.println("Salvando Productos Sincronizados con Process Id");

			List<SynchronizedProduct> synchronizedProducts = new ArrayList<>();

			productApiListWithQuantityDifferences.stream().forEach(productApi ->
			{
				SynchronizedProduct synchronizedProduct = mapper.map(productApi, SynchronizedProduct.class);
				synchronizedProduct.setProcessId(UUID);
				synchronizedProducts.add(synchronizedProduct);
			});

			synchronizedProductRepository.saveAll(synchronizedProducts);

			System.out.println("Productos Salvados con Process Id Exitosamente " + UUID);

			emailConnector.send("motoshop2.sogamoso@gmail.com", "motoshop2.sogamoso@gmail.com", "Productos Actualizados " + nickname, "Se actualizaron " +productApiListWithQuantityDifferences.size()+" productos con process id: " + UUID);

		}
		catch (Exception exception)
		{
			System.out.println("Error Sincronizando Productos: " + exception.getCause()+ " " + exception.getMessage());
		}
	}

	@Override
	public List<ProductApi> getSynchronizedProductsByProcessId(String processId)
	{

		List<ProductApi> productSynchronizedApiList = new ArrayList<>();

		List<SynchronizedProduct> synchronizedProducts = synchronizedProductRepository.findAllByProcessId(processId);

		synchronizedProducts.stream().forEach(synchronizedProduct ->
		{
			productSynchronizedApiList.add(mapper.map(synchronizedProduct, ProductApi.class));
		});

		return productSynchronizedApiList;
	}

	@Override
	public List<ProductApi> saveProductsFromInventoryFile(String nickname)
	{
		try
		{
			List<ProductApi> productInventoryList = getInventoryDataFile(nickname).getProductApiList();

			List<InventoryProduct> inventoryProductsToSave = new ArrayList<>();

			productInventoryList.stream().forEach(productInventory ->
			{
				InventoryProduct inventoryProductToSave = new InventoryProduct();

				inventoryProductToSave.setQuantity(productInventory.getQuantity());
				inventoryProductToSave.setSku(productInventory.getSku());
				inventoryProductToSave.setPurchasePrice(productInventory.getPurchasePrice());
				inventoryProductToSave.setSalePrice(productInventory.getSalePrice());

				String nameCleaned = getCleanedString(productInventory.getName());
				inventoryProductToSave.setName(nameCleaned);

				String reference = getLastWord(nameCleaned);

				Seller seller = sellerRepository.findByName(productInventory.getStore());

				inventoryProductToSave.setSeller(seller);

				inventoryProductToSave.setReference(reference);

				inventoryProductToSave.setLocation(productInventory.getLocation());

				inventoryProductToSave.setSupplier(productInventory.getSupplier());

				inventoryProductsToSave.add(inventoryProductToSave);
			});

			inventoryProductRepository.saveAll(inventoryProductsToSave);

		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvalidFormatException e)
		{
			throw new RuntimeException(e);
		}

		return List.of();
	}

	@Override
	public List<ProductApi> saveIncomingProductsFromInvoiceFile(String nickname, IncomingInvoiceRequest incomingInvoiceRequest)
	{

		List<IncomingProduct> incomingProducts = new ArrayList<>();

		IncomingInvoiceApi incomingInvoiceApi = getInvoiceDataFile(nickname, incomingInvoiceRequest);

		Seller seller = sellerRepository.findByName("Repuestos");

		incomingInvoiceApi.getInvoiceItems().stream().forEach(invoiceItemApi ->
		{
			if(!invoiceItemApi.getProduct().getName().isBlank())
			{
				IncomingProduct incomingProduct = new IncomingProduct();

				incomingProduct.setName(invoiceItemApi.getProduct().getName());
				incomingProduct.setSupplier(incomingInvoiceRequest.getSupplier());
				incomingProduct.setInvoiceCode(incomingInvoiceRequest.getCode());

				incomingProduct.setQuantity(invoiceItemApi.getProduct().getQuantity());

				incomingProduct.setReference(invoiceItemApi.getProduct().getReference());

				List<InventoryProduct> inventoryProductsByReference = inventoryProductRepository.findAllByReference(invoiceItemApi.getProduct().getReference());

				if (!inventoryProductsByReference.isEmpty())
					incomingProduct.setSku(inventoryProductsByReference.stream().findFirst().get().getSku());
				else
					incomingProduct.setSku(emptyData);

				incomingProduct.setUnitValue(invoiceItemApi.getUnitValue());

				BigDecimal purchasePrice = new BigDecimal(invoiceItemApi.getTax()).multiply(invoiceItemApi.getUnitValue());

				incomingProduct.setPurchasePrice(purchasePrice);

				BigDecimal salePrice = new BigDecimal(invoiceItemApi.getTax()).multiply(invoiceItemApi.getUnitValue()).multiply(
						BigDecimal.valueOf(1.5));

				incomingProduct.setSalePrice(salePrice);


				incomingProduct.setSeller(seller);

				incomingProducts.add(incomingProduct);
			}

		});

		incomingProductRepository.saveAll(incomingProducts);


		return List.of();
	}

	private IncomingInvoiceApi getInvoiceDataFile(String nickname, IncomingInvoiceRequest incomingInvoiceRequest)
	{
		IncomingInvoiceApi incomingInvoiceApi = new IncomingInvoiceApi();

		List<InvoiceItemApi> invoiceItemApiList = new ArrayList<>();

		Workbook workbook = fileService.getXlsFileFromSftp(
				incomingInvoiceRequest.getCode() + "_" + incomingInvoiceRequest.getSupplier() + "_" +
						nickname + ".xlsx");

		Sheet sheet = workbook.getSheetAt(0);

		Iterator<Row> rowIterator = sheet.iterator();

		while (rowIterator.hasNext())
		{
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();

			if (row.getRowNum() == 0) {
				continue;
			}

			InvoiceItemApi invoiceItemApi = new InvoiceItemApi();

			ProductApi productApi = new ProductApi();

			productApi.setReference(row.getCell(0).getStringCellValue());
			productApi.setName(row.getCell(1).getStringCellValue());
			productApi.setQuantity((int) row.getCell(2).getNumericCellValue());

			invoiceItemApi.setProduct(productApi);

			invoiceItemApi.setUnitValue(BigDecimal.valueOf(row.getCell(3).getNumericCellValue()));
			invoiceItemApi.setTax(row.getCell(4).getNumericCellValue());
			invoiceItemApi.setDiscount(row.getCell(5).getNumericCellValue());

			invoiceItemApiList.add(invoiceItemApi);
		}

		incomingInvoiceApi.setInvoiceItems(invoiceItemApiList);

		return incomingInvoiceApi;
	}

	private InventoryApi getInventoryDataFile(String nickname) throws IOException, InvalidFormatException
	{
		InventoryApi inventoryApi = new InventoryApi();

		List<ProductApi> productApiList = new ArrayList<>();

		Workbook workbook = fileService.getXlsFileFromSftp(nickname+"_INVENTORY.xls");

		Sheet sheet = workbook.getSheetAt(0);

		Iterator<Row> rowIterator = sheet.iterator();

		while (rowIterator.hasNext())
		{
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();

			if (row.getRowNum() == 0) {
				continue;
			}

			ProductApi productApi = new ProductApi();

			productApi.setStore(row.getCell(0).getStringCellValue());
			productApi.setCategory(row.getCell(1).getStringCellValue());
			productApi.setName(row.getCell(2).getStringCellValue());
			productApi.setQuantity((int) row.getCell(7).getNumericCellValue());
			productApi.setPurchasePrice(BigDecimal.valueOf(row.getCell(5).getNumericCellValue()));
			productApi.setSalePrice(BigDecimal.valueOf(row.getCell(6).getNumericCellValue()));
			productApi.setLocation(row.getCell(9).getStringCellValue());
			productApi.setSupplier(row.getCell(12).getStringCellValue());

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

	private String generateRandomUUID()
	{
		//generates random UUID
		UUID uuid= UUID.randomUUID();
		System.out.println(uuid);
		return uuid.toString();
	}

	private String getCleanedString(String text)
	{
		if (text == null || text.isBlank()) {
			return text;
		}

		UnWantedString[] unWantedStrings =
				{UnWantedString.INITIAL, UnWantedString.SLASH, UnWantedString.MEDIUMSCRIPT, UnWantedString.DOUBLESPACE,
				UnWantedString.Initial, UnWantedString.initial};

		for (UnWantedString stringEnum : unWantedStrings)
		{
			String string = stringEnum.getValue();

			if (string != null && !string.isBlank())
			{
				// Use regular expressions to replace case-insensitively
				Pattern pattern = Pattern.compile(Pattern.quote(string), Pattern.CASE_INSENSITIVE);
				text = pattern.matcher(text).replaceAll(" ");
			}
		}

		return text.trim().replaceAll("\\s+", " ");
	}

	private String getLastWord(String text)
	{
		if (text == null || text.isBlank()) {
			return "";
		}

		String[] words = text.split("\\s+");
		return words[words.length - 1];
	}

}
