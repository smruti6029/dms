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

import com.watsoo.dms.dto.CommanddetalisSendDto;
import com.watsoo.dms.dto.Response;
import com.watsoo.dms.service.CommandSendDetalisService;

@RestController
@RequestMapping("/api/command/detalis")
public class CommandSendDetalisController {

	@Autowired
	private CommandSendDetalisService commandSendDetalisService;

	@GetMapping
	public ResponseEntity<?> getAllCommand(@RequestParam(required = false, defaultValue = "0") int pageSize,
			@RequestParam(required = false, defaultValue = "0") int pageNo) {
		Response<?> response = commandSendDetalisService.getAllCommandDetalis(pageSize, pageNo);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));

	}

	@PostMapping("/send")
	public ResponseEntity<?> sendCommand(@RequestBody CommanddetalisSendDto commanddetalisSendDto) {
		Response<?> response = commandSendDetalisService.sendCommandManually(commanddetalisSendDto);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));

	}

}
