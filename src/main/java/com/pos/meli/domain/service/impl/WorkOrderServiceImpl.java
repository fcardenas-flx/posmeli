package com.pos.meli.domain.service.impl;

import com.pos.meli.app.api.workorder.WorkOrderApi;
import com.pos.meli.domain.repository.WorkOrderRepository;
import com.pos.meli.domain.service.WorkOrderService;
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

	@Override
	public List<WorkOrderApi> getAllWorkOrders()
	{
		return workOrderRepository.findAll();
	}
}
