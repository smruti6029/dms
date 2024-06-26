package com.watsoo.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watsoo.dms.dto.CommandDto;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.service.CommandService;

@RestController
@RequestMapping("/api/commands")
public class CommandController {

	@Autowired
	private CommandService commandService;

	@GetMapping
	public ResponseEntity<?> getAllCommands(@RequestParam(required = false, defaultValue = "0") Integer pageSize,
			@RequestParam(required = false, defaultValue = "0") Integer pageNo,
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate,
			@RequestParam(required = false) String deviceModel, @RequestParam(required = false) Long vechileId,
			@RequestParam(required = false) String imeiNumber) {
		Response<?> response = commandService.getAllCommands(pageSize, pageNo, deviceModel, vechileId, imeiNumber);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCommandById(@PathVariable Long id) {
		Response<?> response = commandService.getCommandById(id);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@PostMapping("/create")
	public ResponseEntity<?> createCommand(@RequestBody CommandDto commandDto) {
		Response<?> response = commandService.createCommand(commandDto);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@PostMapping("/update")
	public ResponseEntity<?> updateCommand(@PathVariable Long id, @RequestBody CommandDto commandDto) {
		Response<?> response = commandService.updateCommand(id, commandDto);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

}
