package com.pos.meli.domain.service.impl;

import com.pos.meli.app.api.CompanyApi;
import com.pos.meli.app.api.InvoiceApi;
import com.pos.meli.app.api.SaleApi;
import com.pos.meli.app.rest.response.meliconnector.MeliItemResult;
import com.pos.meli.domain.model.Company;
import com.pos.meli.domain.model.Sale;
import com.pos.meli.domain.model.SoldProduct;
import com.pos.meli.domain.model.catalog.Consecutive;
import com.pos.meli.domain.provider.meli.MeliConnector;
import com.pos.meli.domain.repository.CatalogRepository;
import com.pos.meli.domain.repository.CompanyRepository;
import com.pos.meli.domain.repository.SaleRepository;
import com.pos.meli.domain.repository.SoldProductRepository;
import com.pos.meli.domain.service.AbstractService;
import com.pos.meli.domain.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class SaleServiceImpl extends AbstractService implements SaleService
{

	@Autowired
	MeliConnector meliConnector;

	@Autowired
	SaleRepository saleRepository;

	@Autowired
	SoldProductRepository soldProductRepository;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	CatalogRepository catalogRepository;

	@Value("${seller.company.nit:1010243260}")
	private String companyNit;

	protected ModelMapper mapper;

	@Override
	public SaleApi performSell(SaleApi saleApi)
	{

		saleApi.setDate(LocalDateTime.now());

		Consecutive consecutive = catalogRepository.findConsecutiveByCode(consecutiveSalesCode);
		int newConsecutive = Integer.parseInt(consecutive.getValue()) + 1;
		String saleCode = consecutiveSalesPrefix + newConsecutive;

		consecutive.setValue(String.valueOf(newConsecutive));

		catalogRepository.save(consecutive);

		saleApi.setCode(saleCode);

		Sale sale = new Sale();
		sale.setCode(saleCode);
		sale.setTotalAmount(saleApi.getTotalAmount());
		sale.setSoldDate(LocalDateTime.now());


		List<SoldProduct> soldProducts = new ArrayList<>();

		saleApi.getSoldProducts().stream().forEach(soldProductApi -> {

			SoldProduct soldProduct = new SoldProduct();

			MeliItemResult meliItemResult = meliConnector.getItemById(soldProductApi.getMeliId());
			int productQuantity = meliItemResult.getInitialQuantity();
			int quantity = productQuantity - soldProductApi.getSoldQuantity();
			meliItemResult.setInitialQuantity(quantity);
			meliConnector.updateItem(meliItemResult);

			soldProduct.setMeliId(soldProductApi.getMeliId());
			soldProduct.setName(soldProductApi.getName());
			soldProduct.setSku(soldProductApi.getSku());
			soldProduct.setSoldPrice(soldProductApi.getSoldPrice());
			soldProduct.setSoldQuantity(soldProductApi.getSoldQuantity());
		});

		sale.setSoldProducts(soldProducts);

		saleRepository.save(sale);

		soldProductRepository.saveAll(soldProducts);

		return saleApi;
	}

	@Override
	public InvoiceApi generateInvoice(SaleApi sale)
	{
		InvoiceApi invoice = new InvoiceApi();

		invoice.setSale(sale);

		Company company = new Company();
		company = companyRepository.findByNit(companyNit);

		invoice.setCompanyApi(mapper.map(company, CompanyApi.class));

		return invoice;
	}

}
