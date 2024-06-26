package com.watsoo.dms.restclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class HttpUtility {

	private String traccarBaseUrl;

	private String traccarUserName;

	private String traccarUserPassword;


	public HttpUtility() {
		super();
		
	}

	public HttpUtility(String traccarBaseUrl, String traccarUserName, String traccarUserPassword) {
		super();
		this.traccarBaseUrl = traccarBaseUrl;
		this.traccarUserName = traccarUserName;
		this.traccarUserPassword = traccarUserPassword;
	}

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public String httpGet(String url) {
		ResponseEntity<String> responseEntity = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(traccarUserName, traccarUserPassword);
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("cache-control", "no-cache");

			HttpEntity<String> entity = new HttpEntity<>(headers);
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			if (!responseEntity.getStatusCode().is2xxSuccessful()) {
				return null;
			} else {
				return responseEntity.getBody();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// encode url which browser accepts
	public String encodeUrl(String url) {
		// to encode dataname
		if (url.contains(" "))
			url = url.replace(" ", "%20");
		if (url.contains("'"))
			url = url.replace("'", "%27");

		return url;
	}

}
