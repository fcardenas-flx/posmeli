package com.pos.meli.domain.service;

import com.pos.meli.app.api.InvoiceApi;
import com.pos.meli.app.api.SaleApi;

public interface SaleService
{

	SaleApi performSell(SaleApi request);

	InvoiceApi generateInvoice(SaleApi sale);
}
