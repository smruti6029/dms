package com.watsoo.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watsoo.dms.dto.DriverDto;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.service.DriverService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/driver")
@Api(tags = "Driver Controller")
public class DriverController {

	@Autowired
	private DriverService driverService;

	@PostMapping("/save")
	public ResponseEntity<?> saveDriver(@RequestBody DriverDto driverDto) {

		Response<?> response = driverService.saveDriver(driverDto);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping("/getbyid")
	public ResponseEntity<?> getDriverById(@RequestParam("driverId") Long driverId) {
		Response<?> response = driverService.getDriverById(driverId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping("/getall")
	public ResponseEntity<?> getAllDriver(@RequestParam(required = false, defaultValue = "0") Integer pageSize,
			@RequestParam(required = false, defaultValue = "0") Integer pageNo,
			@RequestParam(required = false) String dlNumber) {
		Response<?> response = driverService.getAllDrivers(pageSize, pageNo, dlNumber);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping("/getall/perfomance")
	public ResponseEntity<?> getDriverPerfomance(@RequestParam(required = false, defaultValue = "0") Integer pageSize,
			@RequestParam(required = false, defaultValue = "0") Integer pageNo,
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate,
			@RequestParam(required = false) String dlNumber, @RequestParam(required = false) String eventType) {
		Response<?> response = driverService.getAllDriversWithPerfomance(fromDate, toDate, pageSize, pageNo, dlNumber,
				eventType);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}
	
	@GetMapping("/event/count")
	public ResponseEntity<?> getDriverEventsCount(@RequestParam("driverId") Long driverId) {
		Response<?> response = driverService.getDriverEventsCount(driverId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

}
