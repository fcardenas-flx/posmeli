package com.pos.meli.domain.model.workorder.catalog;

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

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "motorcycle_parts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MotorcyclePart
{
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "code", length = 20)
	private String code;

	@Column(name = "reference", length = 255)
	private String reference;

	@Column(name = "name", length = 255)
	private String name;

	@Column(name = "brand", length = 100)
	private String brand;
}
