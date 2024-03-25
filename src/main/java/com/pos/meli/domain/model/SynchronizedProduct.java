package com.pos.meli.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pos.meli.domain.util.BigDecimalMoneySerializer;
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

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "syncronized_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SynchronizedProduct
{
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "sku")
	private String sku;

	@Column(name = "quantity")
	private int quantity;

	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	private BigDecimal purchasePrice;

	@JsonSerialize(using = BigDecimalMoneySerializer.class)
	private BigDecimal salePrice;

	@Column(name = "process_id", nullable = false, length = 255)
	private String processId;
}
