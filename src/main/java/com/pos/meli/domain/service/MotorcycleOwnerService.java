package com.pos.meli.domain.service;

import com.pos.meli.app.api.workorder.MotorcycleApi;
import com.pos.meli.app.api.workorder.MotorcycleOwnerApi;

import java.util.List;

public interface MotorcycleOwnerService
{
	MotorcycleOwnerApi getOwnerByIdentification(String identification);

	List<MotorcycleOwnerApi> getAllOwners();
}
