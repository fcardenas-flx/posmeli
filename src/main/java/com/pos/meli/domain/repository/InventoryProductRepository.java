package com.pos.meli.domain.repository;

import com.pos.meli.domain.model.IncomingProduct;
import com.pos.meli.domain.model.InventoryProduct;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryProductRepository <T extends InventoryProduct> extends JpaRepository<T, Long>,
		JpaSpecificationExecutor<T>,
		PagingAndSortingRepository<T, Long>
{

	List<InventoryProduct> findAllByReference(String reference);
}
