package com.pos.meli.domain.model.workorder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

	@Column(name = "name", length = 255)
	private String code;

	@Column(name = "createdAt")
	private LocalDateTime createdAt;

	@Column(name = "updateAt")
	private LocalDateTime updateAt;

	@Column(name = "description", length = 255)
	private int description;

	@Column(name = "total_amount")
	private BigDecimal totalAmount;
}
