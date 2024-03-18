package com.pos.meli.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends Person
{
	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "meli_account_id", referencedColumnName = "id")
	MeliAccount meliAccount;
}
