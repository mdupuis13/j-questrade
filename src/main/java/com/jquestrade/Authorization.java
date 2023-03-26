package com.jquestrade;

import lombok.Getter;

/** Represents the data that allows an application to make requests the Questrade API. */
@Getter
public class Authorization {
	
	/** Creates a Authorization object using cached data. */
	public Authorization(String refreshToken, String accessToken, String apiServer) {
		this.refresh_token = refreshToken;
		this.access_token = accessToken;
		this.api_server = apiServer;
		this.expires_in = 1800; // I've always seen it 1800 seconds
		this.token_type = "Bearer"; // Access token type is always Bearer
	}

	/**
	 * The access token. Expires in 30 minutes after generation.
	 * Only works with the associated API server, see {@link #api_server}
	 */
	private final String access_token;

	/**
	 * The URL of the API server assigned to the access token.
	 */
	private final String api_server;

	/**
	 * Returns how long after generation the access token will expire.
	 * It appears to always be {@code 1800} seconds (30 minutes).
	 */
	private final int expires_in;

	/**
	 * The new refresh token, which can be used to generate a new {@code Authorization}.
	 */
	private final String refresh_token;

	/**
	 * The access token type. Is always <b>Bearer</b>
	 */
	private final String token_type;


}
