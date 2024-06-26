package com.watsoo.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watsoo.dms.dto.EventDto;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.service.EventService;

@RestController
@RequestMapping("/api/event")
public class EventController {

	@Autowired
	private EventService eventService;

	@GetMapping("/getall")
	public ResponseEntity<?> getAllEvent(@RequestParam(required = false, defaultValue = "0") int pageSize,
			@RequestParam(required = false, defaultValue = "0") int pageNo,
			@RequestParam(required = false) String vehicleNo, @RequestParam(required = false) String driverName,
			@RequestParam(required = false) String eventType, @RequestParam(required = false) String searchKey,
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate,
			@RequestParam(required = false) String dlNumber, @RequestParam(required = false) Integer remarkId) {
		Response<?> response = eventService.getAllEvent(pageSize, pageNo, vehicleNo, driverName, eventType, searchKey,
				fromDate, toDate, dlNumber, remarkId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping("/getcounts")
	public ResponseEntity<?> fetchDashboardEventCounts(@RequestParam(required = false) String value) {
		Response<?> response = eventService.fetchDashBoardCounts(value);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));

	}

	@GetMapping("/get/type")
	public ResponseEntity<?> getAllEventType() {
		Response<?> response = eventService.getAllEventType();
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));

	}

	@GetMapping("/get/id")
	public ResponseEntity<?> getByid(@RequestParam Long eventId) {
		Response<?> response = eventService.getEventById(eventId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));

	}

	@PostMapping("/update")
	public ResponseEntity<?> updateEvent(@RequestBody EventDto eventDto) {
		Response<?> response = eventService.updateEvent(eventDto);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));

	}

	@GetMapping("/v1/dashboard")
	public ResponseEntity<?> getEventDetalisForDashBoard(@RequestBody EventDto eventDto) {
		Response<?> response = eventService.getEventDetalisForDashBoard();
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));

	}

	@GetMapping("/v1/driver/performance")
	public ResponseEntity<?> getDriverPerfomanceByEvent(@RequestParam String value,
			@RequestParam(required = false) String dlNumber) {
		Response<?> response = eventService.getEventDetalisForDriverPerfomance(value,dlNumber);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));

	}

}
