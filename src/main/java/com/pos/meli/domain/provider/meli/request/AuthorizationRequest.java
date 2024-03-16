package com.pos.meli.domain.provider.meli.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationRequest
{
	@JsonProperty("grant_type")
	public String grantType;

	@JsonProperty("client_id")
	public String clientId;

	@JsonProperty("client_secret")
	public String clientSecret;

	@JsonProperty("refresh_token")
	public String refreshToken;
}
