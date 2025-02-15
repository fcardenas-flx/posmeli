package com.pos.meli.domain.service.impl;

import com.pos.meli.app.api.InventoryApi;
import com.pos.meli.app.api.MeliProductApi;
import com.pos.meli.app.api.MeliProductVariationApi;
import com.pos.meli.app.api.ProductApi;
import com.pos.meli.app.rest.response.meliconnector.MeliItemAttribute;
import com.pos.meli.app.rest.response.meliconnector.MeliItemResult;
import com.pos.meli.app.rest.response.meliconnector.MeliItemVariationResult;
import com.pos.meli.app.rest.response.meliconnector.MeliPrice;
import com.pos.meli.domain.model.MeliAccount;
import com.pos.meli.domain.model.SynchronizedProduct;
import com.pos.meli.domain.provider.email.EmailConnector;
import com.pos.meli.domain.provider.meli.MeliConnector;
import com.pos.meli.domain.repository.MeliAccountRepository;
import com.pos.meli.domain.repository.SynchronizedProductRepository;
import com.pos.meli.domain.service.AbstractService;
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


			List<ProductApi> productsUpdatedList = new ArrayList<>();

			List<ProductApi> productsNonUpdatedList = new ArrayList<>();


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
							productsUpdatedList.add(productApi);
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
						productsNonUpdatedList.add(productApi);
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

					ProductApi product = inventoryDataFile.getProductApiList().stream()
							.filter(productApi -> productApi.getSku().equals(variation.getSku())).findFirst().get();

					//System.out.println("Sincronizando Producto con variacion: " + variation.getId() + " Sku: " + variation.getSku() + " " + meliProductApi.getName());

					try
					{
						if(variation.getQuantity() != product.getQuantity())
						{
							meliConnector.updateItemQuantityVariation(meliProductApi.getMeliId(), variation.getId(),
									product.getQuantity(), meliToken);

							System.out.println(product.getName());
							System.out.println(product.getSku());

							productsUpdatedList.add(product);
						}
					}
					catch (Exception exception)
					{
						System.out.println("No se pudo actualizar producto con variación ");
						productsNonUpdatedList.add(product);
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

			String htmlReport = generateSyncReportHtml(productsUpdatedList, productsNonUpdatedList, "Se actualizaron " +productApiListWithQuantityDifferences.size()+
					" productos con process id: " + UUID);

			emailConnector.send("motoshop2.sogamoso@gmail.com", "motoshop2.sogamoso@gmail.com",
					"Productos Actualizados " + nickname, " ", htmlReport);

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

			productApi.setName(row.getCell(2).getStringCellValue());
			productApi.setQuantity(Math.max((int) row.getCell(7).getNumericCellValue(), 0));

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

	private String generateRandomUUID()
	{
		//generates random UUID
		UUID uuid= UUID.randomUUID();
		System.out.println(uuid);
		return uuid.toString();
	}

	private String generateSyncReportHtml(List<ProductApi> processedList, List<ProductApi> nonProcessedList, String text) {
		StringBuilder html = new StringBuilder();
		html.append("<html><body>");
		html.append("<h2>").append(text).append("</h2>");

		// Estilos CSS para mejorar la presentación
		html.append("<style>")
				.append("table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }")
				.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }")
				.append("th { background-color: #f2f2f2; }")
				.append("h3 { margin-top: 20px; }")
				.append("</style>");

		// Tabla de productos procesados
		html.append("<h3 style='color:green;'>Productos Procesados</h3>");
		html.append("<table>");
		html.append("<tr><th>SKU</th><th>Nombre</th><th>Cantidad</th></tr>");
		for (ProductApi p : processedList) {
			html.append("<tr>")
					.append("<td>").append(p.getSku()).append("</td>")
					.append("<td>").append(p.getName()).append("</td>")
					.append("<td>").append(p.getQuantity()).append("</td>")
					.append("</tr>");
		}
		html.append("</table>");

		// Tabla de productos no procesados
		html.append("<h3 style='color:red;'>Productos No Procesados</h3>");
		html.append("<table>");
		html.append("<tr><th>SKU</th><th>Nombre</th><th>Cantidad</th><th>Motivo</th></tr>");
		for (ProductApi p : nonProcessedList) {
			html.append("<tr>")
					.append("<td>").append(p.getSku()).append("</td>")
					.append("<td>").append(p.getName()).append("</td>")
					.append("<td>").append(p.getQuantity()).append("</td>")
					.append("</tr>");
		}
		html.append("</table>");

		html.append("</body></html>");
		return html.toString();
	}

}
