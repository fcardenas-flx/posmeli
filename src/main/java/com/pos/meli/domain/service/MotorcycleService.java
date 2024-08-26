package com.pos.meli.domain.service;

import com.pos.meli.app.api.workorder.MotorcycleApi;


import java.util.List;

public interface MotorcycleService
{

	MotorcycleApi getMotorcycleByPatent(String patent);

	List<MotorcycleApi> getAllMotorcycles();
}
