package com.pos.meli.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pos.meli.domain.util.BigDecimalMoneySerializer;
import io.swagger.v3.oas.annotations.media.Schema;
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
import javax.persistence.MapsId;
import javax.persistence.Table;

import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "Sold_Products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoldProduct
{

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "meli_id", length = 255)
	private String meliId;

	@ManyToOne(optional=false)
	@JoinColumn(name = "sale_id")
	private Sale sale;

	@Column(name = "name", length = 255)
	private String name;

	@Column(name = "sku", length = 255)
	private String sku;

	@Column(name = "sold_quantity", length = 255)
	private int soldQuantity;

	@Schema(description = "sold_price")
	private BigDecimal soldPrice;

}
