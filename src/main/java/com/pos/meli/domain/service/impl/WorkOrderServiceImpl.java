package com.pos.meli.domain.service.impl;

import com.pos.meli.app.api.workorder.WorkOrderApi;
import com.pos.meli.domain.model.workorder.Motorcycle;
import com.pos.meli.domain.model.workorder.MotorcycleOwner;
import com.pos.meli.domain.model.workorder.WorkOrder;
import com.pos.meli.domain.repository.WorkOrderRepository;
import com.pos.meli.domain.service.WorkOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class WorkOrderServiceImpl implements WorkOrderService
{

	@Autowired WorkOrderRepository workOrderRepository;

	protected ModelMapper mapper;

	@PostConstruct
	protected void init()
	{
		mapper = new ModelMapper();
	}

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
			System.out.println("Error Obteniendo order by code " + e.getMessage());
		}

		return workOrderApi;
	}

	@Override
	public WorkOrderApi createWorkOrder(WorkOrderApi workOrderApi)
	{
		WorkOrder workOrder = new WorkOrder();
		WorkOrderApi workOrderApiResult = new WorkOrderApi();

		workOrder.setDescription(workOrderApi.getDescription());
		workOrder.setCreatedAt(LocalDateTime.now());

		Motorcycle motorcycle = new Motorcycle();

		motorcycle.setPatent(workOrderApi.getMotorcycle().getPatent());
		motorcycle.setReference(workOrderApi.getMotorcycle().getReference());
		motorcycle.setColor(workOrderApi.getMotorcycle().getColor());
		motorcycle.setModel(workOrderApi.getMotorcycle().getModel());

		MotorcycleOwner motorcycleOwner = new MotorcycleOwner();

		motorcycleOwner.setName(workOrderApi.getMotorcycleOwner().getName());
		motorcycleOwner.setLastName(workOrderApi.getMotorcycleOwner().getLastName());
		motorcycleOwner.setIdentification(workOrderApi.getMotorcycleOwner().getIdentification());
		motorcycleOwner.setPhoneNumber(workOrderApi.getMotorcycleOwner().getPhoneNumber());
		motorcycleOwner.setEmail(workOrderApi.getMotorcycleOwner().getEmail());

		workOrder.setMotorcycle(motorcycle);
		workOrder.setMotorcycleOwner(motorcycleOwner);

		WorkOrder workOrderSaved = new WorkOrder();

		try
		{
			//
			workOrderApiResult = mapper.map(workOrderRepository.save(workOrder), WorkOrderApi.class);
		}
		catch (Exception e)
		{
			System.out.println("Error Guardando la Orden " + e.getMessage());
		}

		return workOrderApiResult;
	}
}
