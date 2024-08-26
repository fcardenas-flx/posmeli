package com.pos.meli.app.rest;


import com.pos.meli.app.api.workorder.MotorcycleOwnerApi;
import com.pos.meli.domain.service.MotorcycleOwnerService;

import com.pos.meli.domain.util.ApiError;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 86400)
@RestController
@RequestMapping(path = "/services/owner")
public class MotorcycleOwnerController
{
	@Autowired
	private MotorcycleOwnerService motorcycleOwnerService;

	@Operation(summary = "get Motorcycle by patent", description = "get Motorcycle by patent")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get all products", content = @Content(schema = @Schema(implementation = MotorcycleOwnerApi.class))),
			@ApiResponse(responseCode = "400", description = "Bad request data.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Not found.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "Internal error.", content = @Content(schema = @Schema(implementation = ApiError.class))),
	})
	@GetMapping(path = "/getByIdentification", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getOwnerByIdentification(@RequestParam String identification)
			throws Exception
	{
		return new ResponseEntity<>(motorcycleOwnerService.getOwnerByIdentification(identification), HttpStatus.OK);
	}

	@Operation(summary = "get all motorcycles", description = "get all motorcycles")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get all Motorcycles", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MotorcycleOwnerApi.class)))),
			@ApiResponse(responseCode = "400", description = "Bad request data.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Not found.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "Internal error.", content = @Content(schema = @Schema(implementation = ApiError.class))),
	})
	@GetMapping(path = "/get/all", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAll()
			throws Exception
	{
		return new ResponseEntity<>(motorcycleOwnerService.getAllOwners(), HttpStatus.OK);
	}
}
