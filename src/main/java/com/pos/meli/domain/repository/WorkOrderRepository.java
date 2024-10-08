package com.pos.meli.domain.repository;

import com.pos.meli.domain.model.workorder.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderRepository <T extends WorkOrder> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T>,
		PagingAndSortingRepository<T, Long>
{

	WorkOrder findByCode(String code);

	@Query("SELECT wo FROM WorkOrder wo ORDER BY wo.updateAt DESC")
	List<WorkOrder> findTop15ByOrderByUpdatedAtDesc();
}
