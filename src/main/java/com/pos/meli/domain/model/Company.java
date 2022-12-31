package com.pos.meli.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "company")
public class Company
{
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name", length = 255)
	private String name;

	@Column(name = "nit", length = 255)
	private String nit;

	@Column(name = "address", length = 255)
	private String address;

	@Column(name = "phone_number", length = 255)
	private String phoneNumber;

	@Column(name = "email", length = 255)
	private String email;

	@Column(name = "image", length = 255)
	private String image;
}
