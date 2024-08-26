package com.pos.meli.domain.service.impl;

import com.pos.meli.app.api.workorder.MotorcycleApi;

import com.pos.meli.domain.model.workorder.Motorcycle;
import com.pos.meli.domain.repository.MotorcycleRepository;
import com.pos.meli.domain.service.MotorcycleService;
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
public class MotorcycleServiceImpl implements MotorcycleService
{
	@Autowired
	MotorcycleRepository motorcycleRepository;

	protected ModelMapper mapper;

	@PostConstruct
	protected void init()
	{
		mapper = new ModelMapper();
	}

	@Override
	public MotorcycleApi getMotorcycleByPatent(String patent)
	{
		MotorcycleApi motorcycleApi = new MotorcycleApi();

		Motorcycle motorcycle = motorcycleRepository.findByPatent(patent);

		motorcycleApi = mapper.map(motorcycle, MotorcycleApi.class);

		return motorcycleApi;
	}

	@Override
	public List<MotorcycleApi> getAllMotorcycles()
	{
		return motorcycleRepository.findAll();
	}
}
