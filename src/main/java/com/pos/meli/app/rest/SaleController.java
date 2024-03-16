package com.pos.meli.app.rest;

import com.pos.meli.app.api.InvoiceApi;
import com.pos.meli.app.api.SaleApi;
import com.pos.meli.domain.service.SaleService;
import com.pos.meli.domain.util.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 86400)
@RestController
@RequestMapping(path = "/services/sale")
public class SaleController
{

	@Autowired
	private SaleService saleService;

	@Operation(summary = "Perform Sell", description = "Perform Sell")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Products Saved", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvoiceApi.class)))),
			@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "406", description = "Invalid request", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "Internal Error.", content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@PostMapping(path = "/perform/sell", produces = { MediaType.APPLICATION_JSON_VALUE },
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InvoiceApi> performSell(
			@Parameter(description = "Sell request", required = true) @RequestBody SaleApi request)
			throws Exception
	{
		SaleApi sale = saleService.performSell(request);

		InvoiceApi invoice = saleService.generateInvoice(sale);

		return new ResponseEntity<>(invoice, HttpStatus.OK);
	}


}
