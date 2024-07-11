package com.wavemark.scheduler.cucumber.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemark.scheduler.fire.authentication.dto.OAuth2Token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${LOGIN_URL}")
    private String webAppUrl;

    @Value("${USERNAME}")
    private String username;

    @Value("${PASSWORD}")
    private String password;

    private final RestTemplate restTemplate;

    private String getIDPToken() {
        HttpHeaders headers = new HttpHeaders();

        headers.setBasicAuth("WaveMarkIDP", "IJjWrIiC3Acql389Imq519kXfOk4I2");

        String url = webAppUrl + "/iam/oauth2/token?username=" + username
                + "&password=" + password
                + "&grant_type=password";

        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    public String getWebAppToken() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("WebAppHospital", "zR6g8wpXwneMTkAvOOcEbdJg1FDsnE");

        String idToken = getIDPToken();

        String url = webAppUrl + "/warden/oauth/token?username=" + username
                + "&password=" + password
                + "&grant_type=password" + "&id_token=" + idToken;

        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        OAuth2Token token = mapper.readValue(response.getBody(), OAuth2Token.class);

        return token.getAccess_token();
    }

}
