package com.pos.meli.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@MappedSuperclass
public abstract class Person
{
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "identification", nullable = false, length = 100)
	private String identification;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "email", nullable = false, length = 255)
	private String email;

	@Column(name = "phone_number", nullable = false, length = 100)
	private String phoneNumber;
}
