package com.pos.meli.domain.service;

import com.pos.meli.app.api.workorder.WorkOrderApi;
import com.pos.meli.app.rest.request.UpdateWorkOrderRequest;

import java.util.List;

public interface WorkOrderService
{
	List<WorkOrderApi> getAllWorkOrders();

	WorkOrderApi getWorkOrderByCode(String code);

	WorkOrderApi createWorkOrder(WorkOrderApi workOrderApi);

	WorkOrderApi getWorkOrderById(Long id);

	WorkOrderApi updateWorkOrder(Long id, UpdateWorkOrderRequest workOrderApi);
}
