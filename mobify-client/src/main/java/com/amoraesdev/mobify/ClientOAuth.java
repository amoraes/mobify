package com.amoraesdev.mobify;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
@Scope("singleton")
public class ClientOAuth {

	@Value("${security.oauth2.client.client-id}")	
	private String clientId;
	
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	
	@Value("${security.oauth2.client.access-token-uri}")
	private String oauthEndpoint;
	
	@Value("${security.oauth2.client.instrospect-uri}")
	private String oauthIntrospectEndpoint;
	
	private String accessToken;
	
	private Long expiryTimeMills; 
	
	public ClientOAuth() {
	}
	
	private void autenticar() {
    	try {
			HttpResponse<JsonNode> jsonResponse = Unirest.post(oauthEndpoint)
					.basicAuth(this.clientId, this.clientSecret)
					.field("grant_type", "client_credentials")
					.field("client_id", this.clientId)
					.field("client_secret", clientSecret).asJson();
			if(jsonResponse.getStatus() == 401){
				throw new RuntimeException("OAuth error: invalid Client Id or Client Secret");
			}
			else if(jsonResponse.getStatus() == 500){
				throw new RuntimeException("OAuth error: unexpected server fault");	
			}
			else if(jsonResponse.getStatus() == 200){
				JSONObject json = new JSONObject(jsonResponse.getBody().toString());
				this.accessToken = json.getString("access_token");
				if(this.accessToken.equals("")){
					throw new RuntimeException("OAuth error: could not retrieve the Access Token");
				}else{
					setExpiryTime();
				}
			}else{
				throw new RuntimeException("OAuth error: unexpected server fault");
			}
		} catch (UnirestException e) {
			throw new RuntimeException("OAuth error: unexpected server fault", e);
		}
    }
	
	private void setExpiryTime() throws UnirestException {
		//consulta no introspect para saber quando irá expirar
		HttpResponse<JsonNode> jsonIntrospectResponse = Unirest.post(oauthIntrospectEndpoint)
				.basicAuth(this.clientId, this.clientSecret)
				.field("token", this.accessToken)
				.asJson();
		if(jsonIntrospectResponse.getStatus() == 200){
			Long expiryTimeString = jsonIntrospectResponse.getBody().getObject().getLong("exp");
			expiryTimeMills = new Long(expiryTimeString) * 1000;
		}else{
			throw new RuntimeException("OAuth error: error retrieving data from Introspect URL");
		}
	}

	public String getAccessToken() {
		long nowMills = new Date().getTime();
		if(accessToken == null || (expiryTimeMills != null && expiryTimeMills < nowMills)){
			autenticar();
		}
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public void reset(){
		this.accessToken = null;
		this.expiryTimeMills = null;
	}
	
	public String getClientId(){
		return this.clientId;
	}
}
