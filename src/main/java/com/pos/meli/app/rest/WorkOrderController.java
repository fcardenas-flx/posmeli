package com.pos.meli.app.rest;

import com.pos.meli.app.api.ProductApi;
import com.pos.meli.app.api.workorder.WorkOrderApi;

import com.pos.meli.app.rest.request.UpdateWorkOrderRequest;
import com.pos.meli.domain.service.WorkOrderService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 86400)
@RestController
@RequestMapping(path = "/services/work")
public class WorkOrderController
{
	@Autowired
	private WorkOrderService workOrderService;

	@Operation(summary = "get all work orders", description = "get all work orders")
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
		return new ResponseEntity<>(workOrderService.getAllWorkOrders(), HttpStatus.OK);
	}

	@Operation(summary = "get work order by code", description = "get work order by code")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get all products", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductApi.class)))),
			@ApiResponse(responseCode = "400", description = "Bad request data.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Not found.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "Internal error.", content = @Content(schema = @Schema(implementation = ApiError.class))),
	})
	@GetMapping(path = "/get/order/getByCode", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getOrderByCode(@RequestParam String code)
			throws Exception
	{
		return new ResponseEntity<>(workOrderService.getWorkOrderByCode(code), HttpStatus.OK);
	}

	@Operation(summary = "get work order by id", description = "get work order by code")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get all products", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductApi.class)))),
			@ApiResponse(responseCode = "400", description = "Bad request data.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Not found.", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "500", description = "Internal error.", content = @Content(schema = @Schema(implementation = ApiError.class))),
	})
	@GetMapping(path = "/get/order", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getOrderById(@RequestParam Long id)
			throws Exception
	{
		return new ResponseEntity<>(workOrderService.getWorkOrderById(id), HttpStatus.OK);
	}

	@PostMapping(path = "/save/order", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<WorkOrderApi> createWorkOrder(@RequestBody WorkOrderApi workOrderApi) {
		WorkOrderApi newWorkOrder = workOrderService.createWorkOrder(workOrderApi);
		return new ResponseEntity<>(newWorkOrder, HttpStatus.CREATED);
	}

	@PutMapping(path = "/update/order", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<WorkOrderApi> updateWorkOrder(@RequestParam Long id, @RequestBody UpdateWorkOrderRequest workOrderApi) {
		WorkOrderApi newWorkOrder = workOrderService.updateWorkOrder(id, workOrderApi);
		return new ResponseEntity<>(newWorkOrder, HttpStatus.CREATED);
	}
}
