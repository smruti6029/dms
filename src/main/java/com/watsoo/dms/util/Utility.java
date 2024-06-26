package com.watsoo.dms.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.watsoo.dms.repository.CredentialMasterRepository;

/**
 * This class provides utility function for sending HTTP POST, PATCH, DELETE
 * request to a different endpoint
 */
@Component
@SuppressWarnings("unused")
public class Utility {

	@Autowired
	private CredentialMasterRepository credentialMasterRepository;

	public static HttpHeaders createHeaders(String username, String password) {
		return new HttpHeaders() {
			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				set("Authorization", authHeader);
			}
		};
	}
	
	public static List<Long> jsonToList(String json) {
		
		//
		
		ObjectMapper objectMapper=new ObjectMapper();
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Long.class));
        } catch (JsonProcessingException e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }
	
	public static List<String> convertStringToArray(String evidencePhotos) {
		String[] urlArray = evidencePhotos.split(",");
		List<String> files = new ArrayList<>();

		for (String url : urlArray) {
			files.add(url);
		}
		return files;
	}
	
}