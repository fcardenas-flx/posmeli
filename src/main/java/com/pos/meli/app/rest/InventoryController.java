package com.pos.meli.app.rest;

import com.pos.meli.app.api.ProductApi;
import com.pos.meli.domain.service.InventoryService;
import com.pos.meli.domain.util.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 86400)
@RestController
@RequestMapping(path = "/services/inventory")
public class InventoryController
{

	@Autowired
	private InventoryService inventoryService;

	@Operation(summary = "get all products", description = "get all products")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get all products", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductApi.class)))),
			@ApiResponse(responseCode = "400", description = "Bad request data.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Not found.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "Internal error.", content = @Content(schema = @Schema(implementation = ApiError.class))),
	})
	@GetMapping(path = "/get/allproducts", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAllProducts()
			throws Exception
	{
		return new ResponseEntity<>(inventoryService.getAllProducts(), HttpStatus.OK);
	}

	@Operation(summary = "get all Meli products", description = "get all Meli products")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get all Meli products", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductApi.class)))),
			@ApiResponse(responseCode = "400", description = "Bad request data.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Not found.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "Internal error.", content = @Content(schema = @Schema(implementation = ApiError.class))),
	})
	@GetMapping(path = "/get/allMeliProducts", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAllMeliProducts()
			throws Exception
	{
		return new ResponseEntity<>(inventoryService.getAllMeliProducts(), HttpStatus.OK);
	}

	@Operation(summary = "get product by Meli Id", description = "get product by Sku")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get all products", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductApi.class)))),
			@ApiResponse(responseCode = "400", description = "Bad request data.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Not found.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "Internal error.", content = @Content(schema = @Schema(implementation = ApiError.class))),
	})
	@GetMapping(path = "/get/product", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getProductBySku(@RequestParam String meliId)
			throws Exception
	{
		return new ResponseEntity<>(inventoryService.getProductById(meliId), HttpStatus.OK);
	}

	@Operation(summary = "sync products", description = "sync products")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sync all products", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductApi.class)))),
			@ApiResponse(responseCode = "400", description = "Bad request data.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Not found.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "Internal error.", content = @Content(schema = @Schema(implementation = ApiError.class))),
	})
	@GetMapping(path = "/sync/products", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> syncProducts()
			throws Exception
	{
		return new ResponseEntity<>(inventoryService.syncProducts(), HttpStatus.OK);
	}

	@Operation(summary = "Save Products", description = "Save Products")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Products Saved", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductApi.class)))),
			@ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "406", description = "Invalid request", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "Internal Error.", content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@PostMapping(path = "/save/products", produces = { MediaType.APPLICATION_JSON_VALUE },
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductApi>> saveProducts(
			@Parameter(description = "Products request", required = true) @RequestBody List<ProductApi> request)
			throws Exception
	{
		List<ProductApi> productsSaved = inventoryService.saveProducts(request);
		return new ResponseEntity<>(productsSaved, HttpStatus.OK);
	}

}
