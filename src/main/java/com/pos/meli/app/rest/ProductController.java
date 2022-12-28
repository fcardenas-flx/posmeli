package com.pos.meli.app.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 86400)
@RestController
@RequestMapping(path = "/services/products")
public class ProductController
{
	@Operation(summary = "get all products", description = "get all products")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "Posted data File with card program excluded.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = product.class)))),
////			@ApiResponse(responseCode = "400", description = "Bad request data.", content = @Content(schema = @Schema(implementation = ApiError.class))),
////			@ApiResponse(responseCode = "404", description = "Not found.", content = @Content(schema = @Schema(implementation = ApiError.class))),
////			@ApiResponse(responseCode = "500", description = "Internal error.", content = @Content(schema = @Schema(implementation = ApiError.class))),
//	})
	@GetMapping(path = "/get/allproducts", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAllProducts()
			throws Exception
	{
		return new ResponseEntity<>("fileService.getPostedWithCardProgramExcluded()", HttpStatus.OK);
	}

}
