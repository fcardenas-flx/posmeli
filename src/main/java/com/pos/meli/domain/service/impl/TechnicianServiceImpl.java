package com.pos.meli.domain.service.impl;

import com.pos.meli.app.api.workorder.TechnicianApi;
import com.pos.meli.domain.repository.TechnicianRepository;
import com.pos.meli.domain.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class TechnicianServiceImpl implements TechnicianService
{
	@Autowired
	TechnicianRepository technicianRepository;

	@Override
	public List<TechnicianApi> getAllTechnicians()
	{
		return technicianRepository.findAll();
	}
}
