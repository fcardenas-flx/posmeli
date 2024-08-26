package com.pos.meli.domain.model.workorder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "work_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrder
{
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "code", length = 255)
	private String code;

	@Column(name = "createdAt")
	private LocalDateTime createdAt;

	@Column(name = "updateAt")
	private LocalDateTime updateAt;

	@Column(name = "description", length = 255)
	private String description;

	@Column(name = "status", length = 255)
	private String status;

	@Column(name = "total_amount")
	private BigDecimal totalAmount;

	@Column(name = "total_amount_parts")
	private BigDecimal totalAmountParts;

	@Column(name = "total_amount_service")
	private BigDecimal totalAmountService;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "motorcycle_id", referencedColumnName = "id")
	private Motorcycle motorcycle;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "motorcycleOwner_id", referencedColumnName = "id")
	private MotorcycleOwner motorcycleOwner;

	@OneToMany(mappedBy="workOrder")
	private Set<WorkOrderPart> motorcycleParts;

	@OneToMany(mappedBy="workOrder")
	private Set<WorkOrderTechnicalService> technicalServices;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "technician_id", referencedColumnName = "id")
	private Technician technician;
}
