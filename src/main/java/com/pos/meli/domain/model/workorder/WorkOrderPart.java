package com.pos.meli.domain.model.workorder;

import com.pos.meli.domain.model.workorder.catalog.MotorcyclePart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.Table;

import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "work_order_motorcycle_parts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrderPart
{
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "motorcyclePart_id", referencedColumnName = "id")
	private MotorcyclePart motorcyclePart;

	@Column(name = "subTotal")
	private BigDecimal subTotal;

	@ManyToOne
	@JoinColumn(name="workOrder_id", nullable=false)
	private WorkOrder workOrder;
}
