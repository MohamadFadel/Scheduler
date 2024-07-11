package com.wavemark.scheduler.fire.authentication.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class OAuth2Token implements Serializable {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String scope;

}
