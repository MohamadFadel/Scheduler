package com.wavemark.scheduler.fire.httpinvoker.requestor;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthToken implements Serializable {

	private String access_token;
	private String token_type;
	private String refresh_token;
	private String expires_in;
	private String scope;
}
