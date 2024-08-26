package com.pos.meli.domain.service.impl;


import com.pos.meli.app.api.workorder.MotorcycleOwnerApi;

import com.pos.meli.domain.model.workorder.MotorcycleOwner;
import com.pos.meli.domain.repository.MotorcycleOwnerRepository;
import com.pos.meli.domain.service.MotorcycleOwnerService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class MotorcycleOwnerServiceImpl implements MotorcycleOwnerService
{

	@Autowired
	MotorcycleOwnerRepository motorcycleOwnerRepository;

	protected ModelMapper mapper;

	@PostConstruct
	protected void init()
	{
		mapper = new ModelMapper();
	}

	@Override
	public MotorcycleOwnerApi getOwnerByIdentification(String identification)
	{
		MotorcycleOwnerApi motorcycleOwnerApi = new MotorcycleOwnerApi();

		MotorcycleOwner motorcycleOwner = motorcycleOwnerRepository.findByIdentification(identification);

		motorcycleOwnerApi = mapper.map(motorcycleOwner, MotorcycleOwnerApi.class);

		return motorcycleOwnerApi;
	}

	@Override
	public List<MotorcycleOwnerApi> getAllOwners()
	{
		return motorcycleOwnerRepository.findAll();
	}
}
