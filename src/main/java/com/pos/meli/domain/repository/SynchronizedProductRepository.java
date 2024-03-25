package com.pos.meli.domain.repository;

import com.pos.meli.domain.model.SynchronizedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SynchronizedProductRepository <T extends SynchronizedProduct> extends JpaRepository<T, Long>,
		JpaSpecificationExecutor<T>,
		PagingAndSortingRepository<T, Long>
{


	List<SynchronizedProduct> findAllByProcessId(String processId);
}
