package com.watsoo.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watsoo.dms.restclient.RestClientService;

@RestController
@RequestMapping("/api/positions")
public class TraccarController {

	@Autowired
	private RestClientService restClientService;

	@GetMapping
	public ResponseEntity<?> getPositionsByDeviceId(@RequestParam Long deviceId, @RequestParam String from,
			@RequestParam String to) {
		Object positionFrom = restClientService.getPositionFrom(deviceId, from, to);
		return new ResponseEntity<>(positionFrom, HttpStatus.OK);
	}

	@GetMapping("/report")
	public ResponseEntity<?> getReportByDeviceId(@RequestParam(required = false) Long deviceId,
			@RequestParam String from, @RequestParam String to) {

		Object reportFrom = restClientService.getReportFrom(deviceId, from, to);
		if (reportFrom != null) {
			return new ResponseEntity<>(reportFrom, HttpStatus.OK);
		}
		return new ResponseEntity<>(reportFrom, HttpStatus.BAD_REQUEST);
	}

}
