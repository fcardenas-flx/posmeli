package com.pos.meli.domain.repository;

import com.pos.meli.domain.model.workorder.Motorcycle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotorcycleRepository <T extends Motorcycle> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T>,
		PagingAndSortingRepository<T, Long>
{
	Motorcycle findByPatent(String patent);

}
