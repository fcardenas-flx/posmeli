package com.pos.meli.domain.model;

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
@Table(name = "meli_credentials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeliApiCredential
{
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "clientId", nullable = false, length = 255)
	private String clientId;

	@Column(name = "clientSecret", nullable = false, length = 255)
	private String clientSecret;

	@Column(name = "refreshToken", nullable = false, length = 255)
	private String refreshToken;

	@Column(name = "grantType", nullable = false, length = 255)
	private String grantType;

}
