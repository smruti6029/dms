package com.watsoo.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watsoo.dms.dto.Response;
import com.watsoo.dms.service.VehicleService;

@RestController
@RequestMapping("/api/vehicle/")
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;

	@GetMapping("/getall")
	public ResponseEntity<?> getAllExcelFileName(@RequestParam(required = false, defaultValue = "0") int pageSize,
			@RequestParam(required = false, defaultValue = "0") int pageNo,
			@RequestParam(required = false) String vehicleNumber, @RequestParam(required = false) String vehicleName,
			@RequestParam(required = false) String imeiNumber) {
		Response<?> response = vehicleService.getAllVehicle(pageSize, pageNo, vehicleNumber, vehicleName,imeiNumber);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

}
