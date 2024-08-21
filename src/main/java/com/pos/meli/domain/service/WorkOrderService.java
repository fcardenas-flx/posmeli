package com.pos.meli.domain.service;

import com.pos.meli.app.api.workorder.WorkOrderApi;

import java.util.List;

public interface WorkOrderService
{
	List<WorkOrderApi> getAllWorkOrders();

	WorkOrderApi getWorkOrderByCode(String code);
}
