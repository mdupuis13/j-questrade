package com.jquestrade.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the data that allows an application to make requests the Questrade API.
 *
 * @param access_token  The access token.
 *                      Only works with the associated API server, see {@link #api_server}
 * @param api_server    The URL of the API server assigned to the access token.
 * @param expires_in    Returns how long after generation the access token will expire.
 *                      It appears to always be {@code 1800} seconds (30 minutes).
 * @param refresh_token The new refresh token, which can be used to generate a new {@code Authorization}.
 * @param token_type    The access token type. Is always <b>Bearer</b>
 */
public record Authorization(String access_token,
							String api_server,
							int expires_in,
							String refresh_token,
                            String token_type) {
	public Authorization(String access_token,
						 String api_server,
						 String refresh_token){
		this(access_token,api_server,1800,refresh_token,"Bearer");
	}

	}
