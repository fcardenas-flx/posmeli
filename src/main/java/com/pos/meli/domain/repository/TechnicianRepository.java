package com.pos.meli.domain.repository;


import com.pos.meli.domain.model.workorder.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicianRepository <T extends Technician> extends JpaRepository<T, Long>,
		JpaSpecificationExecutor<T>,
		PagingAndSortingRepository<T, Long>
{

	Technician findByIdentification(String identification);
}
