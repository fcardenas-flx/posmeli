package com.pos.meli.domain.repository;

import com.pos.meli.domain.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository <T extends Seller> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T>,
		PagingAndSortingRepository<T, Long>
{

	Seller findByName(String supplier);
}
