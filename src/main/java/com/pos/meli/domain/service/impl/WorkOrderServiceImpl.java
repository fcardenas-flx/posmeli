package com.pos.meli.domain.service.impl;

import com.pos.meli.app.api.workorder.WorkOrderApi;
import com.pos.meli.app.rest.request.UpdateWorkOrderRequest;
import com.pos.meli.domain.model.workorder.Motorcycle;
import com.pos.meli.domain.model.workorder.MotorcycleOwner;
import com.pos.meli.domain.model.workorder.Technician;
import com.pos.meli.domain.model.workorder.WorkOrder;
import com.pos.meli.domain.repository.MotorcycleOwnerRepository;
import com.pos.meli.domain.repository.MotorcycleRepository;
import com.pos.meli.domain.repository.TechnicianRepository;
import com.pos.meli.domain.repository.WorkOrderRepository;
import com.pos.meli.domain.service.WorkOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class WorkOrderServiceImpl implements WorkOrderService
{

	@Autowired
	WorkOrderRepository workOrderRepository;

	@Autowired
	MotorcycleRepository motorcycleRepository;

	@Autowired
	TechnicianRepository technicianRepository;

	@Autowired
	MotorcycleOwnerRepository motorcycleOwnerRepository;

	protected ModelMapper mapper;

	@PostConstruct
	protected void init()
	{
		mapper = new ModelMapper();
	}

	@Override
	public List<WorkOrderApi> getAllWorkOrders()
	{
		return workOrderRepository.findTop15ByOrderByUpdatedAtDesc();
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
		workOrder.setUpdateAt(LocalDateTime.now());

		workOrder.setTotalAmount(BigDecimal.valueOf(0));
		workOrder.setTotalAmountParts(BigDecimal.valueOf(0));
		workOrder.setTotalAmountService(BigDecimal.valueOf(0));

		Motorcycle existingMotorcycle = new Motorcycle();

		existingMotorcycle = motorcycleRepository.findByPatent(workOrderApi.getMotorcycle().getPatent());

		if (existingMotorcycle != null)
		{
			existingMotorcycle.setId(existingMotorcycle.getId());
			existingMotorcycle.setPatent(existingMotorcycle.getPatent());
			existingMotorcycle.setReference(existingMotorcycle.getReference());
			existingMotorcycle.setColor(existingMotorcycle.getColor());
			existingMotorcycle.setModel(existingMotorcycle.getModel());
			workOrder.setMotorcycle(existingMotorcycle);
		}
		else
		{
			Motorcycle motorcycle = new Motorcycle();
			motorcycle.setPatent(workOrderApi.getMotorcycle().getPatent());
			motorcycle.setReference(workOrderApi.getMotorcycle().getReference());
			motorcycle.setColor(workOrderApi.getMotorcycle().getColor());
			motorcycle.setModel(workOrderApi.getMotorcycle().getModel());
			workOrder.setMotorcycle(motorcycle);
		}

		MotorcycleOwner existingMotorcycleOwner = new MotorcycleOwner();

		existingMotorcycleOwner = motorcycleOwnerRepository.findByIdentification(
				workOrderApi.getMotorcycleOwner().getIdentification());

		if (existingMotorcycleOwner != null)
		{
			existingMotorcycleOwner.setId(existingMotorcycleOwner.getId());
			existingMotorcycleOwner.setName(existingMotorcycleOwner.getName());
			existingMotorcycleOwner.setLastName(existingMotorcycleOwner.getLastName());
			existingMotorcycleOwner.setIdentification(existingMotorcycleOwner.getIdentification());
			existingMotorcycleOwner.setPhoneNumber(existingMotorcycleOwner.getPhoneNumber());
			existingMotorcycleOwner.setEmail(existingMotorcycleOwner.getEmail());
			workOrder.setMotorcycleOwner(existingMotorcycleOwner);
		}
		else
		{
			MotorcycleOwner motorcycleOwner = new MotorcycleOwner();
			motorcycleOwner.setName(workOrderApi.getMotorcycleOwner().getName());
			motorcycleOwner.setLastName(workOrderApi.getMotorcycleOwner().getLastName());
			motorcycleOwner.setIdentification(workOrderApi.getMotorcycleOwner().getIdentification());
			motorcycleOwner.setPhoneNumber(workOrderApi.getMotorcycleOwner().getPhoneNumber());
			motorcycleOwner.setEmail(workOrderApi.getMotorcycleOwner().getEmail());
			workOrder.setMotorcycleOwner(motorcycleOwner);
		}

		workOrder.setStatus("Creada");

		Technician existingTechnician = new Technician();

		existingTechnician = technicianRepository.findByIdentification("-");

		if (existingTechnician != null)
		{
			existingTechnician.setId(existingTechnician.getId());
			existingTechnician.setName(existingTechnician.getName());
			existingTechnician.setIdentification(existingTechnician.getIdentification());
			existingTechnician.setPhoneNumber(existingTechnician.getPhoneNumber());
			existingTechnician.setLastName(existingTechnician.getLastName());
			workOrder.setTechnician(existingTechnician);
		}
		else
		{
			Technician technician = new Technician();
			technician.setName("Por Asignar");
			technician.setIdentification("-");
			technician.setPhoneNumber("-");
			workOrder.setTechnician(technician);
		}

		if (workOrderApi.getDescription()!= null)
		{
			workOrder.setDescription(workOrderApi.getDescription());
		}
		else
		{
			workOrder.setDescription("-");
		}

		WorkOrder workOrderSaved = new WorkOrder();

		try
		{
			workOrderApiResult = mapper.map(workOrderRepository.save(workOrder), WorkOrderApi.class);
		}
		catch (Exception e)
		{
			System.out.println("Error Guardando la Orden " + e.getMessage());
		}

		return workOrderApiResult;
	}

	@Override
	public WorkOrderApi getWorkOrderById(Long id)
	{
		WorkOrderApi workOrderApi = new WorkOrderApi();

		try
		{
			Optional workOrder = workOrderRepository.findById(id);

			workOrderApi = mapper.map(workOrder, WorkOrderApi.class);
		}
		catch (Exception e)
		{
			System.out.println("Error Obteniendo order by code " + e.getMessage());
		}

		return workOrderApi;
	}

	@Override
	public WorkOrderApi updateWorkOrder(Long id, UpdateWorkOrderRequest workOrderApi)
	{
		WorkOrder workOrder = new WorkOrder();

		WorkOrderApi workOrderApiResult = new WorkOrderApi();

		Optional<WorkOrder> existingOrder = workOrderRepository.findById(id);

		if (existingOrder.isPresent())
		{
			try
			{
				workOrder.setId(existingOrder.get().getId());

				workOrder.setUpdateAt(LocalDateTime.now());
				workOrder.setStatus(workOrderApi.getStatus());


				Optional<Technician>  existingTechnician = technicianRepository.findById(workOrderApi.getTechnician().getId());

				if (existingTechnician.isPresent())
				{
					Technician technician = new Technician();

					technician.setId(existingTechnician.get().getId());
					technician.setName(existingTechnician.get().getName());
					technician.setIdentification(existingTechnician.get().getIdentification());
					technician.setPhoneNumber(existingTechnician.get().getPhoneNumber());
					technician.setLastName(existingTechnician.get().getLastName());

					workOrder.setTechnician(technician);
				}

				workOrder.setDescription(workOrderApi.getDescription());

				workOrder.setCode(existingOrder.get().getCode());
				workOrder.setTotalAmount(workOrderApi.getTotalAmount());
				workOrder.setTotalAmountParts(workOrderApi.getTotalAmountParts());
				workOrder.setTotalAmountService(workOrderApi.getTotalAmountService());

				workOrder.setMotorcycleOwner(existingOrder.get().getMotorcycleOwner());
				workOrder.setMotorcycle(existingOrder.get().getMotorcycle());
				workOrder.setCreatedAt(existingOrder.get().getCreatedAt());

				workOrder.setTechnicalServices(existingOrder.get().getTechnicalServices());

				workOrder.setMotorcycleParts(existingOrder.get().getMotorcycleParts());

				workOrder = (WorkOrder) workOrderRepository.save(workOrder);
			}
			catch (Exception e)
			{
				System.out.println("Error Actualizando la Orden " + e.getMessage());
			}

		}
		else
		{
			System.out.println("Orden No se puede actualizar, Orden no Existente");
		}

		workOrderApiResult = mapper.map(workOrder, WorkOrderApi.class);

		return workOrderApiResult;
	}
}
