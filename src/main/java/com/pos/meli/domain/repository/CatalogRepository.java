package com.pos.meli.domain.repository;

import com.pos.meli.domain.model.catalog.Catalog;
import com.pos.meli.domain.model.catalog.Consecutive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository<T extends Catalog> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T>,
		PagingAndSortingRepository<T, Long>
{

	Consecutive findConsecutiveByCode(String code);

}
