package com.pos.meli.domain.service.impl;

import com.pos.meli.app.api.workorder.WorkOrderApi;
import com.pos.meli.domain.model.workorder.WorkOrder;
import com.pos.meli.domain.repository.WorkOrderRepository;
import com.pos.meli.domain.service.WorkOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class WorkOrderServiceImpl implements WorkOrderService
{

	@Autowired WorkOrderRepository workOrderRepository;

	protected ModelMapper mapper;

	@Override
	public List<WorkOrderApi> getAllWorkOrders()
	{
		return workOrderRepository.findAll();
	}

	@Override
	public WorkOrderApi getWorkOrderByCode(String code)
	{
		WorkOrderApi workOrderApi = new WorkOrderApi();

		try
		{
			WorkOrder workOrder = workOrderRepository.findByCode(code);

			workOrderApi = mapper.map(workOrder, WorkOrderApi.class);
		}
		catch (Exception e)
		{

		}

		return workOrderApi;
	}
}
