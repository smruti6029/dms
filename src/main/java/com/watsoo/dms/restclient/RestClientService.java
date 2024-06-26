package com.watsoo.dms.restclient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.watsoo.dms.constant.HttpApi;
import com.watsoo.dms.entity.CommandSendDetails;
import com.watsoo.dms.entity.Configuration;
import com.watsoo.dms.repository.ConfigurationRepository;

@Component
public class RestClientService {

	RestTemplate restTemplate = new RestTemplate();

	@Value("${external.service.username}")
	private String username;

	@Value("${external.service.password}")
	private String password;

	@Value("${external.service.baseurl.event}")
	private String baseUrl;

	@Value("${file.dawnload.url}")
	String fileUrl;

	Logger logger = LoggerFactory.getLogger(RestClientService.class);

	@Autowired
	private ConfigurationRepository configurationRepository;

	public String fetchEventDataFromExternalService(List<Integer> deviceId, String type, String fromTime,
			String toTime) {

		ResponseEntity<String> responseEntity = null;
		try {
//			String eventUrl = baseUrl + "/api/reports/events";
			String eventUrl = baseUrl + HttpApi.EVENTS_DETALIS_FEATH_END_POINT;
			String url = buildUrl(eventUrl, deviceId, type, fromTime, toTime);
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(username, password);
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

	private String buildUrl(String baseUrl, List<Integer> deviceIds, String type, String fromTime, String toTime)
			throws UnsupportedEncodingException {
		StringBuilder url = new StringBuilder(baseUrl);
		url.append("?");

		for (int i = 0; i < deviceIds.size(); i++) {
			url.append("deviceId=");
			url.append(URLEncoder.encode(String.valueOf(deviceIds.get(i)), StandardCharsets.UTF_8.toString()));
			if (i < deviceIds.size() - 1) {
				url.append("&");
			}
		}
		url.append("&type=").append(URLEncoder.encode(type, StandardCharsets.UTF_8.toString()));
		url.append("&from=").append(fromTime);
		url.append("&to=").append(toTime);

		return url.toString();
	}

	public String getPositions(List<Long> positionIdList) {
		try {
			ResponseEntity<String> responseEntity = null;
			String positionsEndpoint = "/api/positions?id=";
			String url = constructUrl(positionIdList, HttpApi.POISITIONS_DETALIS_FEATH_END_POINT);

			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(username, password);
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
			return null;
		}

	}

	private String constructUrl(List<Long> positionIdList, String endPoint) {
		if (positionIdList == null || positionIdList.isEmpty()) {

			return baseUrl + endPoint;
		}

		// Construct the URL dynamically based on positionIdList
		StringBuilder urlBuilder = new StringBuilder(baseUrl + endPoint);
		for (int i = 0; i < positionIdList.size(); i++) {
			urlBuilder.append(positionIdList.get(i));
			// Append "&id=" for all elements except the last one
			if (i < positionIdList.size() - 1) {
				urlBuilder.append("&id=");
			}
		}

		return urlBuilder.toString();
	}

	public String getDeviceInformation(Set<Long> allDeviceID) {

		List<Long> deviceIDList = new ArrayList<>(allDeviceID);
		try {
			ResponseEntity<String> responseEntity = null;
//			String positionsEndpoint = "/api/devices?id=";
			String url = constructUrl(deviceIDList, HttpApi.DEVICE_DETALIS_FEATH_END_POINT);

			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(username, password);
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
			return null;
		}

	}

	public String getFilePresentOrNot(String fileName) {

		try {

//			Optional<Configuration> fileAccessUrl = configurationRepository.findByKey("FILE_ACCESS_BASE_URL");
//			if (fileAccessUrl.isPresent() && fileAccessUrl.get() != null) {
//				fileUrl = fileAccessUrl.get().getValue();
//			}

			ResponseEntity<String> responseEntity = null;
//			String positionsEndpoint = "/check?file=";
			String url = fileUrl + HttpApi.CHECK_FILE_END_POINT + fileName;

//			logger.info("check  filr url " + url);

			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(username, password);
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
			return null;
		}

	}

	public void sendHttpPostRequestForCommand(CommandSendDetails commandSendDetails) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBasicAuth(username, password);

			Map<String, Object> requestBody = new HashMap<>();
			Map<String, String> attributes = new HashMap<>();

			attributes.put("data", commandSendDetails.getCommand());
			requestBody.put("deviceId", commandSendDetails.getDeviceId());
			requestBody.put("id", "0");
			requestBody.put("type", "custom");
			requestBody.put("textChannel", "false");
			requestBody.put("description", "New…");
			requestBody.put("attributes", attributes);

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<Object> response = restTemplate.postForEntity(baseUrl + HttpApi.COMMAND_SEND_END_POINT,
					request, Object.class);

			if (response != null) {

			}

		} catch (Exception e) {
		}

	}

	public Object getPositionFrom(Long deviceId, String fromDate, String toDate) {
		try {
			ResponseEntity<String> responseEntity = null;
			String url = baseUrl + HttpApi.POISITIONS + "/?deviceId=" + deviceId;

			url = url + "&from=" + fromDate + "&to=" + toDate;
			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(username, password);
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("cache-control", "no-cache");

			HttpEntity<String> entity = new HttpEntity<>(headers);
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

			if (!responseEntity.getStatusCode().is2xxSuccessful()) {

				return null;
			} else {

				ObjectMapper objectMapper = new ObjectMapper();

				return objectMapper.readValue(responseEntity.getBody(), Object.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String getReportFrom(Long deviceId, String from, String to) {

		try {

//			String url = baseUrl + HttpApi.STOP_REPORT + "/?deviceId=";

			String url = baseUrl + HttpApi.REPORT + "?deviceId=";

			ResponseEntity<String> responseEntity = null;
			if (deviceId != null) {
				url += deviceId;
			} else {
				url += 2;
			}

			url += "&type=allEvents";

			url = url + "&from=" + from + "&to=" + to;
			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(username, password);
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("cache-control", "no-cache");

			HttpEntity<String> entity = new HttpEntity<>(headers);
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

			if (!responseEntity.getStatusCode().is2xxSuccessful()) {

				return null;
			} else {

//				ObjectMapper objectMapper = new ObjectMapper();

				return responseEntity.getBody();
			}
		} catch (Exception e) {
			return null;
		}

	}

	public Resource getFile(String fileName) {

		try {

//			Optional<Configuration> fileAccessUrl = configurationRepository.findByKey("FILE_ACCESS_URL_REST");
//			if (fileAccessUrl.isPresent() && fileAccessUrl.get() != null) {
//				fileUrl = fileAccessUrl.get().getValue();
//			}
			ResponseEntity<Resource> responseEntity = null;
//			String positionsEndpoint = "/check?file=";
			String url = fileUrl + HttpApi.GET_FILE + fileName;

			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(username, password);
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("cache-control", "no-cache");

			HttpEntity<String> entity = new HttpEntity<>(headers);
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Resource.class);
			if (!responseEntity.getStatusCode().is2xxSuccessful()) {
				return null;
			} else {

				return responseEntity.getBody();
			}
		} catch (Exception e) {
			return null;
		}

	}

	public String getPositionByPositionIdAndBatchSize(Long batchSize, Long positionId) {

		try {
			String url = baseUrl + HttpApi.LATEST_POISITIONS + "?lastPositionId=" + positionId + "&batchSize="
					+ batchSize;
			ResponseEntity<String> responseEntity = null;
			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(username, password);
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
			return null;
		}

	}

}
