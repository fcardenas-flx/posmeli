package com.pos.meli.app.rest;

import com.pos.meli.app.api.workorder.WorkOrderApi;

import com.pos.meli.domain.service.TechnicianService;
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
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 86400)
@RestController
@RequestMapping(path = "/services/technician")
public class TechnicianController
{
	@Autowired
	TechnicianService technicianService;

	@Operation(summary = "get all technicians", description = "get all technicians")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get all work orders", content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkOrderApi.class)))),
			@ApiResponse(responseCode = "400", description = "Bad request data.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Not found.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "Internal error.", content = @Content(schema = @Schema(implementation = ApiError.class))),
	})
	@GetMapping(path = "/get/all", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAll()
			throws Exception
	{
		return new ResponseEntity<>(technicianService.getAllTechnicians(), HttpStatus.OK);
	}
}
