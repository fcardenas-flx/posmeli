package com.pos.meli.domain.model;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "meli_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeliAccount
{
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "siteId", nullable = false, length = 255)
	private String siteId;

	@Column(name = "nickname", nullable = false, length = 255)
	private String nickname;

	@Column(name = "userId", nullable = false, length = 255)
	private String userId;

	@Column(name = "sellerId", nullable = false, length = 255)
	private String sellerId;

	@Column(name = "url", nullable = false, length = 255)
	private String url;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "meli_credential_id", referencedColumnName = "id")
	MeliApiCredential meliApiCredential;
}
