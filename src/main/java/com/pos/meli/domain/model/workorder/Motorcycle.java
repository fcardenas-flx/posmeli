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

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "motorcycles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Motorcycle
{
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "patent", unique = true, nullable = false, length = 10)
	private String patent;

	@Column(name = "brand", length = 255)
	private String brand;

	@Column(name = "reference", length = 255)
	private String reference;

	@Column(name = "color", length = 40)
	private String color;

	@Column(name = "model", length = 10)
	private String model;
}
